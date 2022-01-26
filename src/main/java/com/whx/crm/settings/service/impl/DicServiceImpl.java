package com.whx.crm.settings.service.impl;

import com.whx.crm.settings.dao.DicTypeDao;
import com.whx.crm.settings.dao.DicValueDao;
import com.whx.crm.settings.service.DicService;
import com.whx.crm.utils.SqlSessionUtil;

public class DicServiceImpl implements DicService {
  private DicTypeDao dicTypeDao= SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
  private DicValueDao dicValueDao=SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);
}
