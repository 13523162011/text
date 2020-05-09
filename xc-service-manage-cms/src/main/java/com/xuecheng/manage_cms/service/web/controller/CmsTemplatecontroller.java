package com.xuecheng.manage_cms.service.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-09-12 18:11
 **/
@Controller
@RequestMapping("/freemarker")
public class CmsTemplatecontroller {

    @Autowired
    RestTemplate restTemplate;
    @RequestMapping("/banner")
    public String index_banner(Map<String,Object> map){
        String dataUrl="http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f";
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        map.putAll(body);
        return "index_banner";
    }
}
