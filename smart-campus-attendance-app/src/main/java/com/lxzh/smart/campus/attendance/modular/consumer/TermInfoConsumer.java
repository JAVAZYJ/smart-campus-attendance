package com.lxzh.smart.campus.attendance.modular.consumer;

import com.lxzh.smart.campus.basicinfo.api.TermApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 获取学期信息接口
 * @author zyj
 * @since 2019-4-28 08:23:24
 */
@FeignClient(name = "smart-basicinfo",qualifier = "termInfoConsumer")
public interface TermInfoConsumer extends TermApi {

}
