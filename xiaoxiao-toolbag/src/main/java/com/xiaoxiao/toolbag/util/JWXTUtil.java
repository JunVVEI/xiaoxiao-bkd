package com.xiaoxiao.toolbag.util;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.xiaoxiao.common.api.StatusCode;
import com.xiaoxiao.common.exception.ApiException;
import com.xiaoxiao.common.util.AssertUtil;
import com.xiaoxiao.toolbag.model.bo.course.EducationSystemResponse;
import com.xiaoxiao.toolbag.model.bo.course.EducationSystemResponseMsgEnum;
import com.xiaoxiao.toolbag.model.bo.course.EducationSystemToken;
import com.xiaoxiao.toolbag.model.dto.course.EducationSystemLoginDTO;
import com.xiaoxiao.toolbag.model.vo.VerificationCodeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
public class JWXTUtil {
    private static volatile JWXTUtil instance;
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final HttpHeaders headers = new HttpHeaders();

    public static JWXTUtil getInstance() {
        if (instance == null) {
            synchronized (JWXTUtil.class) {
                if (instance == null) {
                    instance = new JWXTUtil();
                }
            }
        }
        return instance;
    }

    private JWXTUtil(){
        // 设置请求媒体数据类型
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 设置返回媒体数据类型
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
    }

    public String identifyVerificationCode(String img) {
        // 验证码base64处理
        if (img.startsWith("data:image/png;base64,")) {
            img = img.replace("data:image/png;base64,", "");
        }
        String body = "{\"img\" : \"" + img + "\"}";
        HttpEntity<String> formEntity = new HttpEntity<>(body, headers);
        // 自动识别验证码
        String res = restTemplate.postForObject("http://localhost:8518/ocr", formEntity, String.class);
        JSON resJson = JSONUtil.parse(res);
        String resCode = resJson.getByPath("code", String.class);
        if (StrUtil.equals(resCode, "1")) {
            return resJson.getByPath("result", String.class);
        }
        return "";
    }


    /**
     * 向教务系统发送请求获取验证码
     */
    public VerificationCodeVO getCode() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        long time = System.currentTimeMillis();
        String path = "https://jwxt.scau.edu.cn/secService/kaptcha?t=" + time + "&KAPTCHA-KEY-GENERATOR-REDIS=securityKaptchaRedisServiceAdapter";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Resource> responseEntity = restTemplate.exchange(path, HttpMethod.GET, new HttpEntity<>(headers), Resource.class);

        Resource body = responseEntity.getBody();
        if (ObjectUtil.isEmpty(body)) {
            log.error("获取教务系统验证码响应异常 {}", body);
            throw new ApiException(StatusCode.SERVER_BUSY);
        }

        // 获取验证码图片
        String encode;
        try (InputStream inputStream = body.getInputStream()) {
            byte[] bytes = InputStreamUtil.toArray(inputStream);
            // 将图片数据转为Base64
            encode = Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            log.error("教务系统验证码图片转换异常 {}", body, e);
            throw new ApiException(StatusCode.SERVER_BUSY);
        }

        // 获取cookie
        List<String> cookieList = responseEntity.getHeaders().get("Set-Cookie");
        if (cookieList == null || ObjectUtil.isEmpty(cookieList)) {
            log.error("教务系统验证码请求获取cookie失败 {}", responseEntity.getHeaders());
            throw new ApiException(StatusCode.SERVER_BUSY);
        }
        String cookie = cookieList.get(0);
        // 将cookie和base64封装成对象返回
        return new VerificationCodeVO(cookie, "data:image/png;base64," + encode);
    }


    /**
     * 登陆教务系统
     */
    private HttpHeaders login(EducationSystemLoginDTO educationSystemLoginDTO) {
        EducationSystemLoginDTO.checkIsValid(educationSystemLoginDTO);

        // 向教务系统发起登录请求
        HttpHeaders headers = new HttpHeaders();
        String url = "https://jwxt.scau.edu.cn/secService/login";

        ResponseEntity<String> res = new RestTemplate().exchange(
                url,
                HttpMethod.POST,
                prepareLoginHttpEntity(educationSystemLoginDTO, headers),
                String.class
        );
        EducationSystemResponse<EducationSystemToken> loginResponse = JSONUtil.toBean(
                res.getBody(),
                new TypeReference<EducationSystemResponse<EducationSystemToken>>() {
                }, false
        );

        // 判断是否填写正确,若填写错误抛出异常
        EducationSystemResponseMsgEnum msgEnum = EducationSystemResponseMsgEnum.getByValue(loginResponse.getErrorMessage());

        // 自动识别验证码错误,返回空对象用于重新识别验证码
        if (Objects.equals(msgEnum, EducationSystemResponseMsgEnum.KAPTCHA_INCORRECT)) {
            return null;
        }

        AssertUtil.isTrue(
                Objects.equals(msgEnum, EducationSystemResponseMsgEnum.SUCCESS),
                msgEnum.getMessage()
        );

        // 带上登录后返回的token
        String token = loginResponse.getData().getToken();
        if (StrUtil.isEmpty(token)) {
            log.error("登录异常,token为空 请求参数 {} 教务系统响应 {}", educationSystemLoginDTO, loginResponse);
            throw new ApiException(StatusCode.SERVER_BUSY);
        }
        headers.add("TOKEN", token);

        return headers;
    }

    /**
     * 准备教务系统登录请求
     */
    private HttpEntity<String> prepareLoginHttpEntity(EducationSystemLoginDTO educationSystemLoginDTO, HttpHeaders httpHeaders) {
        Map<String, String> paramsMap = new HashMap<>(8);
        paramsMap.put("userCode", educationSystemLoginDTO.getStudentNo());
        paramsMap.put("password", educationSystemLoginDTO.getPassword());
        paramsMap.put("kaptcha", educationSystemLoginDTO.getKaptcha());
        paramsMap.put("userCodeType", "account");

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Cookie", educationSystemLoginDTO.getCookie());
        httpHeaders.add("KAPTCHA-KEY-GENERATOR-REDIS", "securityKaptchaRedisServiceAdapter");
        httpHeaders.add("app", "PCWEB");

        return new HttpEntity<>(JSONUtil.toJsonStr(paramsMap), httpHeaders);
    }

    /**
     * 自动输入验证吗
     */
    public HttpHeaders solveCode(EducationSystemLoginDTO educationSystemLoginDTO){
        HttpHeaders httpHeaders = null;
        // 自动识别验证码,失败则重试,循环次数为loop
        int loop = 3;
        while (loop-- > 0) {
            // 自动识别验证码
            VerificationCodeVO verificationCodeVO = getCode();
            educationSystemLoginDTO.setCookie(verificationCodeVO.getSession());
            String code = identifyVerificationCode(verificationCodeVO.getBase64());
            if (StrUtil.isNotBlank(code)) {
                educationSystemLoginDTO.setKaptcha(code);
                // 登录
                httpHeaders = login(educationSystemLoginDTO);
                // httpHeaders不为空表明自动识别验证码成功
                if (Objects.nonNull(httpHeaders)) {
                    break;
                } else {
                    log.warn("自动识别到的验证码错误 {} ,重新获取", educationSystemLoginDTO.getKaptcha());
                }
            }
        }
        if (loop == -1) {
            throw new ApiException(StatusCode.SERVER_BUSY, "服务繁忙,请重新登录!");
        }
        return httpHeaders;
    }
}
