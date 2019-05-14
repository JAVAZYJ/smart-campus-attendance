package com.lxzh.smart.campus.attendance.modular.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 学生考勤以班级为单位统计迟到、早退、缺勤实体类
 * </p>
 *
 * @author zyj
 * @since 2019-04-28
 */
@Data
@TableName("attendance_student_count")
public class AttendanceStudentCount implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private Long id;

    /**
     * 班级ID
     */
    @TableField("class_id")
    private Long classId;

    /**
     * 班级名称
     */
    @TableField("class_name")
    private String className;

    /**
     * 考勤时间
     */
    @TableField("time")
    private LocalDateTime time;

    /**
     * 迟到
     */
    @TableField("late")
    private Integer late;

    /**
     * 早退
     */
    @TableField("leave_early")
    private Integer leaveEarly;

    /**
     * 缺勤
     */
    @TableField("absence")
    private Integer absence;

    /**
     * 校区ID
     */
    @TableField("campus_id")
    private Long campusId;

}
