package com.xiaoxiao.bbs.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * <p>
 * 浏览历史
 * </p>
 *
 * @author yaoyao
 * @since 2023-05-28 10:24:34
 */
@Data
@TableName("browsing_history")
public class BrowsingHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 浏览帖子id
     */
    @TableField("browsing_history_id")
    private Long browsingHistoryId;

    /**
     * 浏览时间
     */
    @TableField("browsing_time")
    private Long browsingTime;


}
