package com.liuchang.test;

import com.liuchang.test.domain.User;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: TODO
 * @Author: liuchang
 * @CreateTime: 2022-08-11  08:53
 */
public class TimeTest {

    public static void main(String[] args) {
//        LocalDate today = LocalDate.now();
//        LocalDate secondDay = today.plusDays(1);
//        LocalDate parse = LocalDate.parse("20220801", DateTimeFormatter.BASIC_ISO_DATE);
//        System.out.println(today);
//        System.out.println(secondDay);
//        System.out.println(parse);

        List<User> users = new LinkedList<>();
        users.add(new User("小明", 18));
        users.add(new User("小红", 18));
        users.add(new User("小兰", 16));
        users.add(new User("小兰", 17));
        users.add(new User("小兰", 17));

        // 根据 name 进行去重
        List<User> userList = users.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(User::getName))),
                        ArrayList::new)
                );
        System.out.println(userList);

    }
}
