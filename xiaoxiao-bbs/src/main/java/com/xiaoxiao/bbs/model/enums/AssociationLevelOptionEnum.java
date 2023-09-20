package com.xiaoxiao.bbs.model.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum AssociationLevelOptionEnum {

    AssociationLevelOptionEnum1(1, "校级组织", true),
    AssociationLevelOptionEnum2(2, "院级组织", true),
    AssociationLevelOptionEnum3(3, "兴趣社团", true),
    ;
    private final int id;
    private final String value;
    private final Boolean isEnable;


    public static List<String> getEnableValueList() {
        return Arrays.stream(values())
                .filter(AssociationLevelOptionEnum::getIsEnable)
                .map(AssociationLevelOptionEnum::getValue)
                .collect(Collectors.toList());
    }
}
