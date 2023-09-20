package com.xiaoxiao.user.model.vo;

import lombok.Data;

/**
 * @author yaoyao
 * @Description
 * @create 2023-08-31 22:59
 */
@Data
public class UserEnergyVO {
    private Double sum;
    private Double count;
    private Long userid;
    private Integer type;
}
