package com.whx.crm.workbench.dao;

import com.whx.crm.workbench.domain.Tran;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TranDao {

    int save(Tran t);

    List<Tran> getTranList();

    Tran detail(String id);

    int changeStage(Tran t);

    int getTotal();


    List<Map<String, Object>> getStageCount();
}
