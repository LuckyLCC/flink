package com.liuchang.test;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

public class hutoolTest {

    public static void main(String[] args) {
//        String id = IdUtil.getSnowflakeNextIdStr();
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        long id = snowflake.nextId();
        String id2 = IdUtil.objectId();
        System.out.println(id);

    }
}
