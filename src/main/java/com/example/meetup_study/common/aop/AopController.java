package com.example.meetup_study.common.aop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
public class AopController {

//test
    @Timer
    @GetMapping("/test/1")
    public void test1() throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("test1");
    }

    @GetMapping("/test/2")
    public void test2(){
        System.out.println("test2");
    }

    @GetMapping("/test/3")
    public String test3(){
        System.out.println("test3");
        return "a";
    }
}
