package com.whx.crm.workbench.service.impl;

import com.whx.crm.utils.DateTimeUtil;
import com.whx.crm.utils.SqlSessionUtil;
import com.whx.crm.utils.UUIDUtil;
import com.whx.crm.workbench.dao.CustomerDao;
import com.whx.crm.workbench.dao.TranDao;
import com.whx.crm.workbench.dao.TranHistoryDao;
import com.whx.crm.workbench.domain.Customer;
import com.whx.crm.workbench.domain.Tran;
import com.whx.crm.workbench.domain.TranHistory;
import com.whx.crm.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {
    TranDao tranDao= SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    TranHistoryDao tranHistoryDao= SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    CustomerDao customerDao= SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    @Override
    public boolean saveTran(Tran t, String customerName) {
        /*
            交易添加业务：
                在做添加之前，参数t里面就少了一项信息，就是客户的主键，customerId
                先处理客户相关的需求
                （1）判断customerName，根据客户名称在客户表进行精确查询
                       如果有这个客户，则取出这个客户的id，封装到t对象中
                       如果没有这个客户，则再客户表新建一条客户信息，然后将新建的客户的id取出，封装到t对象中
                （2）经过以上操作后，t对象中的信息就全了，需要执行添加交易的操作
                （3）添加交易完毕后，需要创建一条交易历史
         */

        boolean flag = true;

        Customer cus = customerDao.getCustomerByName(customerName);

        //如果cus为null，需要创建客户
        if(cus==null){
            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setName(customerName);
            cus.setCreateBy(t.getCreateBy());
            cus.setCreateTime(DateTimeUtil.getSysTime());
            cus.setContactSummary(t.getContactSummary());
            cus.setNextContactTime(t.getNextContactTime());
            cus.setOwner(t.getOwner());
            //添加客户
            int count1 = customerDao.save(cus);
            if(count1!=1){
                flag = false;
            }

        }

        //通过以上对于客户的处理，不论是查询出来已有的客户，还是以前没有我们新增的客户，总之客户已经有了，客户的id就有了
        //将客户id封装到t对象中
        t.setCustomerId(cus.getId());

        //添加交易
        int count2 = tranDao.save(t);
        if(count2!=1){
            flag = false;
        }

        //添加交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(t.getId());
        th.setStage(t.getStage());
        th.setMoney(t.getMoney());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setCreateBy(t.getCreateBy());
        int count3 = tranHistoryDao.save(th);
        if(count3!=1){
            flag = false;
        }

        return flag;
    }

    @Override
    public List<Tran> getTranList() {
        List<Tran> tranList=tranDao.getTranList();
        return tranList;

    }

    @Override
    public Tran detail(String id) {
        Tran tran=tranDao.detail(id);
        return tran;

    }

    @Override
    public List<TranHistory> getHistoryByTranId(String tranId) {
        List<TranHistory> tranHistoryList=tranHistoryDao.getHistoryByTranId(tranId);
        return tranHistoryList;

    }

    @Override
    public boolean changeStage(Tran t) {
        boolean flag=true;
        int count1=tranDao.changeStage(t);
        if(count1==1){
            TranHistory ts=new TranHistory();
            ts.setId(UUIDUtil.getUUID());
            ts.setCreateTime(DateTimeUtil.getSysTime());
            ts.setPossibility(t.getPossibility());
            ts.setCreateBy(t.getEditBy());
            ts.setStage(t.getStage());
            ts.setTranId(t.getId());
            ts.setMoney(t.getMoney());
            ts.setExpectedDate(t.getExpectedDate());
            int count2=tranHistoryDao.changeStage(ts);
            if(count2!=1) flag=false;
        }else {
            flag=false;
        }
        return flag;

    }

    @Override
    public Map<String, Object> getCharts() {
        HashMap<String,Object> hashMap=new HashMap<>();
        //获取total
        int total=tranDao.getTotal();


        //获取stage:count形式的map
        List<Map<String, Object>> dataList=tranDao.getStageCount();
        hashMap.put("total",total);
        hashMap.put("dataList",dataList);

        return hashMap;

    }
}
