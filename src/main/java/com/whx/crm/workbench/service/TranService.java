package com.whx.crm.workbench.service;

import com.whx.crm.workbench.domain.Tran;
import com.whx.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranService {
    boolean saveTran(Tran t, String customerName);

    List<Tran> getTranList();

    Tran detail(String id);

    List<TranHistory> getHistoryByTranId(String tranId);

    boolean changeStage(Tran t);

    Map<String, Object> getCharts();
}
