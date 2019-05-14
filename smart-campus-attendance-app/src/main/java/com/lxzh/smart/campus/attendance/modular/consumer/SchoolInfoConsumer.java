package com.lxzh.smart.campus.attendance.modular.consumer;

import cn.stylefeng.roses.system.api.SchoolService;
import cn.stylefeng.roses.system.api.model.SchoolCampusInfo;
import cn.stylefeng.roses.system.api.model.SchoolInfo;
import feign.hystrix.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
@FeignClient(value = "roses-system", fallbackFactory = SchoolInfoConsumer.SchoolInfoApiFallbackFactory.class)

public interface SchoolInfoConsumer extends SchoolService {

    @Component
    class SchoolInfoApiFallbackFactory implements FallbackFactory<SchoolInfoConsumer> {

        @Override
        public SchoolInfoConsumer create(Throwable cause) {
            return new SchoolInfoConsumer() {
                @Override
                public List<SchoolInfo> getSchoolList() {
                    List<SchoolInfo> list = new ArrayList<>();

                    SchoolInfo schoolInfo1 = new SchoolInfo();
                    schoolInfo1.setId(1L);
                    schoolInfo1.setName("学校1");
                    List<SchoolCampusInfo> campusList1 = new ArrayList<>();
                    SchoolCampusInfo schoolCampus1 = new SchoolCampusInfo();
                    schoolCampus1.setId(1L);
                    schoolCampus1.setCampusName("东校区1");
                    campusList1.add(schoolCampus1);
                    SchoolCampusInfo schoolCampus2 = new SchoolCampusInfo();
                    schoolCampus2.setId(2L);
                    schoolCampus2.setCampusName("西校区1");
                    campusList1.add(schoolCampus2);
                    schoolInfo1.setCampusList(campusList1);
                    list.add(schoolInfo1);

                    SchoolInfo schoolInfo2 = new SchoolInfo();
                    schoolInfo2.setId(2L);
                    schoolInfo2.setName("学校2");
                    List<SchoolCampusInfo> campusList2 = new ArrayList<>();
                    SchoolCampusInfo schoolCampus11 = new SchoolCampusInfo();
                    schoolCampus11.setId(3L);
                    schoolCampus11.setCampusName("校区2");
                    campusList2.add(schoolCampus11);
                    schoolInfo2.setCampusList(campusList2);
                    list.add(schoolInfo2);

                    SchoolInfo schoolInfo3 = new SchoolInfo();
                    schoolInfo3.setId(3L);
                    schoolInfo3.setName("学校3");
                    List<SchoolCampusInfo> campusList3 = new ArrayList<>();
                    SchoolCampusInfo schoolCampus21 = new SchoolCampusInfo();
                    schoolCampus21.setId(4L);
                    schoolCampus21.setCampusName("东校区3");
                    campusList3.add(schoolCampus21);
                    SchoolCampusInfo schoolCampus22 = new SchoolCampusInfo();
                    schoolCampus22.setId(5L);
                    schoolCampus22.setCampusName("西校区3");
                    campusList3.add(schoolCampus22);
                    SchoolCampusInfo schoolCampus32 = new SchoolCampusInfo();
                    schoolCampus32.setId(6L);
                    schoolCampus32.setCampusName("南校区3");
                    campusList3.add(schoolCampus32);
                    schoolInfo3.setCampusList(campusList3);
                    list.add(schoolInfo3);
                    return list;
                }
            };
        }
    }
}
