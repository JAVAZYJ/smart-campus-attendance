package com.lxzh.smart.campus.attendance.modular.service.impl;

import cn.stylefeng.roses.system.api.SchoolService;
import cn.stylefeng.roses.system.api.context.AdditionalInfo;
import cn.stylefeng.roses.system.api.context.LoginContext;
import cn.stylefeng.roses.system.api.context.LoginUser;
import cn.stylefeng.roses.system.api.model.SchoolCampusInfo;
import cn.stylefeng.roses.system.api.model.SchoolInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lxzh.smart.campus.api.model.TeacherInfoVo;
import com.lxzh.smart.campus.attendance.modular.consumer.ClassInfoConsumer;
import com.lxzh.smart.campus.attendance.modular.consumer.GradeClassConsumer;
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
import com.lxzh.smart.campus.basicinfo.api.model.ClassRoomProvide;
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

    @Autowired
    private ClassInfoConsumer classInfoConsumer;

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
     * 针对每天打一次卡判断的定时器(视为缺勤)
     */
    @Scheduled(cron="0 * 23 * * ?")
    public void teacherLeaveCard() {
        System.out.println("每天下班11点执行：============>>>>>>>" + LocalDateTime.now() + "<<<<<<=============更新缺勤信息");
        if (LocalDateTimeUtil.sveralWeeks(LocalDateTime.now()) <= 5) {
            List<AttendanceTeacherRecord> lists = attendanceTeacherRecordMapper.selectList(new QueryWrapper<AttendanceTeacherRecord>()
                    .between("create_time", LocalDateTime.of(LocalDate.now(), LocalTime.MIN), LocalDateTime.of(LocalDate.now(), LocalTime.MAX)));
            for (AttendanceTeacherRecord list : lists){
                if(list.getClockStartTime().isEqual(list.getClockEndTime())){
                    list.setType(2L);
                    attendanceTeacherRecordMapper.update(list,new QueryWrapper<AttendanceTeacherRecord>().eq("teacher_number",list.getTeacherNumber()));
                }
            }
        }
    }

    /**
     * 根据学校查询所有校区
     */
    public void schoolSelect() {
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
    public void campusSelect(SchoolInfo school) {
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
    public void teacherSelect(SchoolCampusInfo campus) {
        List<TeacherInfoVo> teacherInfo = teacherInfoConsumer.getTeacherInfoByCampus(campus.getId());
        if (!CollectionUtils.isEmpty(teacherInfo)) {
            for (TeacherInfoVo teacher : teacherInfo) {
                attendanceTeacherRecordMapper.insert(AttendanceTeacherRecordDTO.convertData(teacher));
            }
        }
    }

    /**
     * 1.教师考勤打卡
     */
    public Integer teacherClockIn(ClockInVO clockInVO) {
        Long campusId  = classInfoConsumer.getCampusIdByEquipmentId(clockInVO.getEquipmentId()).getCampusId();
        //教师打卡设备跟校区关联(判断是否是该校区教师)
        Long schoolCampusId = teacherInfoConsumer.getTeacherInfo(clockInVO.getNumber()).getSchoolCampusId();
        //将教师打卡存储到历史记录表中
        if(campusId == schoolCampusId){
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
        }
        return 0;
    }

    /**
     * 2.教师考勤数据展示
     */
    public Map<String, Map> attendanceDataShow(LocalDateTime localDateTime) {
        Map<String, Map> map = new HashMap();
        Map map1 = new HashMap();
        Map map2 = new HashMap();
        if(loginState() != null){
            //获取0.迟到；1.早退；2.缺勤；3.默认缺勤的数据
            List lateList = attendanceTeacherType(0L);
            List tardyList = attendanceTeacherType(1L);
            List absenceList = attendanceTeacherTypes(2L,3L);
            Integer lateCount = attendanceTeacherTypeCount(0L);
            Integer tardyCount = attendanceTeacherTypeCount(1L);
            Integer absenceCount = attendanceTeacherTypeCounts(2L,3L);
            map1.put("lateList", lateList);
            map2.put("lateCount", lateCount);
            map1.put("tardyList", tardyList);
            map2.put("tardyCount", tardyCount);
            map1.put("absenceList", absenceList);
            map2.put("absenceCount", absenceCount);
            map.put("考勤数据",map1);
            map.put("考勤统计",map2);
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

    /**
     * 根据校区和教师考勤状态获取数据集合
     */
    public List attendanceTeacherType(Long type){
        List<AttendanceTeacherRecord> teacherRecords = attendanceTeacherRecordMapper.selectList(
                new QueryWrapper<AttendanceTeacherRecord>()
                        .eq("campus_id", LoginContext.me().getLoginUser().getCampusId())
                        .eq("type", type));
        return teacherRecords;
    }

    /**
     * 根据校区和教师缺勤状态获取数据集合
     */
    public List attendanceTeacherTypes(Long var1, Long var2){
        List<AttendanceTeacherRecord> teacherRecords = attendanceTeacherRecordMapper.selectList(
                new QueryWrapper<AttendanceTeacherRecord>()
                        .eq("campus_id", LoginContext.me().getLoginUser().getCampusId())
                        .between("type", var1,var2));
        return teacherRecords;
    }

    /**
     * 根据校区和教师考勤状态获取数据统计
     */
    public Integer attendanceTeacherTypeCount(Long type){
        Integer integer = attendanceTeacherRecordMapper.selectCount(
                new QueryWrapper<AttendanceTeacherRecord>()
                        .eq("campus_id", LoginContext.me().getLoginUser().getCampusId())
                        .eq("type", type));
        return integer;
    }

    /**
     * 根据校区和教师考勤状态获取数据统计
     */
    public Integer attendanceTeacherTypeCounts(Long var1, Long var2){
        Integer integer = attendanceTeacherRecordMapper.selectCount(
                new QueryWrapper<AttendanceTeacherRecord>()
                        .eq("campus_id", LoginContext.me().getLoginUser().getCampusId())
                        .between("type", var1,var2));
        return integer;
    }
}
