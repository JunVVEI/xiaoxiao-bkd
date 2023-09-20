package com.xiaoxiao.toolbag.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RedirectController {

    @RequestMapping("/tb/open/coursePage")
    public String openCoursePage() {
        return "redirect:weixin://dl/business/?t=70CfMYlkULd";
    }
}
