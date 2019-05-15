package com.lxzh.smart.campus.attendance.modular.service.impl;

import cn.stylefeng.roses.system.api.SchoolService;
import cn.stylefeng.roses.system.api.context.AdditionalInfo;
import cn.stylefeng.roses.system.api.context.LoginContext;
import cn.stylefeng.roses.system.api.context.LoginUser;
import cn.stylefeng.roses.system.api.model.SchoolCampusInfo;
import cn.stylefeng.roses.system.api.model.SchoolInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lxzh.smart.campus.attendance.modular.consumer.*;
import com.lxzh.smart.campus.attendance.modular.dto.AttendanceLeaveDTO;
import com.lxzh.smart.campus.attendance.modular.dto.AttendanceStudentRecordDTO;
import com.lxzh.smart.campus.attendance.modular.entity.*;
import com.lxzh.smart.campus.attendance.modular.util.AttendanceServiceUtil;
import com.lxzh.smart.campus.attendance.modular.vo.ClockInVO;
import com.lxzh.smart.campus.attendance.modular.dto.LeaveDeleteDTO;
import com.lxzh.smart.campus.attendance.modular.mapper.*;
import com.lxzh.smart.campus.attendance.modular.service.AttendanceStudentService;
import com.lxzh.smart.campus.attendance.modular.util.LocalDateTimeUtil;
import com.lxzh.smart.campus.basicinfo.api.model.ClassInfoProvide;
import com.lxzh.smart.campus.basicinfo.api.model.ClassProvide;
import com.lxzh.smart.campus.basicinfo.api.model.GradeProvide;
import com.lxzh.smart.campus.basicinfo.api.model.TeacherClassProvide;
import com.lxzh.smart.campus.student.api.model.StudentBasicInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * <p>
 * 学生考勤打卡基本信息 服务实现类
 * </p>
 *
 * @author zyj
 * @since 2019-04-28
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
public class AttendanceStudentServiceImpl implements AttendanceStudentService {

    @Autowired
    private AttendanceStudentHistoryMapper historyMapper;

    @Autowired
    private AttendanceStudentRecordMapper recordMapper;

    @Autowired
    private AttendanceStudentCountMapper countMapper;

    @Autowired
    private AttendanceStudentLeaveMapper leaveMapper;

    @Autowired
    private StudentConsumer studentConsumer;

    @Autowired
    private TermInfoConsumer termInfoConsumer;

    @Autowired
    private GradeClassConsumer gradeClassConsumer;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private TeacherClassConsumer teacherClassConsumer;

    @Autowired
    private ClassInfoConsumer classInfoConsumer;

    /**
     * 1.每天早晨1点执行（备份当天所有学校学生信息存储到学生考勤基本情况表中）
     */
    @Scheduled(cron = "0 0 1 * * ?")
    //@Scheduled(cron = "0 * * * * ?")
    public void studentInfoBackup() {
        System.out.println("每天早晨1点执行：============>>>>>>>" + LocalDateTime.now() + "<<<<<<=============数据存储");
        if (LocalDateTimeUtil.sveralWeeks(LocalDateTime.now()) <= 5) {
            //根据校区获取所有年级。通过年级获取所有班级。通过所有班级获取所有学生信息(必须的字段有:学生编号、学生姓名、年级、班级、学期、校区)。
            List<SchoolInfo> schoolList = schoolService.getSchoolList();
            school(schoolList);
        }
    }

    /**
     * 针对每天打一次卡判断的定时器(视为缺勤)
     */
    @Scheduled(cron="0 * 23 * * ?")
    public void studentLeaveCard() {
        System.out.println("每天放学11点执行：============>>>>>>>" + LocalDateTime.now() + "<<<<<<=============更新缺勤信息");
        if (LocalDateTimeUtil.sveralWeeks(LocalDateTime.now()) <= 5) {
            List<AttendanceStudentRecord> lists = recordMapper.selectList(new QueryWrapper<AttendanceStudentRecord>()
                    .between("create_time", LocalDateTime.of(LocalDate.now(), LocalTime.MIN), LocalDateTime.of(LocalDate.now(), LocalTime.MAX)));
            for (AttendanceStudentRecord list : lists){
                if(list.getClockStartTime().isEqual(list.getClockEndTime())){
                    list.setType(2L);
                    recordMapper.update(list,new QueryWrapper<AttendanceStudentRecord>().eq("student_number",list.getStudentNumber()));
                }
            }
        }
    }

