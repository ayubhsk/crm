package com.whx.crm.settings.service.impl;

import com.whx.crm.settings.dao.UserDao;
import com.whx.crm.settings.service.UserService;
import com.whx.crm.utils.SqlSessionUtil;

public class UserServiceImpl implements UserService {
    private UserDao userDao= SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
}
