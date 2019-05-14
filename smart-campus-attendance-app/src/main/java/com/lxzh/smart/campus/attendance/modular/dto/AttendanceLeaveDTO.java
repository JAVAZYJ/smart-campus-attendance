package com.lxzh.smart.campus.attendance.modular.dto;

import cn.stylefeng.roses.system.api.context.AdditionalInfo;
import cn.stylefeng.roses.system.api.context.LoginUser;
import com.lxzh.smart.campus.api.model.TeacherInfoVo;
import com.lxzh.smart.campus.attendance.modular.entity.AttendanceStudentLeave;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 请假DTO
 * </p>
 *
 * @author zyj
 * @since 2019-04-23
 */
@Data
@ApiModel
public class AttendanceLeaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 班级/id
     */
    @ApiModelProperty("班级")
    private String classInfo;

    @ApiModelProperty("班级ID")
    private Long classId;

    /**
     * 学生姓名
     */
    @ApiModelProperty("学生姓名")
    private String studentName;
    /**
     * 学生ID
     */
    @ApiModelProperty("学生ID")
    private Long studentId;

    /**
     * 开始请假的时间
     */
    @ApiModelProperty("开始请假的时间")
    private LocalDateTime leaveStart;

    /**
     * 结束请假的时间
     */
    @ApiModelProperty("结束请假的时间")
    private LocalDateTime leaveEnd;

    /**
     * 请假描述
     */
    @ApiModelProperty("请假描述")
    private String causeLeave;

    /**
     * 转换AttendanceStudentLeave
     */
    public static AttendanceStudentLeave studentLeaveTransform(AttendanceLeaveDTO attendanceLeaveDTO,AdditionalInfo additionalInfo,LoginUser loginUser, Long term){
        AttendanceStudentLeave attendanceStudentLeave = new AttendanceStudentLeave();
        attendanceStudentLeave.setClassInfo(attendanceLeaveDTO.getClassInfo());
        attendanceStudentLeave.setClassId(attendanceLeaveDTO.getClassId());
        attendanceStudentLeave.setStudentName(attendanceLeaveDTO.getStudentName());
        attendanceStudentLeave.setStudentId(attendanceLeaveDTO.getStudentId());
        attendanceStudentLeave.setLeaveStart(attendanceLeaveDTO.getLeaveStart());
        attendanceStudentLeave.setLeaveEnd(attendanceLeaveDTO.getLeaveEnd());
        attendanceStudentLeave.setCauseLeave(attendanceLeaveDTO.getCauseLeave());
        attendanceStudentLeave.setTermId(term);
        attendanceStudentLeave.setRegisterPerson(additionalInfo.getName());
        attendanceStudentLeave.setRegisterId(additionalInfo.getId());
        //生成创建时间
        attendanceStudentLeave.setCreateTime(LocalDateTime.now());
        //生成删除标记状态(0显示，1隐藏)
        attendanceStudentLeave.setDelFlag(0L);
        //生成请假状态
        attendanceStudentLeave.setLeaveState(0L);
        //获取学校ID
        attendanceStudentLeave.setCampusId(loginUser.getSchoolId());
        return attendanceStudentLeave;
    }
}