    /**
     * 学校查询校区
     * @param schoolList
     */
    private void school(List<SchoolInfo> schoolList) {
        if (!CollectionUtils.isEmpty(schoolList)) {
            for (SchoolInfo school : schoolList) {
                campus(school);
            }
        }
    }

    /**
     * 校区查询年级
     * @param school
     */
    private void campus(SchoolInfo school) {
        if(!CollectionUtils.isEmpty(school.getCampusList())){
            for (SchoolCampusInfo campus : school.getCampusList()){
                grade(campus);
            }
        }
    }

    /**
     * 年级查询班级
     * @param campus
     */
    private void grade(SchoolCampusInfo campus) {
        List<GradeProvide> gradeList = gradeClassConsumer.listByCampusId(campus.getId());
        if(!CollectionUtils.isEmpty(gradeList)){
            for (GradeProvide grade : gradeList){
                classes(campus, grade);
            }
        }
    }

    /**
     * 班级查询学生
     * @param campus
     * @param grade
     */
    private void classes(SchoolCampusInfo campus, GradeProvide grade) {
        List<ClassProvide> classList = gradeClassConsumer.classesListByGradeId(grade.getId());
        if (!CollectionUtils.isEmpty(classList)) {
            for (ClassProvide classes : classList) {
                System.out.println("第四阶段班级查询============>>>>>>>" + classes.getId() + "<<<<<<=============" + campus.getCampusName());
                student(campus, classes);
            }
        }
    }

    /**
     * 将学生信息初始化数据库中
     * @param campus
     * @param classes
     */
    private void student(SchoolCampusInfo campus, ClassProvide classes) {
        List<StudentBasicInfo> studentList = studentConsumer.getStudentsByClassId(classes.getId(), termInfoConsumer.getNowTerm(null).getId(), null);
        if (!CollectionUtils.isEmpty(studentList)) {
            for (StudentBasicInfo student : studentList){
                recordMapper.insert(AttendanceStudentRecordDTO.convertData(student, termInfoConsumer.getNowTerm(null).getId()));
            }
        }
    }

