package com.xiaoxiao.toolbag.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.xiaoxiao.toolbag.model.dto.course.EducationSystemLoginDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据库实体类
 *
 * @author zjh
 */
@Data
@TableName("course_info")
@NoArgsConstructor
@AllArgsConstructor
public class CourseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("student_id")
    private String studentId;

    @TableField("term")
    private String term;

    @TableField("detail")
    private String detail;

    @TableField("note")
    private String note;

    @TableField("uid")
    private String uid;

    @TableField("wechat_id")
    private String wechatId;

    @TableField("device_id")
    private String deviceId;

    @TableField("is_delete")
    @TableLogic(value="0",delval="1")
    private Byte isDelete;

    public CourseInfo(EducationSystemLoginDTO loginDTO){
        this.studentId=loginDTO.getStudentNo();
        this.uid=loginDTO.getUid();
        this.wechatId=loginDTO.getWechatId();
        this.deviceId=loginDTO.getDeviceId();
    }
}
