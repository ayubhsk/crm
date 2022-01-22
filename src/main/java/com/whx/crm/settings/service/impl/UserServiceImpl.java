package com.whx.crm.settings.service.impl;

import com.whx.crm.exception.LoginException;
import com.whx.crm.settings.dao.UserDao;
import com.whx.crm.settings.domain.User;
import com.whx.crm.settings.service.UserService;
import com.whx.crm.utils.DateTimeUtil;
import com.whx.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public List<User> getUserList() {
        List<User> users=userDao.getUserList();
        return users;
    }

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {

        Map<String,String> map = new HashMap<String,String>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);

        User user = userDao.login(map);

        if(user==null){

            throw new LoginException("账号密码错误");

        }

        //如果程序能够成功的执行到该行，说明账号密码正确
        //需要继续向下验证其他3项信息

        //验证失效时间
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if(currentTime.compareTo(expireTime)>0){
            throw new LoginException("账号已经失效");
        }

        String lockState = user.getLockState();
        if("0".equals(lockState)){
            throw new LoginException("账号已经锁定");
        }

        //判断ip地址
/*        String allowIps = user.getAllowIps();
        if(!allowIps.contains(ip)){
            throw new LoginException("无效的IP地址");
        }*/
        return user;
    }
}
