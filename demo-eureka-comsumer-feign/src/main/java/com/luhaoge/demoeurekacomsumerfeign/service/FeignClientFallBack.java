package com.luhaoge.demoeurekacomsumerfeign.service;

public class FeignClientFallBack implements UserClient{
    @Override
    public String hello() {
        return "null";
    }

    @Override
    public String hello(int sleep_seconds) {
        return "Hi,容错保护机制已启动！";
    }
}
