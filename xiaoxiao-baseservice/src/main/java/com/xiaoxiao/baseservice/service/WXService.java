package com.xiaoxiao.baseservice.service;

import com.xiaoxiao.baseservice.rpc.model.resquest.SendWeChatInformRequest;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface WXService {

    String processSubscribe(String xmlData) throws ParserConfigurationException, IOException, SAXException;

    String getServiceAccountAccessToken();

    boolean sendWeChatInform(SendWeChatInformRequest sendWeChatInformRequest);

    String getMiniAppAccessToken();

    boolean msgSecCheck(String content, String openid);
}
