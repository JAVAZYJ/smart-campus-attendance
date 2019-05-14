package com.lxzh.smart.campus.attendance.modular.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zyj
 * @since 2019-05-13
 */
@TableName("attendance_teacher_history")
public class AttendanceTeacherHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @TableId("id")
    private Long id;

    /**
     * 教师编号
     */
    @TableField("teacher_number")
    private Long teacherNumber;

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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTeacherNumber() {
        return teacherNumber;
    }

    public void setTeacherNumber(Long teacherNumber) {
        this.teacherNumber = teacherNumber;
    }

    public LocalDateTime getClockInTime() {
        return clockInTime;
    }

    public void setClockInTime(LocalDateTime clockInTime) {
        this.clockInTime = clockInTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    @Override
    public String toString() {
        return "AttendanceTeacherHistory{" +
        "id=" + id +
        ", teacherNumber=" + teacherNumber +
        ", clockInTime=" + clockInTime +
        ", createTime=" + createTime +
        ", equipmentId=" + equipmentId +
        "}";
    }
}
