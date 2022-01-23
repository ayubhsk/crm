package com.whx.crm.workbench.service;

import com.whx.crm.vo.PaginationVo;
import com.whx.crm.workbench.domain.Activity;

import java.util.Map;

public interface ActivityService {

    boolean save(Activity a);

    PaginationVo<Activity> pageList(Map<String, Object> hashMap);
}
