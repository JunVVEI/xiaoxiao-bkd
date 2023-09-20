package com.xiaoxiao.bbs.model.dto;

import com.xiaoxiao.bbs.constants.enums.ReportTypeEnum;
import com.xiaoxiao.common.api.StatusCode;
import com.xiaoxiao.common.util.AssertUtil;
import lombok.Data;

@Data
public class HandleReportDTO {

    /**
     * 举报id
     */
    private Long reportId;

    /**
     * 处理类型
     * {@link ReportTypeEnum}
     */
    private Integer status;

    public static void checkIsValid(HandleReportDTO handleReportDTO) {
        AssertUtil.isTrue(handleReportDTO.getReportId() != null, StatusCode.VALIDATE_FAILED);
        AssertUtil.isTrue(handleReportDTO.getStatus() != null, StatusCode.VALIDATE_FAILED);
    }

}
