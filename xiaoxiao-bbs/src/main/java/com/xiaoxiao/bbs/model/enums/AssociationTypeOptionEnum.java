package com.xiaoxiao.bbs.model.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum AssociationTypeOptionEnum {
    AssociationTypeOptionEnum1(1, "校团委下属组织", true),
    AssociationTypeOptionEnum2(2, "学术类", true),
    AssociationTypeOptionEnum3(3, "文体类", true),
    AssociationTypeOptionEnum4(4, "志愿服务类", true),
    ;
    private final int id;
    private final String value;
    private final Boolean isEnable;


    public static List<String> getEnableBalueList() {
        return Arrays.stream(values())
                .filter(AssociationTypeOptionEnum::getIsEnable)
                .map(AssociationTypeOptionEnum::getValue)
                .collect(Collectors.toList());
    }
}
