package com.xiaoxiao.toolbag.model.dto.course;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xiaoxiao.common.api.StatusCode;
import com.xiaoxiao.common.util.AssertUtil;
import lombok.Data;

/**
 * @author shkstart
 * @create 2023-02-17 20:24
 */
@Data
public class TermQueryDTO {
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
     * 学期
     */
    String term;

    public static void checkIsValid(TermQueryDTO termQueryDTO) {
        AssertUtil.isTrue(
                ObjectUtil.isNotEmpty(termQueryDTO.getStudentNo())
                        || ObjectUtil.isNotEmpty(termQueryDTO.getUid())
                        || ObjectUtil.isNotEmpty(termQueryDTO.getWechatId())
                        || ObjectUtil.isNotEmpty(termQueryDTO.getDeviceId()),
                StatusCode.VALIDATE_FAILED
        );
        AssertUtil.isTrue(StrUtil.isNotEmpty(termQueryDTO.getTerm()),StatusCode.VALIDATE_FAILED,"缺少学期参数!");
    }

}
