package com.lxzh.smart.campus.attendance.modular.consumer;

import com.lxzh.smart.campus.basicinfo.api.ClassInfoApi;
import com.lxzh.smart.campus.basicinfo.api.model.ClassInfoProvide;
import feign.hystrix.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 班级相关信息的接口
 */

@FeignClient(name = "smart-basicinfo", fallbackFactory = ClassInfoConsumer.ClassInfoApiFallbackFactory.class)
public interface ClassInfoConsumer extends ClassInfoApi {
    @Component
    class ClassInfoApiFallbackFactory implements FallbackFactory<ClassInfoConsumer> {

        @Override
        public ClassInfoConsumer create(Throwable cause) {
            return new ClassInfoConsumer(){

                @Override
                public List<ClassInfoProvide> getRoomNameAndClassName(Long termId, Long[] classIds) {
                    return null;
                }

                @Override
                public ClassInfoProvide getClassIdByEquipmentId(Long termId, Long equipmentId) {
                    return null;
                }


            };
        }
    }
}
