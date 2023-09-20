package com.xiaoxiao.bbs.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaoxiao.bbs.service.ContentSecurityService;
import com.xiaoxiao.bbs.service.RpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Objects;

@Service
@Slf4j
public class ContentSecurityServiceImpl implements ContentSecurityService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String MSG_SEC_CHECK_API = "https://api.weixin.qq.com/wxa/msg_sec_check";

    @Resource
    private RpcService rpcService;

    public boolean msgSecCheck(String content, String openid) {
        String url = MSG_SEC_CHECK_API + "?access_token=" + rpcService.getMiniAppAccessToken();

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
