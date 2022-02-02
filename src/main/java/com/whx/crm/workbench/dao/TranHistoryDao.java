package com.whx.crm.workbench.dao;

import com.whx.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    int save(TranHistory th);

    List<TranHistory> getHistoryByTranId(String tranId);

    int changeStage(TranHistory ts);
}
