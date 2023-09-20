package com.xiaoxiao.baseservice.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaoxiao.baseservice.config.WeChatConfig;
import com.xiaoxiao.baseservice.model.bo.*;
import com.xiaoxiao.baseservice.rpc.model.WeChatInformTypeEnum;
import com.xiaoxiao.baseservice.rpc.model.resquest.SendWeChatInformRequest;
import com.xiaoxiao.baseservice.service.RpcService;
import com.xiaoxiao.baseservice.service.WXService;
import com.xiaoxiao.common.cache.RedisService;
import com.xiaoxiao.common.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class WXServiceImpl implements WXService {

    @Resource
    private RpcService rpcService;

    @Resource
    private RedisService redisService;

    @Resource
    private WeChatConfig weChatConfig;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String SEND_INFORM_API_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token={ACCESS_TOKEN}";

    private static final String GET_ACCESS_TOKEN_API = "https://api.weixin.qq.com/cgi-bin/token";

    private static final String MSG_SEC_CHECK_API = "https://api.weixin.qq.com/wxa/msg_sec_check";

    public static final String WECHAT_SERVICE_ACCOUNT_ACCESS_TOKEN = "WECHAT_SERVICE_ACCOUNT_ACCESS_TOKEN";

    public static final String WECHAT_ACCESS_TOKEN = "WECHAT_ACCESS_TOKEN";


    @Override
    public String getServiceAccountAccessToken() {
        String accessToken = redisService.get(WECHAT_SERVICE_ACCOUNT_ACCESS_TOKEN);

        if (StringUtils.isBlank(accessToken)) {
            String url = GET_ACCESS_TOKEN_API
                    + "?grant_type=client_credential"
                    + "&appid=" + weChatConfig.getServiceAccountAppid()
                    + "&secret=" + weChatConfig.getServiceAccountSecret();
            String result = restTemplate.getForObject(url, String.class);
            log.info("获取微信服务号AccessToken结果 {}", result);
            JSON parse = JSONUtil.parse(result);
            accessToken = (String) parse.getByPath("access_token");
            redisService.set(
                    WECHAT_SERVICE_ACCOUNT_ACCESS_TOKEN,
                    accessToken,
                    7000,
                    TimeUnit.SECONDS
            );
        }
        return accessToken;
    }

    @Override
    public String processSubscribe(String xmlData) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xmlData)));
        document.getDocumentElement().normalize();
        String msgType = document.getElementsByTagName("MsgType").item(0).getTextContent();
        if (!msgType.equals("event")) {
            return "success";
        }
        EventMsg eventMsg = new EventMsg(document);
        if (!eventMsg.getEvent().equals("subscribe")) {
            return "success";
        }
        String openId = eventMsg.getFromUserName();
        String accessToken = getServiceAccountAccessToken();
        String unionId = getUnionId(accessToken, openId);
        rpcService.fillOpenidByUnionId(unionId, openId);
        return String.format(
                "<xml>\n" +
                        "  <ToUserName><![CDATA[%s]]></ToUserName>\n" +
                        "  <FromUserName><![CDATA[%s]]></FromUserName>\n" +
                        "  <CreateTime>%s</CreateTime>\n" +
                        "  <MsgType><![CDATA[image]]></MsgType>\n" +
                        "  <Image>\n" +
                        "    <MediaId><![CDATA[%s]]></MediaId>\n" +
                        "  </Image>\n" +
                        "</xml>",
                openId,
                "xiaoxiaoAssistant",
                System.currentTimeMillis(),
                weChatConfig.getSubscribeMediaId()
        );
    }

    public String getUnionId(String accessToken, String openid) {
        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken + "&openid=" + openid;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<WXUserInfoResponse> response = restTemplate.getForEntity(url, WXUserInfoResponse.class);
        if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
            return response.getBody().getUnionId();
        } else {
            throw new RuntimeException("Failed to get unionid from url: " + url);
        }
    }

    @Override
    public boolean sendWeChatInform(SendWeChatInformRequest sendWeChatInformRequest) {
        if (!checkIsValid(sendWeChatInformRequest)) {
            return false;
        }
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<WXInformRequest> entity = new HttpEntity<>(getWxInfoReq(sendWeChatInformRequest));
        ResponseEntity<WXInformResponse> responseEntity = restTemplate.exchange(
                SEND_INFORM_API_URL,
                HttpMethod.POST,
                entity,
                WXInformResponse.class,
                getServiceAccountAccessToken()
        );

        if (responseEntity.getBody().getErrcode() != 0) {
            log.error("微信服务号通知发送失败 {} {}", sendWeChatInformRequest, responseEntity.getBody());
            return false;
        }
        return true;
    }

    private boolean checkIsValid(SendWeChatInformRequest sendWeChatInformRequest) {
        WeChatInformTypeEnum weChatInformTypeEnumByType = WeChatInformTypeEnum.getWeChatInformTypeEnumByType(
                sendWeChatInformRequest.getWeChatInformType()
        );
        String content = sendWeChatInformRequest.getContent();
        return StringUtils.isNotBlank(content) && Objects.nonNull(weChatInformTypeEnumByType);
    }

    private WXInformRequest getWxInfoReq(SendWeChatInformRequest sendWeChatInformRequest) {
        WXInformRequest req = new WXInformRequest();
        // 小程序配置
        WXInformMiniProgram wxInformMiniProgram = new WXInformMiniProgram();
        wxInformMiniProgram.setAppid(weChatConfig.getMiniAppid());
        wxInformMiniProgram.setPagepath("pages/home/index");
        if (sendWeChatInformRequest.getPagePath() != null) {
            wxInformMiniProgram.setPagepath(sendWeChatInformRequest.getPagePath());
        }
        req.setMiniprogram(wxInformMiniProgram);

        // 发送目标配置
        String serviceAccountOpenId = rpcService.getUserServiceAccountOpenId(sendWeChatInformRequest.getUid());
        AssertUtil.isTrue(StringUtils.isNotBlank(serviceAccountOpenId), "获取用户openid失败");
        req.setTouser(serviceAccountOpenId);

        // 模版信息填充
        req.setTemplate_id("3VCCcXyws7hqMb67l0JSLzy0j1LBmbXCX0ZBUJ8YMrQ");
        WXInformData wxInformData = new WXInformData();

        WeChatInformTypeEnum weChatInformTypeEnum = WeChatInformTypeEnum.getWeChatInformTypeEnumByType(
                sendWeChatInformRequest.getWeChatInformType()
        );
        if (Objects.equals(weChatInformTypeEnum, WeChatInformTypeEnum.POST_COURSE)) {
            AssertUtil.isTrue(
                    StringUtils.isNotBlank(sendWeChatInformRequest.getWeChatInformTypeString()),
                    "课程推送消息异常，内容为空"
            );
            wxInformData.setKeyword1(sendWeChatInformRequest.getWeChatInformTypeString());
        } else {
            wxInformData.setKeyword1(weChatInformTypeEnum.getShowContent());
        }
        wxInformData.setKeyword2(sendWeChatInformRequest.getContent());
        req.setData(wxInformData);

        return req;
    }

    @Override
    public String getMiniAppAccessToken() {
        String accessToken = redisService.get(WECHAT_ACCESS_TOKEN);

        if (StringUtils.isBlank(accessToken)) {
            String url = GET_ACCESS_TOKEN_API
                    + "?grant_type=client_credential"
                    + "&appid=" + weChatConfig.getMiniAppid()
                    + "&secret=" + weChatConfig.getMiniSecret();
            String result = restTemplate.getForObject(url, String.class);
            log.info("获取微信小程序AccessToken结果 {}", result);
            JSON parse = JSONUtil.parse(result);
            accessToken = (String) parse.getByPath("access_token");
            redisService.set(
                    WECHAT_ACCESS_TOKEN,
                    accessToken,
                    7000,
                    TimeUnit.SECONDS
            );
        }
        return accessToken;
    }

    public boolean msgSecCheck(String content, String openid) {
        String url = MSG_SEC_CHECK_API + "?access_token=" + getMiniAppAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode requestBody = objectMapper.createObjectNode()
                .put("openid", openid)
                .put("scene", 3)
                .put("version", 2)
                .put("content", content);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        String responseBody = responseEntity.getBody();
        JSON body = JSONUtil.parse(responseBody);
        String suggest = (String) body.getByPath("result.suggest");
        log.info("content: {}, openid: {}, res: {}", content, openid, responseBody);
        return Objects.equals(suggest, "pass");

    }

}
