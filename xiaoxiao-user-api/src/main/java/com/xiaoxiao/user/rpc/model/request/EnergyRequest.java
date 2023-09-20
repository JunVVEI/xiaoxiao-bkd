package com.xiaoxiao.user.rpc.model.request;

import lombok.Data;

/**
 * @author yaoyao
 * @Description
 * @create 2023-09-02 0:51
 */
@Data
public class EnergyRequest {
    private Long userid;

    private Integer type;

    private Long relateId;
}
