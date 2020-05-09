package com.xuecheng.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.manage_cms.service.RabbitMQService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Component
public class ConsumerPostPage {
    private final static Logger LOGGER = LoggerFactory.getLogger( ConsumerPostPage.class );
    @Autowired
    private RabbitMQService rabbitMQService;
    @RabbitListener(queues = {"${xuecheng.mq.queue}"})
    public void postPage(byte[] arrs) {
        String msg = null;
        try {
            msg = new String( arrs, "UTF-8" );
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //1.判断msg的数据是否有效
        if(StringUtils.isBlank( msg )) {
            LOGGER.error( "获得msg信息为空" );
            return ;
        }
        //2.将msg中的json信息比哪位Map数据
        Map map = JSON.parseObject( msg, Map.class );
        //3.从map中获得pageId值
        Object pageId = map.get( "pageId" );
        //4.判断pageId的有效性
        if(StringUtils.isBlank( pageId.toString() )) {
            LOGGER.error( "获得的pageId信息无效" );
            return ;
        }
        try {
            //5.根据pageId将对应的静态化页面发布到服务器物理路径下
            rabbitMQService.savePageToServerPath( pageId.toString() );
        } catch(Exception e) {
            e.printStackTrace();
            LOGGER.error( "静态化页面发布错误", e );
        }
    }
}
