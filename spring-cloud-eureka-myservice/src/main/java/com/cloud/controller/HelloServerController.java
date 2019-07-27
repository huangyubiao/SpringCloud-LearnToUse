package com.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloServerController {
    @Value("${server.port}")
    String port;
    @RequestMapping("/sayHello")
    public String hello(@RequestParam String say){
            return say+", i com from the server "+port;

    }
}
