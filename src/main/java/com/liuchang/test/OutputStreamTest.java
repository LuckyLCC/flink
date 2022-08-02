package com.liuchang.test;

import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * @Description: TODO
 * @Author: liuchang
 * @CreateTime: 2022-08-01  09:06
 */
public class OutputStreamTest {

    public static void main(String[] args) {
        ArrayList<String> strings = new ArrayList<>();

        Type genericSuperclass = strings.getClass().getGenericSuperclass();
        // genericInterfaces = java.util.AbstractList<E>
        System.out.println("genericSuperclass = " + genericSuperclass);




    }
}
