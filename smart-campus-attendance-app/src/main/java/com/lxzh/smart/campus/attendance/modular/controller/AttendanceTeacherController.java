package com.lxzh.smart.campus.attendance.modular.controller;

import com.lxzh.smart.campus.attendance.modular.entity.AttendanceTeacherRecord;
import com.lxzh.smart.campus.attendance.modular.service.AttendanceTeacherRecordService;
import com.lxzh.smart.campus.attendance.modular.vo.ClockInVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 教师考勤控制类
 * @author zyj
 * @since 2019-5-15 10:30:05
 */
@RestController
@RequestMapping("/attendance_student")
@Api(value = "/attendance_student", tags = {"教师考勤管理"})
public class AttendanceTeacherController {

    @Autowired
    private AttendanceTeacherRecordService attendanceTeacherRecordService;

    /**
     * 教师考勤打卡
     */
    @PostMapping("/clock_in")
    @ApiOperation(value = "教师考勤打卡", notes = "教师考勤打卡")
    public Integer attendance(@RequestBody ClockInVO clockInDTO){
        clockInDTO.setNumber(1125210159732473857L);
        clockInDTO.setClockInTime(LocalDateTime.now());
        clockInDTO.setEquipmentId(1L);
        return attendanceTeacherRecordService.teacherClockIn(clockInDTO);
    }

}
