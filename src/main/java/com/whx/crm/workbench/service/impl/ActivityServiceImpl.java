package com.whx.crm.workbench.service.impl;

import com.whx.crm.settings.dao.UserDao;
import com.whx.crm.settings.domain.User;
import com.whx.crm.utils.SqlSessionUtil;
import com.whx.crm.vo.PaginationVo;
import com.whx.crm.workbench.dao.ActivityDao;
import com.whx.crm.workbench.dao.ActivityRemarkDao;
import com.whx.crm.workbench.domain.Activity;
import com.whx.crm.workbench.domain.ActivityRemark;
import com.whx.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private ActivityDao activityDao= SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao= SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao=SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    @Override
    public boolean save(Activity a) {
        boolean flag=true;
        int count=activityDao.save(a);
        if(count!=1) flag=false;
        return flag;
    }

    @Override
    public PaginationVo<Activity> pageList(Map<String, Object> hashMap) {
        int total=activityDao.getTotalByCondition(hashMap);
        List<Activity> activityList=activityDao.getActivityListByCondition(hashMap);
        PaginationVo<Activity> vo=new PaginationVo<>();
        vo.setTotal(total);
        vo.setDataList(activityList);
            return vo;
    }

    @Override
    public boolean delete(String[] ids) {
        boolean flag=true;
        //查询出需要删除的备注数量
        int count1=activityRemarkDao.getCountsByAids(ids);

        //删除备注，返回删除的数量
        int count2=activityRemarkDao.deleteByAids(ids);
        if(count1!=count2) flag=false;

        //删除市场活动
        int count3=activityDao.delete(ids);
        if(count3!=ids.length) flag=false;
        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {
        List<User> userList = userDao.getUserList();
        Activity a=activityDao.getById(id);
        Map<String,Object> map=new HashMap<>();
        map.put("userList",userList);
        map.put("a",a);
        return map;
    }

    @Override
    public boolean update(Activity a) {
        boolean flag=true;
        int count=activityDao.update(a);
        if(count!=1) flag=false;
        return flag;
    }

    @Override
    public Activity detail(String id) {
        Activity a = activityDao.detail(id);
        return a;
    }

    @Override
    public boolean deleteById(String id) {
        boolean flag=true;
        int count=activityDao.deleteById(id);
        if(count!=1) flag=false;
        return flag;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {
        List<ActivityRemark> remarks=activityRemarkDao.getRemarkListByAid(activityId);
        return remarks;
    }
}
