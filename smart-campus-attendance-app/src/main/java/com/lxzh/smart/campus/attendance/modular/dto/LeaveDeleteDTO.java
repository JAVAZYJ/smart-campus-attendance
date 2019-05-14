package com.lxzh.smart.campus.attendance.modular.dto;

import com.lxzh.smart.campus.attendance.modular.entity.AttendanceStudentLeave;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zyj
 */
@Data
@ApiModel
public class LeaveDeleteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学生姓名
     */
    @ApiModelProperty("学生姓名")
    private String studentName;

    /**
     * 班级信息
     */
    @ApiModelProperty("班级信息")
    private String classInfo;

    /**
     * 班级学号
     */
    @ApiModelProperty("班级学号")
    private Long studentId;

    /**
     * AttendanceStudentLeave类数据更新
     */
    public static AttendanceStudentLeave updateData(AttendanceStudentLeave attendanceStudentLeave){
        AttendanceStudentLeave attendanceStudentLeave1 = new AttendanceStudentLeave();
        attendanceStudentLeave1.setId(attendanceStudentLeave.getId());
        attendanceStudentLeave1.setGradeInfo(attendanceStudentLeave.getGradeInfo());
        attendanceStudentLeave1.setClassInfo(attendanceStudentLeave.getClassInfo());
        attendanceStudentLeave1.setStudentName(attendanceStudentLeave.getStudentName());
        attendanceStudentLeave1.setStudentNumber(attendanceStudentLeave.getStudentNumber());
        attendanceStudentLeave1.setStudentId(attendanceStudentLeave.getStudentId());
        attendanceStudentLeave1.setCauseLeave(attendanceStudentLeave.getCauseLeave());
        attendanceStudentLeave1.setCreateTime(attendanceStudentLeave.getCreateTime());
        attendanceStudentLeave1.setRegisterPerson(attendanceStudentLeave.getRegisterPerson());
        attendanceStudentLeave1.setDelFlag(1L);
        attendanceStudentLeave1.setLeaveStart(attendanceStudentLeave.getLeaveStart());
        attendanceStudentLeave1.setCampusId(attendanceStudentLeave.getCampusId());
        attendanceStudentLeave1.setLeaveStart(attendanceStudentLeave.getLeaveStart());
        attendanceStudentLeave1.setLeaveEnd(attendanceStudentLeave.getLeaveEnd());
        return attendanceStudentLeave1;
    }
}
