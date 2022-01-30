package com.whx.crm.workbench.web.controller;

import com.whx.crm.settings.domain.User;
import com.whx.crm.utils.DateTimeUtil;
import com.whx.crm.utils.PrintJson;
import com.whx.crm.utils.ServiceFactory;
import com.whx.crm.utils.UUIDUtil;
import com.whx.crm.vo.PaginationVo;
import com.whx.crm.workbench.domain.Activity;
import com.whx.crm.workbench.domain.Clue;
import com.whx.crm.workbench.domain.Tran;
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
        }else if ("/workbench/clue/getActivityListByClueId.do".equals(path)) {
            getActivityListByClueId(request,response);
        }else if ("/workbench/clue/unbound.do".equals(path)) {
            unbound(request,response);
        }else if ("/workbench/clue/getActivityListByNameAndNotByClueId.do".equals(path)) {
            getActivityListByNameAndNotByClueId(request,response);
        }else if ("/workbench/clue/bund.do".equals(path)) {
            bund(request,response);
        }else if ("/workbench/clue/getActivityListByName.do".equals(path)) {
            getActivityListByName(request,response);
        }else if ("/workbench/clue/convert.do".equals(path)) {
            convert(request,response);
        }
    }

    private void convert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("进行转换");
        String clueId = request.getParameter("clueId");

        //接收是否需要创建交易的标记
        String flag = request.getParameter("flag");

        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        Tran t = null;

        //如果需要创建交易
        if("a".equals(flag)){

            t = new Tran();

            //接收交易表单中的参数
            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();


            t.setId(id);
            t.setMoney(money);
            t.setName(name);
            t.setExpectedDate(expectedDate);
            t.setStage(stage);
            t.setActivityId(activityId);
            t.setCreateBy(createBy);
            t.setCreateTime(createTime);

        }
        ClueService clueService= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
                /*

            为业务层传递的参数：

            1.必须传递的参数clueId，有了这个clueId之后我们才知道要转换哪条记录
            2.必须传递的参数t，因为在线索转换的过程中，有可能会临时创建一笔交易（业务层接收的t也有可能是个null）

         */
        boolean flag1 = clueService.convert(clueId,t,createBy);

        if(flag1){
            response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");

        }

    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("转换线索时，根据名字模糊查询市场活动的信息");
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String aname = request.getParameter("aname");
        List<Activity> activityList=activityService.getActivityListByName(aname);
        PrintJson.printJsonObj(response,activityList);
    }

    private void bund(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到关联市场信息和线索的操作");
        ClueService clueService= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        String[] aids=request.getParameterValues("aid");
        String cid = request.getParameter("cid");
        boolean flag=clueService.bund(cid,aids);
        PrintJson.printJsonFlag(response,flag);

    }

    private void getActivityListByNameAndNotByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("查询市场活动列表，排除掉已经关联的部分");
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String aname = request.getParameter("aname");
        String clueId = request.getParameter("clueId");
        HashMap<String,String> map=new HashMap<>();
        map.put("aname",aname);
        map.put("clueId",clueId);
        List<Activity> activityList=activityService.getActivityListByNameAndNotByClueId(map);
        PrintJson.printJsonObj(response,activityList);
    }

    private void unbound(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到解除线索关联的操作");
        ClueService clueService= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        String id = request.getParameter("id");
        boolean flag=clueService.unbound(id);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查找市场信息和线索关系的步骤");
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String clueId = request.getParameter("clueId");
        List<Activity> activityList=activityService.getActivityListByClueId(clueId);
        PrintJson.printJsonObj(response,activityList);

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
