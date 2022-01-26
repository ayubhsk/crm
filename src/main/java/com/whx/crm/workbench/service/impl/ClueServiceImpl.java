package com.whx.crm.workbench.service.impl;

import com.whx.crm.utils.SqlSessionUtil;
import com.whx.crm.workbench.dao.ClueDao;
import com.whx.crm.workbench.service.ClueService;

public class ClueServiceImpl implements ClueService {
    private ClueDao clueDao=SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
}
