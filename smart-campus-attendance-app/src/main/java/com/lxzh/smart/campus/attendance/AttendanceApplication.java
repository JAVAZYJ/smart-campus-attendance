package com.lxzh.smart.campus.attendance;

import cn.stylefeng.roses.core.config.DbInitializerAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = DbInitializerAutoConfiguration.class)
@EnableFeignClients(basePackages = "com.lxzh.smart.campus.attendance.modular.consumer")
@MapperScan("com.lxzh.smart.campus.attendance.modular.mapper")
@EnableScheduling //使能定时任务
public class AttendanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AttendanceApplication.class, args);
    }
}
