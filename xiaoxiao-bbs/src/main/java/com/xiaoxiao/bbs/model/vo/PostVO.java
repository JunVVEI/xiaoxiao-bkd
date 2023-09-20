package com.xiaoxiao.bbs.model.vo;

import cn.hutool.core.bean.BeanUtil;
import com.xiaoxiao.bbs.model.entity.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class PostVO {
    public Long id;

    /**
     * 帖子id
     */
    public Long postId;

    /**
     * 用户真实id
     */
    public Long userId;

    /**
     * 用户匿名id
     */
    public Long identityId;

    /**
     * 用户头像
     */
    public String avatar;

    /**
     * 帖子正文
     */
    public String content;

    /**
     * 发帖人用户名
     */
    public String creatorName;

    /**
     * 创建时间
     */
    public Timestamp createTime;

    /**
     * 分享量
     */
    public Integer shareCount;

    /**
     * 点赞量
     */
    public Integer likeCount;

    /**
     * 浏览量
     */
    public Integer viewCount;

	/**
	 * 评论量
	 */
	public Integer commentCount;

    /**
     * 点赞标志
     */
    public Boolean isLike;

	/**
	 * 媒体存储路径
	 */
	public String mediaPath;

    /**
     * 是否是创建者
     */
    public Boolean isCreator;

	public static PostVO preparePostVo(Post post) {
		PostVO postVO = new PostVO();
		BeanUtil.copyProperties(post, postVO);
		postVO.setIsLike(false);
		return postVO;
	}

}
