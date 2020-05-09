package com.xuecheng.manage_cms.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class RabbitMQService {
    private static final Logger logger = LoggerFactory.getLogger( PageService.class );
    @Autowired
    private CmsPageRepository pageRepository;
    @Autowired
    private CmsSiteRepository siteRepository;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFSBucket gridFSBucket;
    /**
     * 将页面从GridFS上下载存放的服务器物理路径上
     * @param pageId String CmsPage的ID值
     */
    public void savePageToServerPath(String pageId) {
        //1.获得CmsPage
        CmsPage cmsPage = getCmsPageBypageId( pageId );
        //2.获得htmlFile资源
        //2.1获得CmsPage对应的静态页面hmtlFileId（GridFS上的页面数据）
        String htmlFileId = cmsPage.getHtmlFileId();
        GridFsResource gridFsResource = getGridFsResourceByHtmlFileId( cmsPage.getHtmlFileId() );
        //3.获得站点信息
        //获得站点Id
        String siteId = cmsPage.getSiteId();
        CmsSite cmsSite = getCmsSiteBySiteId( siteId );
        //4.将页面发布到服务的物理路径上
        //4.1 获得站点的物理地址
        String sitePagePhysicalPath = cmsSite.getPagePhysicalPath();
        //4.2 获得页面的物理地址
        String pagePhysicalPath = cmsPage.getPagePhysicalPath();
        //4.3 获得静态页面的完整物理路径地址
        String savePath = sitePagePhysicalPath + pagePhysicalPath + cmsPage.getPageName();
        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        //4.4 将页面发布到服务的物理路径上
        try {
            inputStream = gridFsResource.getInputStream();
            //获得静态页面服务的物理地址：htmlpage path = 站点的地址+页面的地址+页面的名称
            outputStream = new FileOutputStream( new File( savePath ) );
            IOUtils.copy( inputStream, outputStream );
        } catch(IOException e) {
            e.printStackTrace();
            logger.error( "get error when download htmlFile is {}", e );
        } finally {
            if(!ObjectUtils.isEmpty( inputStream )) {
                try {
                    inputStream.close();
                } catch(IOException e) {
                    e.printStackTrace();
                    logger.error( "close inputstream error :{}", e );
                }
            }
            if(!ObjectUtils.isEmpty( outputStream )) {
                try {
                    outputStream.close();
                } catch(IOException e) {
                    e.printStackTrace();
                    logger.error( "close outputStream error :{}", e );
                }
            }
        }
    }
    /**
     * 根据siteId获得站点信息
     * @param siteId String 站点的Id
     * @return  CmsSite  站点信息对象
     */
    private CmsSite getCmsSiteBySiteId(String siteId) {
        if(StringUtils.isBlank( siteId ))
            ExceptionCast.cast( CmsCode.CMS_GENERATEHTML_SITEDATAISNULL );
        //获得Site
        Optional<CmsSite> siteOptional = siteRepository.findById( siteId );
        if(!siteOptional.isPresent())
            ExceptionCast.cast( CmsCode.CMS_GENERATEHTML_SITEDATAISNULL );
        return siteOptional.get();
    }
    /**
     * 根据 htmlFileId 获得静态页面资源对象
     * @param htmlFileId String 静态页面Id
     * @return GridFsResource 静态页面资源对象
     */
    private GridFsResource getGridFsResourceByHtmlFileId(String htmlFileId) {
        //判断CmsPage的静态页面的Id是否为空
        if(StringUtils.isBlank( htmlFileId ))
            ExceptionCast.cast( CmsCode.CMS_GENERATEHTML_HTMLISNULL );
        //获得Files对象
        GridFSFile gridFSFile = gridFsTemplate.findOne( Query.query( Criteria.where( "_id" ).is( htmlFileId ) ) );
        //获得下载流对象
        GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream( gridFSFile.getObjectId() );
        //创建chunks对象-GridFSResourc对象
        return new GridFsResource( gridFSFile, downloadStream );
    }
    /**
     * 根据pageId获得CmsPage对象信息
     * @param pageId String CmsPage的Id值
     * @return  CmsPage 页面信息对象信息
     */
    private CmsPage getCmsPageBypageId(String pageId) {
        //判断pageId的合法
        if(StringUtils.isBlank( pageId ))
            ExceptionCast.cast( CommonCode.INVALID_PARAM );
        //根据pageId获得CmsPage
        Optional<CmsPage> pageOptional = pageRepository.findById( pageId );
        //判断根据pageId获得的CmsPage信息
        if(!pageOptional.isPresent())
            ExceptionCast.cast( CmsCode.CMS_GETPAGE_DATANOTFOUND );
        return pageOptional.get();
    }
}
