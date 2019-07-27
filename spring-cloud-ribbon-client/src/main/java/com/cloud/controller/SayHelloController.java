package com.cloud.controller;

import com.cloud.service.SayHelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SayHelloController {
    @Autowired
    SayHelloService sayHelloService;
    @RequestMapping ("/sayHello")
    public String sayHello(@RequestParam String say){
        return sayHelloService.sayHello(say);

    }
}
