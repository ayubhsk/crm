package com.whx.crm.workbench.dao;

import com.whx.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer cus);

    List<String> getCustomerNames(String name);
}
