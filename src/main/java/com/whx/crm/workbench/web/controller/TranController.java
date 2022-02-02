package com.whx.crm.workbench.web.controller;

import com.whx.crm.settings.domain.User;
import com.whx.crm.settings.service.UserService;
import com.whx.crm.settings.service.impl.UserServiceImpl;
import com.whx.crm.utils.DateTimeUtil;
import com.whx.crm.utils.PrintJson;
import com.whx.crm.utils.ServiceFactory;
import com.whx.crm.utils.UUIDUtil;
import com.whx.crm.workbench.domain.Tran;
import com.whx.crm.workbench.domain.TranHistory;
import com.whx.crm.workbench.service.CustomerService;
import com.whx.crm.workbench.service.TranService;
import com.whx.crm.workbench.service.impl.CustomerServiceImpl;
import com.whx.crm.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
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
            save(request, response);
        } else if ("/workbench/transaction/getCustomerName.do".equals(path)) {
            getCustomerNames(request, response);
        } else if ("/workbench/transaction/saveTran.do".equals(path)) {
            saveTran(request, response);
        } else if ("/workbench/transaction/getTranList.do".equals(path)) {
            getTranList(request, response);
        } else if ("/workbench/transaction/detail.do".equals(path)) {
            detail(request, response);
        } else if ("/workbench/transaction/getHistoryByTranId.do".equals(path)) {
            getHistoryByTranId(request, response);
        } else if ("/workbench/transaction/changeStage.do".equals(path)) {
            changeStage(request, response);
        }else if ("/workbench/transaction/getCharts.do".equals(path)) {
            getCharts(request, response);
        }
    }

    private void getCharts(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("生成漏斗图的操作");
        TranService tranService= (TranService) ServiceFactory.getService(new TranServiceImpl());

        //返回一个total，
        //和阶段名:记录数形式的数据
        Map<String,Object> map=tranService.getCharts();
        PrintJson.printJsonObj(response,map);

    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {
        TranService tranService= (TranService) ServiceFactory.getService(new TranServiceImpl());
        System.out.println("进入到修改阶段的操作");
        String id=request.getParameter("id");
        String stage = request.getParameter("stage");//应该跳转到的阶段的名字
        String expectedDate = request.getParameter("expectedDate");//应该跳转到的阶段的名字
        String money = request.getParameter("money");//应该跳转到的阶段的名字
        //String index = request.getParameter("index");//应该跳转到的阶段的下标
        String editTime = DateTimeUtil.getSysTime();
        User user = (User) request.getSession().getAttribute("user");
        String editBy=user.getName();

        Tran t = new Tran();
        t.setId(id);
        t.setStage(stage);
        t.setMoney(money);
        t.setExpectedDate(expectedDate);
        t.setEditBy(editBy);
        t.setEditTime(editTime);



        boolean flag = tranService.changeStage(t);

        ServletContext servletContext = request.getServletContext();
        Map<String,String> pMap = (Map<String,String>)servletContext.getAttribute("pMap");
        t.setPossibility(pMap.get(stage));

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("success", flag);
        map.put("t", t);

        PrintJson.printJsonObj(response, map);

    }

    private void getHistoryByTranId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("详细信息页面刷新历史");
        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
        String tranId = request.getParameter("tranId");
        List<TranHistory> tranHistoryList = tranService.getHistoryByTranId(tranId);
        ServletContext context = request.getServletContext();
        Map<String, String> pMap = (Map<String, String>) context.getAttribute("pMap");
        for (TranHistory history : tranHistoryList) {
            String stage = history.getStage();
            history.setPossibility(pMap.get(stage));
        }
        PrintJson.printJsonObj(response, tranHistoryList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("跳转到详细信息页面");
        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
        String id = request.getParameter("id");
        Tran tran = tranService.detail(id);
        String stage = tran.getStage();
        ServletContext context = request.getServletContext();
        Map<String, String> pMap = (Map<String, String>) context.getAttribute("pMap");
        tran.setPossibility(pMap.get(stage));
        request.setAttribute("t", tran);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request, response);
    }

    private void getTranList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到刷新交易index页面的操作");
        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
        List<Tran> tranList = tranService.getTranList();
        PrintJson.printJsonObj(response, tranList);
    }

    private void saveTran(HttpServletRequest request, HttpServletResponse response) throws IOException {

        System.out.println("执行添加交易的操作");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerName = request.getParameter("customerName"); //此处我们暂时只有客户名称，还没有id
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");

        Tran t = new Tran();
        t.setId(id);
        t.setOwner(owner);
        t.setMoney(money);
        t.setName(name);
        t.setExpectedDate(expectedDate);
        t.setStage(stage);
        t.setType(type);
        t.setSource(source);
        t.setActivityId(activityId);
        t.setContactsId(contactsId);
        t.setCreateTime(createTime);
        t.setCreateBy(createBy);
        t.setDescription(description);
        t.setContactSummary(contactSummary);
        t.setNextContactTime(nextContactTime);

        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = tranService.saveTran(t, customerName);

        if (flag) {

            //如果添加交易成功，跳转到列表页
            response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");

        }

    }

    private void getCustomerNames(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到补齐客户名称模块");
        CustomerService customerService = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        String name = request.getParameter("name");
        List<String> names = customerService.getCustomerNames(name);
        PrintJson.printJsonObj(response, names);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到创建交易列表页面");
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = userService.getUserList();
        request.setAttribute("userList", userList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request, response);
    }
}


