package com.whx.crm.workbench.web.controller;

import com.whx.crm.settings.domain.User;
import com.whx.crm.settings.service.UserService;
import com.whx.crm.settings.service.impl.UserServiceImpl;
import com.whx.crm.utils.*;
import com.whx.crm.workbench.domain.Activity;
import com.whx.crm.workbench.service.ActivityService;
import com.whx.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到市场活动控制器");
        String path = request.getServletPath();
        if ("/workbench/activity/getUserList.do".equals(path)) {
            getUserList(request, response);
        } else if ("/workbench/activity/save.do".equals(path)) {
            save(request, response);

        }
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动添加操作");
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String createTime = DateTimeUtil.getSysTime();//创建是时间为当前时间
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        Activity a=new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setCreateTime(createTime);
        a.setCreateBy(createBy);
        boolean flag=activityService.save(a);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> users = userService.getUserList();
        PrintJson.printJsonObj(response, users);

    }


}
