package com.liuchang.test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Description: TODO
 * @Author: liuchang
 * @CreateTime: 2022-09-14  15:53
 */
public class auth {

    public static void main(String[] args) {
        String yyyyMMddHHmmss1 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        Long yyyyMMddHHmmss = Long.valueOf(yyyyMMddHHmmss1);

        System.out.println(yyyyMMddHHmmss);
    }
}
