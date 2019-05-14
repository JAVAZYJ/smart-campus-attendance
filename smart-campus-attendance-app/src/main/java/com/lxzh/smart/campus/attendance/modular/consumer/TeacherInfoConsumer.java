package com.lxzh.smart.campus.attendance.modular.consumer;

import com.lxzh.smart.campus.api.model.TeacherInfoVo;
import com.lxzh.smart.campus.api.service.TeacherServiceApi;
import feign.hystrix.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import java.util.List;

@FeignClient(name = "smart-teacher" , fallbackFactory = TeacherInfoConsumer.TeacherInfoApiFallbackFactory.class)
public interface TeacherInfoConsumer extends TeacherServiceApi {
    @Component
    class TeacherInfoApiFallbackFactory implements FallbackFactory<TeacherInfoConsumer> {

        @Override
        public TeacherInfoConsumer create(Throwable cause) {
            return new TeacherInfoConsumer(){

                @Override
                public TeacherInfoVo getTeacherInfo(Long teacherId) {
                    TeacherInfoVo teacherInfoVo = new TeacherInfoVo();
                    teacherInfoVo.setName("张无忌");
                    teacherInfoVo.setSchoolId(1L);
                    return teacherInfoVo;
                }

                @Override
                public List<TeacherInfoVo> getTeacherListByIds(String ids) {
                    return null;
                }

                @Override
                public List<TeacherInfoVo> getTeacherListByName(String name) {
                    return null;
                }
            };
        }
    }
}
