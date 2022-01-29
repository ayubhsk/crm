package com.whx.crm.workbench.service.impl;

import com.whx.crm.settings.dao.UserDao;
import com.whx.crm.settings.domain.User;
import com.whx.crm.utils.SqlSessionUtil;
import com.whx.crm.vo.PaginationVo;
import com.whx.crm.workbench.dao.ClueDao;
import com.whx.crm.workbench.domain.Activity;
import com.whx.crm.workbench.domain.Clue;
import com.whx.crm.workbench.service.ClueService;

import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {
    private ClueDao clueDao=SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private UserDao userDao=SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    @Override
    public List<User> getUsers() {
        List<User> users=userDao.getUserList();
        return users;
    }

    @Override
    public boolean save(Clue clue) {
        boolean flag=true;
        int count=clueDao.save(clue);
        if(count!=1) flag=false;
        return flag;

    }

    @Override
    public PaginationVo<Clue> pageList(Map<String, Object> hashMap) {
        int total=clueDao.getTotalByCondition(hashMap);
        List<Clue> ClueList=clueDao.getClueListByCondition(hashMap);
        PaginationVo<Clue> vo=new PaginationVo<>();
        vo.setTotal(total);
        vo.setDataList(ClueList);
        return vo;
    }

    @Override
    public Clue detail(String id) {
        Clue c=clueDao.getClueById(id);
        return c;

    }
}
