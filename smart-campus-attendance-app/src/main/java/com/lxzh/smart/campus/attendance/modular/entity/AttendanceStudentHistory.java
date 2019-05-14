package com.lxzh.smart.campus.attendance.modular.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 学生考勤打卡历史记录实体类
 * </p>
 *
 * @author zyj
 * @since 2019-04-28
 */
@Data
@TableName("attendance_student_history")
public class AttendanceStudentHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @TableId("id")
    private Long id;

    /**
     * 学生编号
     */
    @TableField("student_number")
    private Long studentNumber;

    /**
     * 打卡时间
     */
    @TableField("clock_in_time")
    private LocalDateTime clockInTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 设备ID
     */
    @TableField("equipment_id")
    private Long equipmentId;

}
