package com.lxzh.smart.campus.attendance.modular.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.apache.tomcat.jni.Local;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 学生考勤打卡基本信息实体类
 * </p>
 *
 * @author zyj
 * @since 2019-04-28
 */
@Data
@TableName("attendance_student_record")
public class AttendanceStudentRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @TableField("id")
    private Long id;

    /**
     * 学生编号
     */
    @TableField("student_number")
    private Long studentNumber;

    /**
     * 班级信息
     */
    @TableField("grade_id")
    private Long gradeId;

    /**
     * 班级信息
     */
    @TableField("class_id")
    private Long classId;

    /**
     * 学期信息
     */
    @TableField("term_id")
    private Long termId;

    /**
     * 开始打卡时间
     */
    @TableField("clock_start_time")
    private LocalDateTime clockStartTime;

    /**
     * 最后打卡时间
     */
    @TableField("clock_end_time")
    private LocalDateTime clockEndTime;

    /**
     * 0.迟到；1.早退；2.缺勤
     */
    @TableField("type")
    private Long type;

    /**
     * 校区ID
     */
    @TableField("campus_id")
    private Long campusId;

    /**
     * 学校id
     */
    @TableField("school_id")
    private Long schoolId;

    /**
     * 学校id
     */
    @TableField("create_time")
    private LocalDateTime createTime;
}
