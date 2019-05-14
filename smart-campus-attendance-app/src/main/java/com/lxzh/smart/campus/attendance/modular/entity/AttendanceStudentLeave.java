package com.lxzh.smart.campus.attendance.modular.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 学生考勤请假类
 * </p>
 *
 * @author zyj
 * @since 2019-04-23
 */
@ApiModel
@Data
@TableName("attendance_student_leave")
public class AttendanceStudentLeave implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @ApiModelProperty("序号")
    @TableField("id")
    private Long id;

    /**
     * 年级
     */
    @ApiModelProperty("年级")
    @TableField("grade_info")
    private String gradeInfo;

    /**
     * 班级
     */
    @ApiModelProperty("班级")
    @TableField("class_info")
    private String classInfo;

    /**
     * 班级ID
     */
    @ApiModelProperty("班级ID")
    @TableField("class_id")
    private Long classId;

    /**
     * 学生姓名
     */
    @ApiModelProperty("学生姓名")
    @TableField("student_name")
    private String studentName;

    /**
     * 学生编号
     */
    @ApiModelProperty("学生编号")
    @TableField("student_number")
    private Long studentNumber;

    /**
     * 班级学号
     */
    @ApiModelProperty("班级学号")
    @TableField("student_id")
    private Long studentId;

    /**
     * 学期信息
     */
    @ApiModelProperty("学期信息")
    @TableField("term_id")
    private Long termId;

    /**
     * 开始请假的时间
     */
    @ApiModelProperty("开始请假的时间")
    @TableField("leave_start")
    private LocalDateTime leaveStart;

    /**
     * 结束请假的时间
     */
    @ApiModelProperty("结束请假的时间")
    @TableField("leave_end")
    private LocalDateTime leaveEnd;

    /**
     * 请假描述
     */
    @ApiModelProperty("请假描述")
    @TableField("cause_leave")
    private String causeLeave;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    @TableField("register_person")
    private String registerPerson;

    /**
     * 创建人ID
     */
    @ApiModelProperty("创建人")
    @TableField("register_id")
    private Long registerId;

    /**
     * 删除标记(0.代表有效；1.代表无效)
     */
    @ApiModelProperty("删除标记(0.代表有效；1.代表无效)")
    @TableField("del_flag")
    private Long delFlag;

    /**
     * 请假进度(0:代表请假完成；1请假申请中)
     */
    @ApiModelProperty("请假进度(0:代表请假完成；1请假申请中)")
    @TableField("leave_state")
    private Long leaveState;

    /**
     * 校区ID
     */
    @ApiModelProperty("校区ID")
    @TableField("campus_id")
    private Long campusId;

}
