package com.whx.crm.settings.service.impl;

import com.whx.crm.settings.dao.DicTypeDao;
import com.whx.crm.settings.dao.DicValueDao;
import com.whx.crm.settings.domain.DicType;
import com.whx.crm.settings.domain.DicValue;
import com.whx.crm.settings.service.DicService;
import com.whx.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {
  private DicTypeDao dicTypeDao= SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
  private DicValueDao dicValueDao=SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

  @Override
  public Map<String, List<DicValue>> getAll() {
    //将字典类型列表取出
    System.out.println("服务器缓存处理数据字典开始");
    List<DicType> dicTypeList=dicTypeDao.getTypeList();
    HashMap<String, List<DicValue>> map=new HashMap<>();
    for(DicType dc:dicTypeList){
      String code=dc.getCode();
      List<DicValue> dicValueList=dicValueDao.getValueListByCode(code);
      map.put(code+"List",dicValueList);
    }
    System.out.println("服务器缓存处理数据字典结束");
    return map;
  }
}
