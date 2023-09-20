package com.xiaoxiao.bbs.model.dto;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.springframework.util.Assert;

@Data
public class PostDTO {
    /**
     * 用户真实id
     */
    private Long userId;

    /**
     * 用户匿名身份id
     */
    private Long identityId;

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 帖子正文
     */
    private String content;

    /**
     * 媒体存储路径
     */
    private String mediaPath;

    /**
     * 标签id
     */
    private Long tagId;

    /**
     * 参数校验
     *
     * @param postDTO 帖子数据传输对象
     */
    public static void checkIsValid(PostDTO postDTO) {
        Assert.isTrue(StrUtil.isNotBlank(postDTO.getContent()), "正文不能为空");
    }

}
