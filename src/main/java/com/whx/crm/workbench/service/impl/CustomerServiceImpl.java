package com.whx.crm.workbench.service.impl;

import com.whx.crm.utils.SqlSessionUtil;
import com.whx.crm.workbench.dao.CustomerDao;
import com.whx.crm.workbench.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    CustomerDao customerDao= SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);



    @Override
    public List<String> getCustomerNames(String name) {
        List<String> names=customerDao.getCustomerNames(name);
        return names;
    }
}
