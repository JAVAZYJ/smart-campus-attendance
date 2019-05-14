package com.lxzh.smart.campus.attendance.modular.service;

import com.lxzh.smart.campus.attendance.modular.dto.AttendanceLeaveDTO;
import com.lxzh.smart.campus.attendance.modular.dto.LeaveDeleteDTO;
import com.lxzh.smart.campus.attendance.modular.entity.AttendanceStudentLeave;
import com.lxzh.smart.campus.attendance.modular.entity.AttendanceStudentRecord;
import com.lxzh.smart.campus.attendance.modular.vo.ClockInVO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  学生考勤打卡服务类
 * </p>
 *
 * @author zyj
 * @since 2019-04-28
 */
public interface AttendanceStudentService {

    /**
     * 学生打卡
     * @param clockInDTO
     * @return
     */
    Integer studentClockIn(ClockInVO clockInDTO);

    /**
     * 3.显示学生考勤数据
     * @param localDateTime
     * @param id
     * @return List
     */
    Map<String, Map> attendanceDataShow(LocalDateTime localDateTime , Integer id);

    /**
     * 添加学生请假信息
     * @param attendanceLeaveDTO
     * @return
     */
    Integer leave(AttendanceLeaveDTO attendanceLeaveDTO);

    /**
     * 查询学生请假信息
     * @param attendanceLeaveDTO
     * @return
     */
    List<AttendanceStudentLeave> studentLeaveSelect(AttendanceLeaveDTO attendanceLeaveDTO);

    /**
     * 删除学生请假记录
     * @param leaveDeleteDTO
     * @return
     */
    Integer leaveDelete(LeaveDeleteDTO leaveDeleteDTO);
}
