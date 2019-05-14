package com.lxzh.smart.campus.attendance.modular.service.impl;

import cn.stylefeng.roses.system.api.SchoolService;
import cn.stylefeng.roses.system.api.context.AdditionalInfo;
import cn.stylefeng.roses.system.api.context.LoginContext;
import cn.stylefeng.roses.system.api.context.LoginUser;
import cn.stylefeng.roses.system.api.model.SchoolCampusInfo;
import cn.stylefeng.roses.system.api.model.SchoolInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lxzh.smart.campus.api.model.TeacherInfoVo;
import com.lxzh.smart.campus.attendance.modular.consumer.TeacherInfoConsumer;
import com.lxzh.smart.campus.attendance.modular.dto.AttendanceStudentRecordDTO;
import com.lxzh.smart.campus.attendance.modular.dto.AttendanceTeacherRecordDTO;
import com.lxzh.smart.campus.attendance.modular.entity.AttendanceStudentRecord;
import com.lxzh.smart.campus.attendance.modular.entity.AttendanceTeacherRecord;
import com.lxzh.smart.campus.attendance.modular.mapper.AttendanceTeacherHistoryMapper;
import com.lxzh.smart.campus.attendance.modular.mapper.AttendanceTeacherRecordMapper;
import com.lxzh.smart.campus.attendance.modular.service.AttendanceTeacherRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxzh.smart.campus.attendance.modular.util.AttendanceServiceUtil;
import com.lxzh.smart.campus.attendance.modular.util.LocalDateTimeUtil;
import com.lxzh.smart.campus.attendance.modular.vo.ClockInVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zyj
 * @since 2019-05-13
 */
@Service
public class AttendanceTeacherServiceImpl extends ServiceImpl<AttendanceTeacherRecordMapper, AttendanceTeacherRecord> implements AttendanceTeacherRecordService {

    @Autowired
    private AttendanceTeacherRecordMapper attendanceTeacherRecordMapper;

    @Autowired
    private AttendanceTeacherHistoryMapper attendanceTeacherHistoryMapper;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private TeacherInfoConsumer teacherInfoConsumer;

    /**
     * 1.每天早晨1点执行（备份当天所有学校教师信息存储到学生考勤基本情况表中）
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void teacherInfoBackup() {
        System.out.println("每天早晨1点执行：============>>>>>>>" + LocalDateTime.now() + "<<<<<<=============数据存储");
        if (LocalDateTimeUtil.sveralWeeks(LocalDateTime.now()) <= 5) {
            //通过所有校区获取所有教师信息存储到数据库
            schoolSelect();
        }
    }

    /**
     * 根据学校查询所有校区
     */
    private void schoolSelect() {
        List<SchoolInfo> schoolList = schoolService.getSchoolList();
        if (!CollectionUtils.isEmpty(schoolList)) {
            for (SchoolInfo school : schoolList) {
                campusSelect(school);
            }
        }
    }

    /**
     * 根据校区查询所有教师
     *
     * @param school
     */
    private void campusSelect(SchoolInfo school) {
        if (!CollectionUtils.isEmpty(school.getCampusList())) {
            for (SchoolCampusInfo campus : school.getCampusList()) {
                teacherSelect(campus);
            }
        }
    }

    /**
     * 将查询所有的教师信息初始化存储到数据库
     * @param campus
     */
    private void teacherSelect(SchoolCampusInfo campus) {
        List<TeacherInfoVo> teacherInfo = teacherInfoConsumer.getTeacherInfoByCampus(campus.getId());
        if (!CollectionUtils.isEmpty(teacherInfo)) {
            for (TeacherInfoVo teacher : teacherInfo) {
                attendanceTeacherRecordMapper.insert(AttendanceTeacherRecordDTO.convertData(teacher));
            }
        }
    }

    /**
     * 2.教师考勤打卡
     */
    public Integer teacherClockIn(ClockInVO clockInVO) {

        //教师打卡设备跟校区关联(判断是否是该校区教师)
        Long schoolCampusId = teacherInfoConsumer.getTeacherInfo(clockInVO.getNumber()).getSchoolCampusId();
        //将教师打卡存储到历史记录表中
        attendanceTeacherHistoryMapper.insert(ClockInVO.historyTransformTeacher(clockInVO));
        //判断教师是周几打卡
        if (LocalDateTimeUtil.sveralWeeks(clockInVO.getClockInTime()) <= 5) {
            QueryWrapper<AttendanceTeacherRecord> queryWrapper = new QueryWrapper<AttendanceTeacherRecord>()
                    .eq("student_number", clockInVO.getNumber())
                    .between("create_time", LocalDateTime.of(LocalDate.now(), LocalTime.MIN), LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
            AttendanceTeacherRecord record = attendanceTeacherRecordMapper.selectOne(queryWrapper);
            if (record != null) {
                //查找历史记录获取第一次打卡和最后一次打卡的时间
                Map<String, LocalDateTime> map = AttendanceServiceUtil.startEndTime(clockInVO.getNumber(), attendanceTeacherHistoryMapper);
                //判断打卡教师打卡的状态
                String type = AttendanceServiceUtil.attendanceTeacherType(clockInVO.getNumber(), attendanceTeacherHistoryMapper);
                //数据进行转换
                AttendanceTeacherRecord updateRecord = AttendanceTeacherRecordDTO.updateData(record, map, type);
               return attendanceTeacherRecordMapper.update( updateRecord, queryWrapper);
            }
        }
        return 0;
    }

    /**
     *  教师考勤数据展示
     */
    public Map<String, Map> attendanceDataShow(LocalDateTime localDateTime ,Integer id) {
        Map<String, Map> map = new HashMap();
        if(loginState() != null){
            if (id == 1) {

            }else {

            }
        }
        return map;
    }

    /**
     * 登录状态
     * @return
     */
    public AdditionalInfo loginState(){
        LoginUser loginUser  = LoginContext.me().getLoginUser();
        if (loginUser != null) {
            if (loginUser.isTeacher()) {
                return loginUser.getAdditionalInfo();
            }
        }
        return null;
    }

    
}
