package com.lxzh.smart.campus.attendance.modular.vo;

import com.lxzh.smart.campus.attendance.modular.entity.AttendanceStudentCount;
import com.lxzh.smart.campus.attendance.modular.entity.AttendanceStudentHistory;
import com.lxzh.smart.campus.attendance.modular.entity.AttendanceTeacherHistory;
import com.lxzh.smart.campus.basicinfo.api.model.ClassProvide;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 教师/学生考勤打卡DTO
 * @author zyj
 * @since 2019-4-28 10:16:25
 */
@Data
@ApiModel
public class ClockInVO {

    /**
     * 教师/学生编号
     */
    @ApiModelProperty("教师/学生编号")
    private Long number;

    /**
     * 打卡时间
     */
    @ApiModelProperty("打卡时间")
    private LocalDateTime clockInTime;

    /**
     * 设备ID
     */
    @ApiModelProperty("设备ID")
    private Long equipmentId;

    /**
     * 学生打卡转换学生打卡历史记录DTO
     */
    public static AttendanceStudentHistory historyTransformStudent(ClockInVO clockInDTO){
        AttendanceStudentHistory attendanceStudentHistory = new AttendanceStudentHistory();
        attendanceStudentHistory.setStudentNumber(clockInDTO.getNumber());
        attendanceStudentHistory.setEquipmentId(clockInDTO.getEquipmentId());
        attendanceStudentHistory.setClockInTime(clockInDTO.getClockInTime());
        attendanceStudentHistory.setCreateTime(LocalDateTime.now());
        return attendanceStudentHistory;
    }

    /**
     * 教师打卡转换教师打卡历史记录DTO
     */
    public static AttendanceTeacherHistory historyTransformTeacher(ClockInVO clockInDTO){
        AttendanceTeacherHistory attendanceTeacherHistory = new AttendanceTeacherHistory();
        attendanceTeacherHistory.setTeacherNumber(clockInDTO.getNumber());
        attendanceTeacherHistory.setEquipmentId(clockInDTO.getEquipmentId());
        attendanceTeacherHistory.setClockInTime(clockInDTO.getClockInTime());
        attendanceTeacherHistory.setCreateTime(LocalDateTime.now());
        return attendanceTeacherHistory;
    }

    /**
     * 学生打卡转换为已班为单位的考勤统计DTO
     */
    public static AttendanceStudentCount countTransformStudent(ClassProvide classes,Integer v1,Integer v2,Integer v3){
        AttendanceStudentCount attendanceStudentCount = new AttendanceStudentCount();
        attendanceStudentCount.setClassId(classes.getId());
        attendanceStudentCount.setClassName(classes.getName());
        attendanceStudentCount.setLate(v1);
        attendanceStudentCount.setLeaveEarly(v2);
        attendanceStudentCount.setAbsence(v3);
        return attendanceStudentCount;
    }
}
