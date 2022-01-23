package com.whx.crm.workbench.dao;

import com.whx.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int save(Activity a);

    List<Activity> getActivityListByCondition(Map<String, Object> hashMap);

    int getTotalByCondition(Map<String, Object> hashMap);
}
