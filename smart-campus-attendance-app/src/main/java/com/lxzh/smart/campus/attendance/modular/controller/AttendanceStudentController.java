package com.lxzh.smart.campus.attendance.modular.controller;

import com.lxzh.smart.campus.attendance.modular.dto.AttendanceLeaveDTO;
import com.lxzh.smart.campus.attendance.modular.vo.ClockInVO;
import com.lxzh.smart.campus.attendance.modular.dto.LeaveDeleteDTO;
import com.lxzh.smart.campus.attendance.modular.entity.AttendanceStudentLeave;
import com.lxzh.smart.campus.attendance.modular.service.AttendanceStudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 学生考勤控制类
 * @author zyj
 * @since 2019-4-28 10:30:05
 */
@RestController
@RequestMapping("/attendance_student")
@Api(value = "/attendance_student", tags = {"学生考勤管理"})
public class AttendanceStudentController {

    @Autowired
    private AttendanceStudentService attendanceStudentService;

    /**
     * 学生考勤打卡
     */
    @PostMapping("/clock_in")
    @ApiOperation(value = "学生考勤打卡", notes = "学生考勤打卡")
    public Integer attendance(@RequestBody ClockInVO clockInDTO){
        clockInDTO.setNumber(1125210159732473857L);
        clockInDTO.setClockInTime(LocalDateTime.now());
        clockInDTO.setEquipmentId(1L);
        return attendanceStudentService.studentClockIn(clockInDTO);
    }

    /**
     * 查询全校或教师带的班级学生迟到、早退、缺勤
     */
    @PostMapping("/attendance_select")
    public Map<String, Map> selectAttendance(String localDateTime,Integer choose){
        //TODO:当前用户登录的ID获取班级信息
        return attendanceStudentService.attendanceDataShow(LocalDateTime.now(),1);
    }

    /**
     * 添加学生请假信息
     */
    @PostMapping("/student_leave")
    @ApiOperation(value = "添加学生请假信息", notes = "添加学生请假信息")
    public Integer studentLeave(@RequestBody AttendanceLeaveDTO attendanceLeaveDTO){
        attendanceLeaveDTO.setStudentId(1125210159732473857L);
        attendanceLeaveDTO.setClassId(1121573239109480450L);
        return attendanceStudentService.leave(attendanceLeaveDTO);
    }

    /**
     * 根据条件查看学生请假信息
     */
    @PostMapping("/student_leave_select")
    @ApiOperation(value = "根据条件查看学生请假信息", notes = "根据条件查看学生请假信息")
    public List<AttendanceStudentLeave> studentLeaveSelect(@RequestBody AttendanceLeaveDTO attendanceLeaveDTO){
        return attendanceStudentService.studentLeaveSelect(attendanceLeaveDTO);
    }

    /**
     * 删除学生请假记录
     */
    @PostMapping("/student_leave_delete")
    @ApiOperation(value = "删除学生请假记录", notes = "删除学生请假记录")
    public Integer leaveDelete(@RequestBody LeaveDeleteDTO leaveDeleteDTO){
        leaveDeleteDTO.setStudentId(1125210159732473857L);
        return attendanceStudentService.leaveDelete(leaveDeleteDTO);
    }
}
