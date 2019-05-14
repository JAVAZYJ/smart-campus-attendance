package com.lxzh.smart.campus.attendance.modular.consumer;

import com.lxzh.smart.campus.student.api.StudentApi;
import com.lxzh.smart.campus.student.api.model.StudentBasicInfo;
import feign.hystrix.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 获取学生信息接口
 * @author zyj
 * @since 2019-4-28 08:23:24
 * 备注:fallbackFactory降级处理:意思就是在调用接口是遇到超时等其他故障时，会自动获取下面重写的方法
 */
@FeignClient(value = "smart-student" , fallbackFactory = StudentConsumer.StudentApiFallbackFactory.class)
public interface StudentConsumer extends StudentApi {

    @Component
    class StudentApiFallbackFactory implements FallbackFactory<StudentConsumer> {

        @Override
        public StudentConsumer create(Throwable cause) {
            return new StudentConsumer() {
                @Override
                public List<StudentBasicInfo> getStudentsByClassId(long classId, Long termId, String studentStatus) {
                    return null;
                }

                @Override
                public StudentBasicInfo getStudentById(long studentId, Long termId) {
                    return null;
                }

                @Override
                public List<StudentBasicInfo> getStudentsByIds(Long[] studentIds, Long termId) {
                    return null;
                }

                @Override
                public StudentBasicInfo getStudentById(long campusId, String studentNumber, Long termId) {
                    return null;
                }
            };
        }
    }
}
