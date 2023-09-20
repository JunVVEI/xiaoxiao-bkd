package com.xiaoxiao.bbs.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class AssociationQueryDTO {
    private Long currentPage = 1L;
    private Long pageSize = 20L;
    private List<String> keyWords;

    public static void checkIsValid(AssociationQueryDTO associationQueryDTO) {

    }
}
