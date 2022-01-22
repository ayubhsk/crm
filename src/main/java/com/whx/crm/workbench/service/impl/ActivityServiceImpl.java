package com.whx.crm.workbench.service.impl;

import com.whx.crm.utils.SqlSessionUtil;
import com.whx.crm.workbench.dao.ActivityDao;
import com.whx.crm.workbench.domain.Activity;
import com.whx.crm.workbench.service.ActivityService;

public class ActivityServiceImpl implements ActivityService {
    private ActivityDao dao= SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);

    @Override
    public boolean save(Activity a) {
        boolean flag=true;
        int count=dao.save(a);
        if(count!=1) flag=false;
        return flag;
    }
}
