package com.whx.crm.settings.dao;

import com.whx.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {

    List<DicValue> getValueListByCode(String code);
}
