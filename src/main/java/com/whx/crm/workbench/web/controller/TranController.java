package com.whx.crm.workbench.web.controller;

import com.whx.crm.settings.domain.User;
import com.whx.crm.settings.service.UserService;
import com.whx.crm.settings.service.impl.UserServiceImpl;
import com.whx.crm.utils.DateTimeUtil;
import com.whx.crm.utils.PrintJson;
import com.whx.crm.utils.ServiceFactory;
import com.whx.crm.utils.UUIDUtil;
import com.whx.crm.vo.PaginationVo;
import com.whx.crm.workbench.domain.Activity;
import com.whx.crm.workbench.domain.Clue;
import com.whx.crm.workbench.domain.Customer;
import com.whx.crm.workbench.domain.Tran;
import com.whx.crm.workbench.service.ActivityService;
import com.whx.crm.workbench.service.ClueService;
import com.whx.crm.workbench.service.CustomerService;
import com.whx.crm.workbench.service.impl.ActivityServiceImpl;
import com.whx.crm.workbench.service.impl.ClueServiceImpl;
import com.whx.crm.workbench.service.impl.CustomerServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到交易控制器");
        String path = request.getServletPath();
        if ("/workbench/transaction/save.do".equals(path)) {
            save(request,response);
        } else if ("/workbench/transaction/getCustomerName.do".equals(path)) {
            getCustomerNames(request,response);
        }
    }

    private void getCustomerNames(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到补齐客户名称模块");
        CustomerService customerService= (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        String name = request.getParameter("name");
        List<String> names=customerService.getCustomerNames(name);
        PrintJson.printJsonObj(response,names);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到创建交易列表页面");
        UserService userService= (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = userService.getUserList();
        request.setAttribute("userList",userList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);
    }
}


