package com.frank.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: uicsoft-admin
 * @Date: 2024/3/27 9:34 周三
 * @Project_Name: MyLicense
 * @Version: 1.0
 * @description TODO
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/hello")
    public String hello(){
        return "请求成功";
    }
}
