package com.deptManager.Dept.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class Demo {
    @GetMapping("/hello")
    public String greeting(){
        return "Hello buddy";
    }
}
