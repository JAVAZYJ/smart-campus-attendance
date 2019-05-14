package com.lxzh.smart.campus.attendance.modular.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lxzh.smart.campus.attendance.modular.entity.AttendanceStudentHistory;
import com.lxzh.smart.campus.attendance.modular.entity.AttendanceTeacherHistory;
import com.lxzh.smart.campus.attendance.modular.mapper.AttendanceStudentHistoryMapper;
import com.lxzh.smart.campus.attendance.modular.mapper.AttendanceTeacherHistoryMapper;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * 学生考勤工具类
 */
public class AttendanceServiceUtil {

    /**
     * 计算当天学生考勤第一次的时间和最后一次的时间
     */
    public static Map<String, LocalDateTime> startEndTime( Long number, AttendanceStudentHistoryMapper historyMapper){
        Map<String,LocalDateTime> map = new HashMap<>(2);
        List<AttendanceStudentHistory> historyList = historyMapper.selectList(new QueryWrapper<AttendanceStudentHistory>()
                .eq("student_number",number)
                .ge("clock_in_time", LocalDateTime.of(LocalDate.now(), LocalTime.MIN))
                .lt("clock_in_time", LocalDateTime.of(LocalDate.now(), LocalTime.MAX)));
        List<LocalDateTime> localDateTimes = new ArrayList<>();
        for (AttendanceStudentHistory history : historyList){
            localDateTimes.add(history.getClockInTime());
        }
        //当天第一次打卡的时间
        LocalDateTime min = Collections.min(localDateTimes);
        //最后一次打卡的时间
        LocalDateTime max = Collections.max(localDateTimes);
        map.put("min",min);
        map.put("max",max);
        return map;
    }

    /**
     * 计算当天教师考勤第一次的时间和最后一次的时间
     */
    public static Map<String, LocalDateTime> startEndTime(Long number, AttendanceTeacherHistoryMapper historyMapper){
        Map<String,LocalDateTime> map = new HashMap<>(2);
        List<AttendanceTeacherHistory> historyList = historyMapper.selectList(new QueryWrapper<AttendanceTeacherHistory>()
                .eq("teacher_number",number)
                .ge("clock_in_time", LocalDateTime.of(LocalDate.now(), LocalTime.MIN))
                .lt("clock_in_time", LocalDateTime.of(LocalDate.now(), LocalTime.MAX)));
        List<LocalDateTime> localDateTimes = new ArrayList<>();
        for (AttendanceTeacherHistory history : historyList){
            localDateTimes.add(history.getClockInTime());
        }
        LocalDateTime min = Collections.min(localDateTimes);
        LocalDateTime max = Collections.max(localDateTimes);
        map.put("min",min);
        map.put("max",max);
        return map;
    }

    /**
     * 根据学生打卡判断考勤类型(0.正常1.迟到；2.早退；3.缺勤)
     * @param number
     * @return
     */
    public static String attendanceStudentType(Long number,AttendanceStudentHistoryMapper historyMapper){
        String assignment = "";
        //存储考勤情况
        Map<String, LocalDateTime> timeMap = startEndTime(number,historyMapper);
        LocalDateTime startTime = timeMap.get("min");
        LocalDateTime endTime = timeMap.get("max");
        //迟到的计算方法(设置迟到的时间标准为：07:00)
        Integer latecomer = startTime.toString().substring(11, 16).compareTo("07:00");
        //缺勤的计算方法(设置缺勤的时间标准为：09:00)
        Integer absence = startTime.toString().substring(11, 16).compareTo("09:00");
        //如果迟到了，或者达到缺勤的时间，那统一归类为缺勤;计算方法(判断标准:-1代表没有迟到；1代表迟到；0代表准时到)
        if (latecomer > 0 && absence > 0) {
            assignment = "absence";
        } else if(latecomer > 0 && absence < 0){
            //归类为迟到
            assignment = "latecomer";
        }else if(startTime.isAfter(endTime)&& !startTime.isEqual(endTime) && endTime.toString().substring(11, 16).compareTo("18:00") < 0){
            //早退
            assignment = "early";
        }
        return assignment;
    }

    /**
     * 根据教师打卡判断考勤类型(0.正常1.迟到；2.早退；3.缺勤)
     * @param number
     * @return
     */
    public static String attendanceTeacherType(Long number,AttendanceTeacherHistoryMapper historyMapper){
        String assignment = "";
        Map<String, LocalDateTime> timeMap = startEndTime(number,historyMapper);
        //迟到的计算方法(设置迟到的时间标准为：07:00)
        Integer latecomer = timeMap.get("min").toString().substring(11, 16).compareTo("07:00");
        //缺勤的计算方法(设置缺勤的时间标准为：09:00)
        Integer absence = timeMap.get("min").toString().substring(11, 16).compareTo("09:00");
        //如果迟到了，或者达到缺勤的时间，那统一归类为缺勤;计算方法(判断标准:-1代表没有迟到；1代表迟到；0代表准时到)
        if (latecomer > 0 && absence > 0) {
            assignment = "absence";
        } else if(latecomer > 0 && absence < 0){
            //归类为迟到
            assignment = "latecomer";
        }else if(timeMap.get("min").isAfter(timeMap.get("max"))&& !timeMap.get("min").isEqual(timeMap.get("max")) && timeMap.get("max").toString().substring(11, 16).compareTo("18:00") < 0){
            //早退
            assignment = "early";
        }
        return assignment;
    }
}
