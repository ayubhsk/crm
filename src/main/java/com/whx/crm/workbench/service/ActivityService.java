package com.whx.crm.workbench.service;

import com.whx.crm.vo.PaginationVo;
import com.whx.crm.workbench.domain.Activity;
import com.whx.crm.workbench.domain.ActivityRemark;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ActivityService {

    boolean save(Activity a);

    PaginationVo<Activity> pageList(Map<String, Object> hashMap);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndActivity(String id);

    boolean update(Activity a);

    Activity detail(String id);

    boolean deleteById(String id);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    boolean deleteRemark(String id);

    boolean saveRemark(ActivityRemark ar);

    boolean updateRemark(ActivityRemark remark);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameAndNotByClueId(HashMap<String, String> map);

    List<Activity> getActivityListByName(String aname);
}
