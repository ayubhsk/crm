package com.whx.crm.workbench.web.controller;

import com.whx.crm.settings.domain.User;
import com.whx.crm.utils.DateTimeUtil;
import com.whx.crm.utils.PrintJson;
import com.whx.crm.utils.ServiceFactory;
import com.whx.crm.utils.UUIDUtil;
import com.whx.crm.vo.PaginationVo;
import com.whx.crm.workbench.domain.Activity;
import com.whx.crm.workbench.domain.Clue;
import com.whx.crm.workbench.service.ActivityService;
import com.whx.crm.workbench.service.ClueService;
import com.whx.crm.workbench.service.impl.ActivityServiceImpl;
import com.whx.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ClueController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到线索控制器");
        String path = request.getServletPath();
        if ("/workbench/clue/create.do".equals(path)) {
            create(request,response);
        }else if ("/workbench/clue/save.do".equals(path)) {
            save(request,response);
        }else if ("/workbench/clue/pageList.do".equals(path)) {
            pageList(request,response);
        }else if ("/workbench/clue/detail.do".equals(path)) {
            detail(request,response);
        }
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ClueService clueService= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        System.out.println("进入到详细信息页面模块");
        String id = request.getParameter("id");
        Clue c=clueService.detail(id);
        request.setAttribute("c",c);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request,response);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        ClueService clueService= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        System.out.println("进入到查询线索信息模块（分页查询和信息查询）");
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);//第几页
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);//每页展示的记录数
        //计算略过的记录数
        int skipCount = (pageNo - 1) * pageSize;

/*        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");*/

        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("pageSize", pageSize);
        hashMap.put("skipCount", skipCount);
/*        hashMap.put("name", name);
        hashMap.put("owner", owner);
        hashMap.put("startDate", startDate);
        hashMap.put("endDate", endDate);*/

        //vo专门传给前端
        PaginationVo<Clue> vo = clueService.pageList(hashMap);
        PrintJson.printJsonObj(response, vo);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到保存线索的步骤");
        ClueService clueService= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String createBy = request.getParameter("createBy");
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");
        String id= UUIDUtil.getUUID();
        String createTime= DateTimeUtil.getSysTime();

        Clue clue=new Clue();
        clue.setFullname(fullname);
        clue.setAppellation(appellation);
        clue.setOwner(owner);
        clue.setCompany(company);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setPhone(phone);
        clue.setWebsite(website);
        clue.setMphone(mphone);
        clue.setState(state);
        clue.setSource(source);
        clue.setCreateBy(createBy);
        clue.setDescription(description);
        clue.setContactSummary(contactSummary);
        clue.setNextContactTime(nextContactTime);
        clue.setAddress(address);
        clue.setId(id);
        clue.setCreateTime(createTime);
        boolean flag=clueService.save(clue);
        PrintJson.printJsonFlag(response,flag);


    }

    private void create(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到创建线索的步骤");
        ClueService clueService= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        List<User> users =clueService.getUsers();
        PrintJson.printJsonObj(response,users);
    }
}
