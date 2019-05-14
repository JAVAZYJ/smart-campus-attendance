package com.lxzh.smart.campus.attendance.modular.dto;

import com.lxzh.smart.campus.api.model.TeacherInfoVo;
import com.lxzh.smart.campus.attendance.modular.entity.AttendanceTeacherRecord;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class AttendanceTeacherRecordDTO  implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 序号
     */
    private Long id;

    /**
     * 教师编号
     */
    private Long teacherNumber;

    /**
     * 第一次打卡时间
     */
    private LocalDateTime clockStartTime;

    /**
     * 最后一次打卡时间
     */
    private LocalDateTime clockEndTime;

    /**
     * 传入数据库生成时间
     */
    private LocalDateTime createTime;

    /**
     * 设备ID
     */
    private Long equipmentId;

    /**
     * 0.迟到；1.早退；2.缺勤；3.默认缺勤
     */
    private Long type;

    /**
     * 校区ID
     */
    private Long campusId;

    /**
     * 教师备份初始化数据转换
     */
    public static AttendanceTeacherRecord convertData(TeacherInfoVo teacher){
        AttendanceTeacherRecord attendanceTeacherRecord = new AttendanceTeacherRecord();
        attendanceTeacherRecord.setTeacherNumber(teacher.getTeacherId());
        attendanceTeacherRecord.setCampusId(teacher.getSchoolCampusId());
        attendanceTeacherRecord.setCreateTime(LocalDateTime.now());
        attendanceTeacherRecord.setType(3L);
        return attendanceTeacherRecord;
    }

    /**
     * 更新数据
     */
    public static AttendanceTeacherRecord updateData(AttendanceTeacherRecord attendanceTeacherRecord, Map<String,LocalDateTime> startEndTime, String type){
        AttendanceTeacherRecord teacherRecord = new AttendanceTeacherRecord();
        teacherRecord.setTeacherNumber(attendanceTeacherRecord.getTeacherNumber());
        teacherRecord.setCampusId(attendanceTeacherRecord.getCampusId());
        teacherRecord.setClockStartTime(startEndTime.get("min"));
        teacherRecord.setClockEndTime(startEndTime.get("max"));
        if(type.equals("absence")){
            teacherRecord.setType(0L);
        }else if(type.equals("latecomer")){
            teacherRecord.setType(1L);
        }
        teacherRecord.setCreateTime(LocalDateTime.now());
        return teacherRecord;
    }
}
