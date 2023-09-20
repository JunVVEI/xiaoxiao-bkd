package com.xiaoxiao.toolbag.model.vo;

import lombok.Data;

@Data
public class AIDrawingVO {
    String imageUrl;

    public AIDrawingVO(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
