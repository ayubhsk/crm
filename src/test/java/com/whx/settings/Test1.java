package com.whx.settings;

import com.whx.crm.utils.DateTimeUtil;
import org.junit.Test;

public class Test1 {
    @Test
    public void name() {
        String currentTime = DateTimeUtil.getSysTime();
        String time="2022-01-08 00:05:35";
        System.out.println(currentTime.compareTo(time));
    }
}
