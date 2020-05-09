package com.xuecheng.manage_cms.service.web.controller;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsPageParam;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-09-12 18:11
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {

    @Autowired
    CmsPageRepository cmsPageRepository;



    @Test
    public void testFindAll(){
        List<CmsPage> all = cmsPageRepository.findAll();
        System.out.println(all);

    }

    //分页查询
    @Test
    public void testFindPage(){
        //分页参数
        int page = 1;//从0开始
        int size = 10;
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        System.out.println(all);
    }

    //修改
    @Test
    public void testUpdate() {
        //查询对象
        Optional<CmsPage> optional = cmsPageRepository.findById("5b4b1d8bf73c6623b03f8cec");
        if(optional.isPresent()){
            CmsPage cmsPage = optional.get();
            //设置要修改值
            cmsPage.setPageAliase("test01");
            //...
            //修改
            CmsPage save = cmsPageRepository.save(cmsPage);
            System.out.println(save);
        }

    }

    //根据页面名称查询
    @Test
    public void testfindByPageName(){
        CmsPage cmsPage = cmsPageRepository.findByPageName("测试页面");
        System.out.println(cmsPage);
    }
    //添加
    @Test
    public void testInsert(){
        //定义页面管理实体类
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId("s01");
        cmsPage.setTemplateId("t01");
        cmsPage.setPageName("测试页面");
        cmsPage.setPageCreateTime(new Date());

        //定义页面管理参数列表实体类
        List<CmsPageParam> cmsPageParams = new ArrayList<>();
        CmsPageParam cmsPageParam = new CmsPageParam();
        cmsPageParam.setPageParamName("param1");
        cmsPageParam.setPageParamValue("value1");
        cmsPageParams.add(cmsPageParam);

        //将页面管理参数列表添加到页面管理中
        cmsPage.setPageParams(cmsPageParams);

        //保存页面管理对象
        cmsPageRepository.save(cmsPage);
        System.out.println(cmsPage);
    }
    @Test
    public void queryFindAll(){
        //创建查询条件匹配器
        ExampleMatcher matching = ExampleMatcher.matching();
        CmsPage cmsPage=new CmsPage();
        cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
        //模板ID
        cmsPage.setTemplateId( "5a962b52b00ffc514038faf7" );
        //页面别名
        cmsPage.setPageAliase("首页");
        Example<CmsPage> all = Example.of(cmsPage, matching);
        PageRequest pageable = PageRequest.of(0, 10);
        Page<CmsPage> allcms = cmsPageRepository.findAll(all, pageable);
        System.out.println(allcms.getContent());
    }
    @Test
    public void queryFindbyidandnameandpath(){
        //创建查询条件匹配器
        ExampleMatcher matching = ExampleMatcher.matching();
        CmsPage cmsPage=new CmsPage();
        cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
        //模板ID
        cmsPage.setPageName("12");
        //页面别名
        cmsPage.setPageWebPath("12");
        Example<CmsPage> all = Example.of(cmsPage, matching);
        PageRequest pageable = PageRequest.of(0, 10);
        Page<CmsPage> all1 = cmsPageRepository.findAll(all, pageable);
        System.out.println(all1.getContent());
    }
    @Autowired
    RestTemplate restTemplate;
    @Test
    public void testRestTemplate(){
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f", Map.class);
        System.out.println(forEntity);
    }
}
