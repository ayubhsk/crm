package com.whx.crm.workbench.service.impl;

import com.whx.crm.utils.SqlSessionUtil;
import com.whx.crm.vo.PaginationVo;
import com.whx.crm.workbench.dao.ActivityDao;
import com.whx.crm.workbench.domain.Activity;
import com.whx.crm.workbench.service.ActivityService;

import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private ActivityDao dao= SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);

    @Override
    public boolean save(Activity a) {
        boolean flag=true;
        int count=dao.save(a);
        if(count!=1) flag=false;
        return flag;
    }

    @Override
    public PaginationVo<Activity> pageList(Map<String, Object> hashMap) {
        int total=dao.getTotalByCondition(hashMap);
        List<Activity> activityList=dao.getActivityListByCondition(hashMap);
        PaginationVo<Activity> vo=new PaginationVo<>();
        vo.setTotal(total);
        vo.setDataList(activityList);
            return vo;
    }
}
