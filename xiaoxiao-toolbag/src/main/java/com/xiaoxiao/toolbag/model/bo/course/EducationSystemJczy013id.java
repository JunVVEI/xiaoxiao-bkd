package com.xiaoxiao.toolbag.model.bo.course;


import cn.hutool.core.annotation.Alias;
import lombok.Data;

import java.util.List;

/**
 * 教务系统参数
 *
 * @author zjh
 */
@Data
public class EducationSystemJczy013id {

    @Alias("JwPublicXnxq")
    List<JwPublicXnxq> xnxq;

    @Data
    public class JwPublicXnxq {
        @Alias("sfdqxqcode")
        String code;//是否当前学期 0否，1是
        @Alias("name")
        String jczy013id;//学期参数,形同"2022-2023-1"

    }
}

