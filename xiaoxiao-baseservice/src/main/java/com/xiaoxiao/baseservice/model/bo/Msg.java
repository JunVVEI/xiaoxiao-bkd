package com.xiaoxiao.baseservice.model.bo;

import lombok.Data;
import org.w3c.dom.Document;
@Data
public class Msg {
    private String ToUserName;
    private String FromUserName;
    private String CreateTime;
    private String MsgType;
    private String MsgId;

    public Msg(Document document) {
        this.ToUserName = document.getElementsByTagName("ToUserName").item(0).getTextContent();
        this.FromUserName = document.getElementsByTagName("FromUserName").item(0).getTextContent();
        this.CreateTime = document.getElementsByTagName("CreateTime").item(0).getTextContent();
        this.MsgType = document.getElementsByTagName("MsgType").item(0).getTextContent();
      //  this.MsgId = document.getElementsByTagName("MsgId").item(0).getTextContent();
    }

}
