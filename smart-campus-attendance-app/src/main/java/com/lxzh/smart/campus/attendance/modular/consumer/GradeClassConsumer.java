package com.lxzh.smart.campus.attendance.modular.consumer;

import com.lxzh.smart.campus.basicinfo.api.GradeAndClassApi;
import com.lxzh.smart.campus.basicinfo.api.model.ClassProvide;
import com.lxzh.smart.campus.basicinfo.api.model.GradeProvide;
import feign.hystrix.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@FeignClient(name = "smart-basicinfo", fallbackFactory = GradeClassConsumer.GradeClassApiFallbackFactory.class)
public interface GradeClassConsumer extends GradeAndClassApi {

    @Component
    class GradeClassApiFallbackFactory implements FallbackFactory<GradeClassConsumer> {


        @Override
        public GradeClassConsumer create(Throwable cause) {
            return new GradeClassConsumer() {
                @Override
                public List<GradeProvide> listByCampusId(Long campusId) {
                    List<GradeProvide> list = new ArrayList<>();
                    GradeProvide gradeProvide1 = new GradeProvide();
                    gradeProvide1.setId(111L);
                    gradeProvide1.setName("初一年级");
                    GradeProvide gradeProvide2 = new GradeProvide();
                    gradeProvide2.setId(112L);
                    gradeProvide2.setName("初二年级");
                    GradeProvide gradeProvide3 = new GradeProvide();
                    gradeProvide3.setId(113L);
                    gradeProvide3.setName("初三年级");
                    list.add(gradeProvide1);
                    list.add(gradeProvide2);
                    list.add(gradeProvide3);
                    return list;
                }

                @Override
                public List<ClassProvide> classListByCampusId(Long campusId) {
                    List<ClassProvide> classListByCampusId = new ArrayList<>();
                    ClassProvide classProvide = new ClassProvide();
                    classProvide.setId(123L);
                    classListByCampusId.add(classProvide);
                    return null;
                }

                @Override
                public GradeProvide getGradeByGradeId(Long gradeId) {
                    return null;
                }

                @Override
                public Map<Long, String> getGradesByIds(Long[] gradeIds) {
                    return null;
                }

                @Override
                public Map<Long, String> getClassesByIds(Long[] classIds) {
                    return null;
                }

                @Override
                public GradeProvide getGradeByClassId(Long classId) {
                    return null;
                }

                @Override
                public List<ClassProvide> classesListByGradeId(Long gradeId) {
                    return null;
                }
            };
        }
    }
}
