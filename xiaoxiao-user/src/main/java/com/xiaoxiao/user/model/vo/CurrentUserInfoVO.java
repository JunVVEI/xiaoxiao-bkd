package com.xiaoxiao.user.model.vo;

import lombok.Data;

import javax.validation.Valid;
import java.sql.Timestamp;

@Data
@Valid
public class CurrentUserInfoVO {

    /**
     * id
     * 用户id
     */
    private Long id;

    /**
     * userName
     * 用户名
     */
    private String userName;

    /**
     * birth
     * 生日
     */

    private Timestamp birth;
    /**
     * backgroundImage
     * 背景图
     */
    private String backgroundImage;

    /**
     * sex
     * 性别
     */
    private Integer sex;

    /**
     * avatar
     * 头像
     */
    private String avatar;

    /**
     * realName
     * 真实姓名
     */
    private String realName;

    /**
     * email
     * 邮箱
     */
    private String email;

    /**
     * 用户被关注的 数量
     */
    private Integer followerCount;

    /**
     * 粉丝数量
     */
    private Integer followingCount;

    /**
     * 帖子发布数量
     */
    private Integer postCount;

    /**
     * 获取的点赞数量，仅帖子
     */
    private Integer likeCount;
}
