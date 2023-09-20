package com.xiaoxiao.toolbag.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author yaoyao
 * @since 2023-08-04 12:12:53
 */
@Data
@TableName("free_room")
public class FreeRoom implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 周次
     */
    @TableField("week")
    private Integer week;

    /**
     * 星期
     */
    @TableField("day")
    private Integer day;

    @TableField("building_id")
    private String buildingId;

    @TableField("course_time")
    private String courseTime;

    /**
     * 教室号
     */
    @TableField("classroom")
    private String classroom;

    @TableField("seat_number")
    private Integer seatNumber;

    @TableField("exam_seat_number")
    private Integer examSeatNumber;


}
