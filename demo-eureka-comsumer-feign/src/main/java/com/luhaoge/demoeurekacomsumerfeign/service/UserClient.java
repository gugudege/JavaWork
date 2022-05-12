package com.luhaoge.demoeurekacomsumerfeign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * FeignClient定义在接口上，指定接口中的方法远程调用的服务名
 */
@FeignClient(name = "eureka-provider1",fallback = FeignClientFallBack.class)
public interface UserClient {
    @RequestMapping(value = "/port")//当借口调用hello()方法时，相当于调用UserClient接口的/port接口
    public String hello();

    @RequestMapping(value = "/user/sayHi")
    public String hello(@RequestParam(value = "sleep_seconds")int sleep_seconds);
}
