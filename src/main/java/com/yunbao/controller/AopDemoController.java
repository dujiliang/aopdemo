package com.yunbao.controller;

import com.yunbao.annotation.SysLog;
import com.yunbao.service.CountSservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: djl
 * @Date: 2019/4/18 11:39
 * @Version 1.0
 */
@RestController
@RequestMapping("aopDemoController")
public class AopDemoController {

    @Autowired
    private CountSservice countSservice;

    @SysLog
    @GetMapping("/test")
    public String test(String name,String hello){
        return name;
    }


    @RequestMapping("lockDemo")
    public Object lockDemo(Integer userId,Integer countId) {
        int count = countSservice.doUserCount(userId,countId);
        return true;
    }
}
