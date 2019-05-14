package com.lxzh.smart.campus.attendance.modular.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zyj
 * @since 2019-05-13
 */
@TableName("attendance_teacher_record")
@Data
public class AttendanceTeacherRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @TableField("id")
    private Long id;

    /**
     * 教师编号
     */
    @TableField("teacher_number")
    private Long teacherNumber;

    /**
     * 第一次打卡时间
     */
    @TableField("clock_start_time")
    private LocalDateTime clockStartTime;

    /**
     * 最后一次打卡时间
     */
    @TableField("clock_end_time")
    private LocalDateTime clockEndTime;

    /**
     * 传入数据库生成时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 0.迟到；1.早退；2.缺勤；3.默认缺勤
     */
    @TableField("type")
    private Long type;

    /**
     * 校区ID
     */
    @TableField("campus_id")
    private Long campusId;

}
