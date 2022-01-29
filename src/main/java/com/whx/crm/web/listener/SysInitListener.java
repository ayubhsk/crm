package com.whx.crm.web.listener;

import com.whx.crm.settings.domain.DicValue;
import com.whx.crm.settings.service.DicService;
import com.whx.crm.settings.service.impl.DicServiceImpl;
import com.whx.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;

public class SysInitListener implements ServletContextListener {

    /*

    该方法是用来监听上下文域对象的方法，当服务器启动，上下文域对象创建
    对象创建完毕后，马上执行该方法

    event：该参数能够取得监听的对象
            监听的是什么对象，就可以通过该参数能取得什么对象
            例如我们现在监听的是上下文域对象，通过该参数就可以取得上下文域对象

 */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("上下文作用域对象创建了");
        DicService dicService= (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String, List<DicValue>> map=dicService.getAll() ;
        ServletContext application = event.getServletContext();
        for(Map.Entry<String, List<DicValue>> entry: map.entrySet()){
            application.setAttribute(entry.getKey(),entry.getValue());
        }


    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
