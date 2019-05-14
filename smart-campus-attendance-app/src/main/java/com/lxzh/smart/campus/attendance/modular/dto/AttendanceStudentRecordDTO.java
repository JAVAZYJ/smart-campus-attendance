package com.lxzh.smart.campus.attendance.modular.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.lxzh.smart.campus.attendance.modular.entity.AttendanceStudentRecord;
import com.lxzh.smart.campus.student.api.model.StudentBasicInfo;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


/**
 * @author zyj
 */
@Data
public class AttendanceStudentRecordDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    private Long id;

    /**
     * 学生编号
     */
    private Long studentNumber;

    /**
     * 班级信息
     */
    private Long gradeId;

    /**
     * 班级信息
     */
    private Long classId;

    /**
     * 学期信息
     */
    private Long termId;

    /**
     * 开始打卡时间
     */
    private LocalDateTime clockStartTime;

    /**
     * 最后打卡时间
     */
    private LocalDateTime clockEndTime;

    /**
     * 0.迟到；1.早退；2.缺勤
     */
    private Long type;

    /**
     * 校区ID
     */
    private Long campusId;

    /**
     * 学校ID
     */
    private Long schoolId;

    /**
     * 数据转换
     */
    public static AttendanceStudentRecord convertData(StudentBasicInfo studentBasicInfo,Long termId){
        AttendanceStudentRecord attendanceStudentRecord = new AttendanceStudentRecord();
        attendanceStudentRecord.setStudentNumber(studentBasicInfo.getId());
        attendanceStudentRecord.setCampusId(studentBasicInfo.getSchoolCampusId());
        attendanceStudentRecord.setGradeId(studentBasicInfo.getGradeId());
        attendanceStudentRecord.setClassId(studentBasicInfo.getClassId());
        attendanceStudentRecord.setTermId(termId);
        attendanceStudentRecord.setType(3L);
        attendanceStudentRecord.setSchoolId(studentBasicInfo.getSchoolId());
        attendanceStudentRecord.setCreateTime(LocalDateTime.now());
        return attendanceStudentRecord;
    }

    /**
     * 更新数据
     */
    public static AttendanceStudentRecord updateData(AttendanceStudentRecord attendanceStudentRecord,Map<String,LocalDateTime> startEndTime, String type,Long gradeId){
        AttendanceStudentRecord studentRecord = new AttendanceStudentRecord();
        studentRecord.setStudentNumber(attendanceStudentRecord.getStudentNumber());
        studentRecord.setCampusId(attendanceStudentRecord.getCampusId());
        studentRecord.setGradeId(gradeId);
        studentRecord.setClassId(attendanceStudentRecord.getClassId());
        studentRecord.setTermId(attendanceStudentRecord.getTermId());
        studentRecord.setClockStartTime(startEndTime.get("min"));
        studentRecord.setClockEndTime(startEndTime.get("max"));
        //存储考勤类型(0.迟到latecomer；1.早退early；2.缺勤absence)
        if(type.equals("latecomer")){
            studentRecord.setType(0L);
        }else if(type.equals("early")){
            studentRecord.setType(1L);
        }else if(type.equals("absence")){
            studentRecord.setType(2L);
        }
        studentRecord.setSchoolId(attendanceStudentRecord.getSchoolId());
        studentRecord.setCreateTime(LocalDateTime.now());
        return studentRecord;
    }
}