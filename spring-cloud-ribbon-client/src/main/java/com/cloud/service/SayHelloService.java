package com.cloud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SayHelloService {
    @Autowired
    RestTemplate restTemplate;
    public  String sayHello(String say){
        return restTemplate.getForObject("http://myservice/sayHello?say="+say,String.class);
    }
}
