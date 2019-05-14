package com.lxzh.smart.campus.attendance.modular.config;

import cn.stylefeng.roses.system.api.LoginContextService;
import cn.stylefeng.roses.system.api.context.LoginContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginConfiguration {

    @Bean
    public LoginContext loginContext(LoginContextService contextService){
        return new LoginContext(contextService);
    }
}
