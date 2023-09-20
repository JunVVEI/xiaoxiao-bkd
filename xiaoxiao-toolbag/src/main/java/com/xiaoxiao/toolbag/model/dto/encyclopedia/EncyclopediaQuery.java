package com.xiaoxiao.toolbag.model.dto.encyclopedia;

import com.xiaoxiao.common.api.BasePageCondition;
import com.xiaoxiao.common.util.AssertUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
public class EncyclopediaQuery extends BasePageCondition {

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 排序方式
     */
    private Integer sortType = 1;

    /**
     * true 升序, false 升序
     */
    private Boolean isAsc = true;

    @Getter
    @AllArgsConstructor
    private enum SortTypeEnum {

        /**
         * 按创建时间排序
         */
        DEFAULT(1, "create_time"),
        /**
         * 按照浏览量排序
         */
        VIEW_COUNT(2, "view_count"),
        /**
         * 按自定义的排序权重排序
         */
        SORT_WEIGHT(3, "sort_weight");


        /**
         * 排序类型标识
         */
        private final Integer type;

        /**
         * 排序字段,方法引用
         */
        private final String orderColumn;

        public static SortTypeEnum getSortTypeEnum(Integer type) {
            SortTypeEnum sortTypeEnum = Arrays.stream(SortTypeEnum.values())
                    .filter(item -> Objects.equals(item.getType(), type))
                    .findFirst()
                    .orElse(null);
            AssertUtil.isTrue(Objects.nonNull(sortTypeEnum), "不支持的排序方式");
            return sortTypeEnum;
        }

    }

    /**
     * @return 返回排序字段
     */
    public String getSortField() {
        return SortTypeEnum.getSortTypeEnum(this.getSortType()).getOrderColumn();
    }

    public static void checkIsValid(EncyclopediaQuery encyclopediaQuery) {
        // TODO: 参数校验，后面补充
    }
}
