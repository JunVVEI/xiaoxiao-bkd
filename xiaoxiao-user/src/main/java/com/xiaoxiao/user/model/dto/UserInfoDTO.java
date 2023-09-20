package com.xiaoxiao.user.model.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Valid
public class UserInfoDTO {
    /**
     * id
     * 用户id
     */
    @NotNull(message = "id不能为null")
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
    @Pattern(regexp = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$\n",
            message = "不符时间规则")
    private String birth;
    /**
     * backgroundImage
     * 背景图
     */
    @Pattern(regexp = "[a-zA-z]+://[^\\s]*", message = "背景校验错误")
    private String backgroundImage;
    /**
     * sex
     * 性别
     */
    @Pattern(regexp = "[01]", message = "性别校验错误")
    private Integer sex;
    /**
     * avatar
     * 头像
     */
    @Pattern(regexp = "[a-zA-z]+://[^\\s]*", message = "头像校验错误")
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
    @Email
    private String email;
}
