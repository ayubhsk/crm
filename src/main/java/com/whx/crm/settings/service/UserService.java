package com.whx.crm.settings.service;

import com.whx.crm.exception.LoginException;
import com.whx.crm.settings.domain.User;
import javafx.fxml.LoadException;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoadException, LoginException;
}
