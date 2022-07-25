package com.liuchang.test;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class hutoolTest {

    public static void main(String[] args) {
//        String id = IdUtil.getSnowflakeNextIdStr();
//        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
//        long id = snowflake.nextId();
//        String id2 = IdUtil.objectId();
//        System.out.println(id);



        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddHHmmssSSS");
        String dateTime = format.format(new Date());

        System.out.println(dateTime);

    }
}
