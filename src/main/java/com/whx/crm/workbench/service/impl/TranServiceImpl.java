package com.whx.crm.workbench.service.impl;

import com.whx.crm.utils.SqlSessionUtil;
import com.whx.crm.workbench.dao.TranDao;
import com.whx.crm.workbench.dao.TranHistoryDao;
import com.whx.crm.workbench.service.TranService;

public class TranServiceImpl implements TranService {
    TranDao tranDao= SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    TranHistoryDao tranHistoryDao= SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
}
