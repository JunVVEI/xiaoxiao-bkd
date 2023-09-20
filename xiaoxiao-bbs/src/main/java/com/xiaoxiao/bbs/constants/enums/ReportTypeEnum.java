package com.xiaoxiao.bbs.constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum ReportTypeEnum {

    UNTREATED(1, "未处理"),

    BANNED(2, "已处理-封禁"),

    NORMAL(3, "已处理-正常"),

    DELETE(4, "已处理-删除");

    /**
     * 对应的值
     */
    private final Integer status;

    /**
     * 说明
     */
    private final String desc;

    public static ReportTypeEnum getReportTypeEnumByStatus(Integer status) {
        return Stream.of(ReportTypeEnum.values())
                .filter(reportTypeEnum -> Objects.equals(reportTypeEnum.getStatus(), status))
                .findFirst()
                .orElse(null);
    }

}
