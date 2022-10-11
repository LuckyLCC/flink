package com.liuchang.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.liuchang.test.domain.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO
 * @Author: liuchang
 * @CreateTime: 2022-09-13  14:49
 */
public class JsonTest02 {

    public static void main(String[] args) {
        List<JSONObject> objectList = new ArrayList<>();
        User lc = new User("lc", 23);
        String lc1 = JSON.toJSONString(lc);
        JSONObject jsonObject = JSON.parseObject(lc1);
        objectList.add(jsonObject);
        objectList.add(jsonObject);
        objectList.add(jsonObject);


        System.out.println(objectList);

        String string = JSON.toJSONString(objectList);
        System.out.println("-----------------"+string);

//        User user = new User("lc",21);
//        List<User> list = new ArrayList<User>();
//        list.add(user);
//        list.add(user);
//        list.add(user);
//        System.out.println(list);
//
//        String string = JSON.toJSONString(list);
//        System.out.println(string);

    }
}
