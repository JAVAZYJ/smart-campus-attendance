package com.lxzh.smart.campus.attendance.modular.consumer;

import com.lxzh.smart.campus.basicinfo.api.TeacherClassApi;
import com.lxzh.smart.campus.basicinfo.api.model.ClassInfoProvide;
import com.lxzh.smart.campus.basicinfo.api.model.TeacherClassProvide;
import feign.hystrix.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import java.util.List;

@FeignClient(name = "smart-basicinfo" , fallbackFactory = TeacherClassConsumer.TeacherClassApiFallbackFactory.class)
public interface TeacherClassConsumer extends TeacherClassApi {

    @Component
    class TeacherClassApiFallbackFactory implements FallbackFactory<TeacherClassConsumer> {


        @Override
        public TeacherClassConsumer create(Throwable cause) {
            return new TeacherClassConsumer() {
                @Override
                public List<TeacherClassProvide> getClass(Long teacherId, Long termId) {
                    return null;
                }

                @Override
                public TeacherClassProvide getTeacher(Long termId, Long classId, Long lessonId) {
                    TeacherClassProvide teacherClassProvide = new TeacherClassProvide();
                    teacherClassProvide.setClassId(1121573239109480450L);
                    return teacherClassProvide;
                }

                @Override
                public List<TeacherClassProvide> getTeacherIdAndLessonId(Long classId, Long termId) {
                    return null;
                }
            };
        }
    }
}
