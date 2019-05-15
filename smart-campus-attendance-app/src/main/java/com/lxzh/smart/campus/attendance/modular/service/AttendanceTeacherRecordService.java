package com.lxzh.smart.campus.attendance.modular.service;

import cn.stylefeng.roses.system.api.model.SchoolCampusInfo;
import com.lxzh.smart.campus.attendance.modular.entity.AttendanceTeacherRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lxzh.smart.campus.attendance.modular.vo.ClockInVO;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyj
 * @since 2019-05-13
 */
public interface AttendanceTeacherRecordService extends IService<AttendanceTeacherRecord> {

    /**
     * 1.教师考勤打卡
     */
    Integer teacherClockIn(ClockInVO clockInVO);

    /**
     * 2.教师考勤数据展示
     */
    Map<String, Map> attendanceDataShow(LocalDateTime localDateTime);

}
