package com.xuecheng.ucenter.service;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import com.xuecheng.framework.domain.ucenter.XcUser;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.ucenter.dao.XcCompanyUserRepository;
import com.xuecheng.ucenter.dao.XcUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class UserService {
    @Autowired
    private XcUserRepository xcUserRepository;
    @Autowired
    private XcCompanyUserRepository companyUserRepository;
    //根据用户名查询用户信息
    public XcUser findXcUserByUsername(String username){
        return xcUserRepository.findXcUserByUsername(username);
    }
    //根据账号查询用户的信息，返回用户扩展信息
    public XcUserExt getUserExt(String username){
        //1.根据用户账号获得用户的基本信息
        XcUser xcUser = this.findXcUserByUsername(username);
        if(ObjectUtils.isEmpty(xcUser))
            return null;
            //2.创建用户扩展信息（包括用户的权限信息、公司Id）
            XcUserExt xcUserExt=new XcUserExt();
            BeanUtils.copyProperties(xcUser,xcUserExt);
            //3.根据用户id查询公司的Id信息并分装到UserExt对象中
            String id = xcUserExt.getId();
            XcCompanyUser xcCompanyUser = companyUserRepository.findByUserId(id);
            if(!ObjectUtils.isEmpty( xcCompanyUser ))
                xcUserExt.setCompanyId( xcCompanyUser.getCompanyId() );
            return xcUserExt;
    }
}
