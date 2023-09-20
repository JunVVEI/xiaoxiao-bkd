package com.xiaoxiao.user.model.dto;

import lombok.Data;

import java.util.Objects;

/**
 * @author yaoyao
 * @Description
 * @create 2023-08-31 22:52
 */
@Data
public class EnergyDTO {
    private Long userid;
    private Integer type;
    private Double count;
    private Long relateId;

    public static Boolean checkIsVaild(EnergyDTO energyDTO){
        if(Objects.isNull(energyDTO)){
            return Boolean.FALSE;
        }
        return Objects.nonNull(energyDTO.getUserid()) && Objects.nonNull(energyDTO.getRelateId()) && Objects.nonNull(energyDTO.getType());
    }
}
