package com.lxzh.smart.campus.attendance.modular.consumer;

import cn.stylefeng.roses.system.api.LoginContextService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("roses-system")
public interface LoginContextServiceConsumer extends LoginContextService {
}