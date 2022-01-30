package com.whx.properti;

import org.junit.Test;

import java.util.*;

public class Test1 {
    @Test
    public void readProperties() {
        Map<String,String> pMap = new HashMap<String,String>();

        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");

        Enumeration<String> e = rb.getKeys();

        while (e.hasMoreElements()){

            //阶段
            String key = e.nextElement();
            //可能性
            String value = rb.getString(key);

            pMap.put(key, value);


        }
        System.out.println(pMap);
    }
}
