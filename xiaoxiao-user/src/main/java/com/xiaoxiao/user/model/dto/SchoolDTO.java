package com.xiaoxiao.user.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class SchoolDTO {
    /**
     * uid
     * 用户id
     */
    @NotNull(message = "id不能为空")
    private Long id;
    /**
     * schoolNo
     * 学号
     */
    @NotNull(message = "学号不能为空")
    private Integer schoolNo;
    /**
     * realName
     * 真实姓名
     */
    @NotNull(message = "真实姓名不能为空")
    private String realName;
    /**
     * schoolName
     * 学校名字
     */
    @NotNull(message = "学校名字不能为空")
    private String schoolName;
    /**
     * major
     * 专业
     */
    @NotNull(message = "专业不能为空")
    private String major;
    /**
     * enrollmentYear
     * 入学年份
     */
    @NotNull(message = "入学日期不能为空")
    @Pattern(regexp = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$\n",
            message = "不符时间规则")
    private String enrollmentYear;
    /**
     * photoUrl
     * 认证图片
     */
    @NotNull(message = "认证图片不能为空")
    @Pattern(regexp = "[a-zA-z]+://[^\\s]*", message = "图片路径校验错误")
    private String photoUrl;
    /**
     * campusIdentity
     * 身份
     */
    @NotNull(message = "身份不能为空")
    private Integer campusIdentity;
    /**
     * identification
     * 学校认证
     */
    private Integer identification;
}