    /**
     * 2.学生考勤打卡（设备根据教室关联，教室跟班级关联）
     */
    @Override
    public Integer studentClockIn(ClockInVO clockInVO) {
        //TODO：通过打卡设备ID查询对应的班级ID和教室ID
        ClassInfoProvide classInfo = classInfoConsumer.getClassIdByEquipmentId(termInfoConsumer.getNowTerm(null).getId(), clockInVO.getEquipmentId());
        if(classInfo != null){
            //TODO：通过打卡的学生编号获取当前学期该学生信息
            StudentBasicInfo studentInfo = studentConsumer.getStudentById(clockInVO.getNumber(), termInfoConsumer.getNowTerm(null).getId());
            if(studentInfo != null){
                if(classInfo.getClassId().equals(studentInfo.getClassId())){
                    //1.将学生打卡存入学生打卡历史记录表中
                    historyMapper.insert(ClockInVO.historyTransformStudent(clockInVO));
                    //判断学生是周几打卡
                    if (LocalDateTimeUtil.sveralWeeks(clockInVO.getClockInTime()) <= 5) {
                        //2.将学生打卡存入学生考勤基本情况记录表中
                        QueryWrapper<AttendanceStudentRecord> queryWrapper = new QueryWrapper<AttendanceStudentRecord>()
                                .eq("student_number", clockInVO.getNumber())
                                .between("create_time", LocalDateTime.of(LocalDate.now(), LocalTime.MIN),LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
                        AttendanceStudentRecord record = recordMapper.selectOne(queryWrapper);
                        if (record != null) {
                            //查找历史记录获取第一次打卡和最后一次打卡的时间
                            Map<String, LocalDateTime> map = AttendanceServiceUtil.startEndTime(clockInVO.getNumber(), historyMapper);
                            //判断打卡学生打卡的状态
                            String type = AttendanceServiceUtil.attendanceStudentType(clockInVO.getNumber(),historyMapper);
                            //数据进行转换

                            AttendanceStudentRecord updateRecord = AttendanceStudentRecordDTO.updateData(record, map ,type,studentInfo.getGradeId());
                            return recordMapper.update(updateRecord, queryWrapper);
                        }
                    }
                }
            }
        }
        return 0;
    }

    /**
     * 3.显示学生考勤数据
     */
    @Override
    public Map<String, Map> attendanceDataShow(LocalDateTime localDateTime ,Integer id) {
        Map<String, Map> map = new HashMap();
        if(loginState() != null){
            //根据选择全校或班级进行显示
            if (id == 1) {
                map.put("全校",attendanceSchool());
            }else {
                map.put("班级",attendanceStudent());
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

    /**
     * 如果请假则不算缺勤
     */
    public List<AttendanceStudentLeave> leaves(){
        List<AttendanceStudentLeave> leaves = leaveMapper.selectList(new QueryWrapper<AttendanceStudentLeave>()
                .eq("del_flag", 0L)
                .eq("term_id",termInfoConsumer.getNowTerm(null).getId())
                .ge("leave_start", LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusHours(8))
                .le("leave_end", LocalDateTime.of(LocalDate.now(), LocalTime.MAX).minusHours(18)));
        return leaves;
    }

    /**
     * 根据登录用户对应的学校ID显示学生考勤数据
     */
    public Map attendanceSchool(){
        Map<String, List<AttendanceStudentRecord>> map1 = new HashMap();
        Map<String, Integer> map2 = new HashMap();
        Map<String, List> map = new HashMap();
        Map registerMap = new HashMap();
        List list = new ArrayList();
        List<AttendanceStudentRecord> lateList = attendanceStudentSchoolType(0L);
        List<AttendanceStudentRecord> tardyList = attendanceStudentSchoolType(1L);
        List<AttendanceStudentRecord> absenceList = attendanceStudentSchoolTypes(2L, 3L);
        Integer lateCount = attendanceStudentSchoolCount(0L);
        Integer tardyCount  = attendanceStudentSchoolCount(1L);
        Integer absenceCount = attendanceStudentSchoolCounts(2L,3L);
        map1.put("lateList", lateList);
        map2.put("lateCount", lateCount);
        map1.put("tardyList", tardyList);
        map2.put("tardyCount", tardyCount);
        List<AttendanceStudentRecord> absenceList1 = new ArrayList<>();
        boolean leave = false;
        if(!CollectionUtils.isEmpty(leaves())){
            leave = true;
        }
        if(leave){
            List registerList = new ArrayList();
            for (AttendanceStudentLeave lea : leaves()){
                for(AttendanceStudentRecord absence : absenceList){
                    if(lea.getStudentId().longValue() == absence.getStudentNumber().longValue()){
                        registerList.add(lea);
                        continue;
                    }
                    absenceList1.add(absence);
                }
            }
            registerMap.put("registerList",registerList);
            registerMap.put("registerCount",registerList.size());
        }
        map1.put("absenceList", absenceList1);
        map2.put("absenceCount", absenceCount);
        list.add(map1);
        list.add(map2);
        list.add(registerMap);
        map.put("学校",list);
        return map;
    }

    /**
     * 根据登录用户对应的班级ID显示学生考勤数据
     */
    public Map attendanceStudent(){
        Map<String, List<AttendanceStudentRecord>> map1 = new HashMap();
        Map<String, Integer> map2 = new HashMap();
        Map<String, List> map3 = new HashMap();
        //获取登录用户编号
        Long teacherId = loginState().getId();
        //查询用户当前学期的班级
        List<TeacherClassProvide> classList = teacherClassConsumer.getClass(teacherId,termInfoConsumer.getNowTerm(null).getId());
        if (!CollectionUtils.isEmpty(classList)) {
            for (TeacherClassProvide classes : classList) {
                List list = new ArrayList();
                Map registerMap = new HashMap();
                List<AttendanceStudentRecord> lateList = attendanceStudentClassType(classes.getClassId(),0L);
                List<AttendanceStudentRecord> tardyList =  attendanceStudentClassType(classes.getClassId(),1L);
                List<AttendanceStudentRecord> absenceList = attendanceStudentClassTypes(classes.getClassId(),2L,3L);
                Integer lateCount = attendanceStudentClassCount(classes.getClassId(),0L);
                Integer tardyCount = attendanceStudentClassCount(classes.getClassId(),1L);
                Integer absenceCount =  attendanceStudentClassCounts(classes.getClassId(),2L,3L);
                map1.put("lateList", lateList);
                map1.put("tardyList", tardyList);
                map2.put("lateCount", lateCount);
                map2.put("tardyCount", tardyCount);
                List<AttendanceStudentRecord> absenceList1 = new ArrayList<>();
                boolean leave = false;
                if(!CollectionUtils.isEmpty(leaves())){
                    leave = true;
                }
                if(leave == true){
                    List registerList = new ArrayList();
                    for (AttendanceStudentLeave lea : leaves()){
                        for(AttendanceStudentRecord absence : absenceList){
                            if(lea.getStudentId().equals(absence.getStudentNumber())){
                                registerList.add(lea);
                                continue;
                            }
                            absenceList1.add(absence);
                        }
                    }
                    registerMap.put("registerList",registerList);
                    registerMap.put("registerCount",registerList.size());
                }
                map1.put("absenceList", absenceList1);
                map2.put("absenceCount", absenceCount);
                list.add(map1);
                list.add(map2);
                list.add(registerMap);
                map3.put(classes.getClassName(),list);
            }
        }
        return map3;
    }

    /**
     * 根据登录用户对应的学校ID获取当前学期全校学生考勤数据集合
     */
    public List attendanceStudentSchoolType(Long type){
        List<AttendanceStudentRecord> list = recordMapper.selectList(
                new QueryWrapper<AttendanceStudentRecord>()
                        .eq("school_id", LoginContext.me().getLoginUser().getSchoolId())
                        .eq("type", type)
                        .eq("term_id", termInfoConsumer.getNowTerm(null).getId())
                        .between("create_time", LocalDateTime.of(LocalDate.now(), LocalTime.MIN), LocalDateTime.of(LocalDate.now(), LocalTime.MAX)));
        return list;
    }

    /**
     * 根据登录用户对应的学校ID获取当前学期全校学生缺勤数据集合
     */
    public List attendanceStudentSchoolTypes(Long var1,Long var2){
        List<AttendanceStudentRecord> absenceList = recordMapper.selectList(
                new QueryWrapper<AttendanceStudentRecord>()
                        .eq("school_id", LoginContext.me().getLoginUser().getSchoolId())
                        .ge("type", var1).le("type",var2)
                        .eq("term_id", termInfoConsumer.getNowTerm(null).getId())
                        .between("create_time", LocalDateTime.of(LocalDate.now(), LocalTime.MIN), LocalDateTime.of(LocalDate.now(), LocalTime.MAX)));
        return absenceList;
    }

    /**
     * 根据登录用户对应的学校ID获取当前学期全校学生考勤数据统计
     */
    public Integer attendanceStudentSchoolCount(Long type){
        Integer integer = recordMapper.selectCount(
                new QueryWrapper<AttendanceStudentRecord>()
                        .eq("school_id", LoginContext.me().getLoginUser().getSchoolId())
                        .eq("type", type)
                        .eq("term_id", termInfoConsumer.getNowTerm(null).getId())
                        .between("create_time", LocalDateTime.of(LocalDate.now(), LocalTime.MIN), LocalDateTime.of(LocalDate.now(), LocalTime.MAX)));
        return integer;
    }

    /**
     * 根据登录用户对应的学校ID获取当前学期全校学生缺勤数据统计
     */
    public Integer attendanceStudentSchoolCounts(Long var1,Long var2){
        Integer integer = recordMapper.selectCount(
                new QueryWrapper<AttendanceStudentRecord>()
                        .eq("school_id", LoginContext.me().getLoginUser().getSchoolId())
                        .ge("type", var1).le("type",var2)
                        .eq("term_id", termInfoConsumer.getNowTerm(null).getId())
                        .between("create_time", LocalDateTime.of(LocalDate.now(), LocalTime.MIN), LocalDateTime.of(LocalDate.now(), LocalTime.MAX)));
        return integer;
    }

    /**
     * 根据登录用户对应的班级ID获取考勤数据的集合
     */
    public List attendanceStudentClassType(Long classesId,Long type){
        List<AttendanceStudentRecord> list = recordMapper.selectList(
                new QueryWrapper<AttendanceStudentRecord>()
                        .eq("class_id", classesId)
                        .eq("type", type)
                        .eq("term_id", termInfoConsumer.getNowTerm(null).getId())
                        .between("create_time", LocalDateTime.of(LocalDate.now(), LocalTime.MIN), LocalDateTime.of(LocalDate.now(), LocalTime.MAX)));
        return list;
    }

    /**
     * 根据登录用户对应的班级ID获取考勤数据的集合
     */
    public Integer attendanceStudentClassCount(Long classesId,Long type){
        Integer integer = recordMapper.selectCount(
                new QueryWrapper<AttendanceStudentRecord>()
                        .eq("class_id", classesId)
                        .eq("type", type)
                        .eq("term_id", termInfoConsumer.getNowTerm(null).getId())
                        .between("create_time", LocalDateTime.of(LocalDate.now(), LocalTime.MIN), LocalDateTime.of(LocalDate.now(), LocalTime.MAX)));
        return integer;
    }

    /**
     * 根据登录用户对应的班级ID获取缺勤数据的集合
     */
    public List attendanceStudentClassTypes(Long classesId,Long var1,Long var2){
        List<AttendanceStudentRecord> absenceList =
                recordMapper.selectList(
                        new QueryWrapper<AttendanceStudentRecord>()
                                .eq("class_id", classesId)
                                .ge("type", var1).le("type",var2)
                                .eq("term_id", termInfoConsumer.getNowTerm(null).getId())
                                .between("create_time", LocalDateTime.of(LocalDate.now(), LocalTime.MIN), LocalDateTime.of(LocalDate.now(), LocalTime.MAX)));

        return absenceList;
    }

    /**
     * 根据登录用户对应的班级ID获取缺勤数据的集合
     */
    public Integer attendanceStudentClassCounts(Long classesId,Long var1,Long var2){
        Integer integer =
                recordMapper.selectCount(
                        new QueryWrapper<AttendanceStudentRecord>()
                                .eq("class_id", classesId)
                                .ge("type", var1).le("type",var2)
                                .eq("term_id", termInfoConsumer.getNowTerm(null).getId())
                                .between("create_time", LocalDateTime.of(LocalDate.now(), LocalTime.MIN), LocalDateTime.of(LocalDate.now(), LocalTime.MAX)));

        return integer;
    }

    /**
     * 4.添加学生请假信息
     */
    @Override
    public Integer leave(AttendanceLeaveDTO attendanceLeaveDTO) {
        if(loginState() != null){
            //if(attendanceLeaveDTO.getLeaveStart() != null && attendanceLeaveDTO.getLeaveEnd() != null && attendanceLeaveDTO.getLeaveStart().isAfter(attendanceLeaveDTO.getLeaveEnd()) ){
                AttendanceStudentRecord StudentInfo = recordMapper.selectOne(new QueryWrapper<AttendanceStudentRecord>()
                        .eq("student_number", attendanceLeaveDTO.getStudentId())
                        .eq("term_id", termInfoConsumer.getNowTerm(null).getId())
                        .eq("class_id", attendanceLeaveDTO.getClassId())
                        .between("create_time", LocalDateTime.of(LocalDate.now(), LocalTime.MIN), LocalDateTime.of(LocalDate.now(), LocalTime.MAX)));
                if(StudentInfo != null){
                    return leaveMapper.insert(AttendanceLeaveDTO.studentLeaveTransform(attendanceLeaveDTO,loginState(),LoginContext.me().getLoginUser(),termInfoConsumer.getNowTerm(null).getId()));
                }
                return 1;
            //}
        }
        return 0;
    }

    /**
     * 5.查询学生请假信息
     */
    @Override
    public List<AttendanceStudentLeave> studentLeaveSelect(AttendanceLeaveDTO attendanceLeaveDTO) {
        if(loginState() != null){
            QueryWrapper queryWrapper = new QueryWrapper<AttendanceStudentLeave>().eq("del_flag", 0L).eq("register_person",loginState().getName()).eq("term_id",termInfoConsumer.getNowTerm(null).getId());
            if (StringUtils.isNotEmpty(attendanceLeaveDTO.getStudentName())) {
                queryWrapper.eq("student_name", attendanceLeaveDTO.getStudentName());
            }
            if (StringUtils.isNotEmpty(attendanceLeaveDTO.getClassInfo())) {
                queryWrapper.eq("class_info", attendanceLeaveDTO.getClassInfo());
            }
            if (attendanceLeaveDTO.getLeaveStart() != null) {
                queryWrapper.ge("create_time", attendanceLeaveDTO.getLeaveStart());
            }
            if (attendanceLeaveDTO.getLeaveEnd() != null) {
                queryWrapper.ge("create_time", attendanceLeaveDTO.getLeaveEnd());
            }
            queryWrapper.orderByDesc("create_time");
            return leaveMapper.selectList(queryWrapper);
        }
       return null;
    }

    /**
     * 6.删除学生请假记录
     */
    @Override
    public Integer leaveDelete(LeaveDeleteDTO leaveDeleteDTO) {
            if(loginState() != null) {
                QueryWrapper queryWrapper = new QueryWrapper<AttendanceStudentLeave>()
                        .eq("student_name", leaveDeleteDTO.getStudentName())
                        .eq("del_flag", 0L)
                        .eq("term_id", termInfoConsumer.getNowTerm(null).getId());
                AttendanceStudentLeave attendanceStudentLeave = leaveMapper.selectOne(queryWrapper);
                if (attendanceStudentLeave != null) {
                    attendanceStudentLeave.setDelFlag(1L);
                    return leaveMapper.update(attendanceStudentLeave, queryWrapper);
                }
            }
        return 0;
    }

}
