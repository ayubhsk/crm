package com.whx.crm.workbench.dao;


import com.whx.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {

    int save(Clue clue);

    int getTotalByCondition(Map<String, Object> hashMap);

    List<Clue> getClueListByCondition(Map<String, Object> hashMap);

    Clue getClueById(String id);
}
