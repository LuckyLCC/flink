package com.liuchang.test;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class hutoolTest {

    public static void main(String[] args) throws ParseException {
//        String id = IdUtil.getSnowflakeNextIdStr();
//        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
//        long id = snowflake.nextId();
//        String id2 = IdUtil.objectId();
//        System.out.println(id);



        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddHHmmssSSS");
        String dateTime = format.format(new Date());

        System.out.println(dateTime);

        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = format1.parse("20220808");
        String f = format2.format(parse);
        System.out.println(f);

    }
}
