package com.whx.crm.workbench.service;

import com.whx.crm.settings.domain.User;
import com.whx.crm.vo.PaginationVo;
import com.whx.crm.workbench.domain.Activity;
import com.whx.crm.workbench.domain.Clue;
import com.whx.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface ClueService {
    List<User> getUsers();

    boolean save(Clue clue);

    PaginationVo<Clue> pageList(Map<String, Object> hashMap);

    Clue detail(String id);


    boolean unbound(String id);

    boolean bund(String cid, String[] aids);

    boolean convert(String clueId, Tran t, String createBy);
}
