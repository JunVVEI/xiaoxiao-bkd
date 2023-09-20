package com.xiaoxiao.baseservice.controller;

import com.xiaoxiao.baseservice.service.WXService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import javax.annotation.Resource;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@RestController
@RequestMapping("/bs/wx")
public class WXController {

    @Resource
    private WXService wxService;

    @PostMapping("/test")
    public ResponseEntity<String> processSubscribe(@RequestBody String xmlData) throws ParserConfigurationException, IOException, SAXException {
        return ResponseEntity.ok(wxService.processSubscribe(xmlData));
    }

}
