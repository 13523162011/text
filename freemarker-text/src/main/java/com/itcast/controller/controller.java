package com.itcast.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/freemarker")
@Controller
public class controller {
    @RequestMapping("/text1")
    public String freemarker(Map<String, Object> map){
        map.put("name","黑马程序员");
        //返回模板文件名称
        return "test1";
    }
}
