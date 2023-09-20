package com.xiaoxiao.toolbag.controller;

import com.theokanning.openai.completion.chat.ChatMessage;
import com.xiaoxiao.common.annotation.XiaoXiaoResponseBody;
import com.xiaoxiao.toolbag.annotation.OpenAiLimit;
import com.xiaoxiao.toolbag.model.dto.course.AIDrawingDTO;
import com.xiaoxiao.toolbag.model.vo.AIDrawingVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tb/oa")
public class OpenAiController {


    @PostMapping("/chat")
    @XiaoXiaoResponseBody
    @OpenAiLimit
    public ChatMessage query(@RequestBody List<ChatMessage> chatMessages) {
        return null;
    }

    @PostMapping("/drawing")
    @XiaoXiaoResponseBody
    @OpenAiLimit
    public AIDrawingVO query(@RequestBody AIDrawingDTO aiDrawingDTO) {
        return null;
    }
}
