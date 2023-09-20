package com.xiaoxiao.toolbag.model.dto.course;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.xiaoxiao.common.api.StatusCode;
import com.xiaoxiao.common.util.AssertUtil;
import lombok.Data;

/**
 * 教务系统登录数据封装类
 *
 * @author zjh
 */
@Data
public class EducationSystemLoginDTO {

    /**
     * 学号
     */
    String studentNo;

    /**
     * 校校uid
     */
    String uid;

    /**
     * 微信id
     */
    String wechatId;

    /**
     * 设备id
     */
    String deviceId;

    /**
     * 教务系统密码
     */
    String password;

    /**
     * 验证码
     */
    String kaptcha;

    /**
     * cookie
     */
    String cookie;

    public static void checkIsValid(EducationSystemLoginDTO dto) {
        AssertUtil.isTrue(NumberUtil.isNumber(dto.getStudentNo()) && dto.getStudentNo().length() == 12, StatusCode.VALIDATE_FAILED,"学号格式错误!");
    }

}
