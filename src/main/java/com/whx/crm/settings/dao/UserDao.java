package com.whx.crm.settings.dao;

import com.whx.crm.settings.domain.User;

import java.util.Map;

public interface UserDao {
    User login(Map<String, String> map);
}
