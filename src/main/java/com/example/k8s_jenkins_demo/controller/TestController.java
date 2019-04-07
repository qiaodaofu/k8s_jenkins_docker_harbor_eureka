package com.example.k8s_jenkins_demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class TestController {

    @RequestMapping("getTest")
    public Object getTest(){
        System.out.println("11");
        return "辛苦了";
    }

}
