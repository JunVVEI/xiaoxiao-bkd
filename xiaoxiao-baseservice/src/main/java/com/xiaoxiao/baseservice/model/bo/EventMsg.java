package com.xiaoxiao.baseservice.model.bo;

import org.w3c.dom.Document;


public class EventMsg extends Msg{
    private String event;

    public EventMsg(Document document){
        super(document);
        this.event = document.getElementsByTagName("Event").item(0).getTextContent();
    }

    public String getEvent(){
        return this.event;
    }
}
