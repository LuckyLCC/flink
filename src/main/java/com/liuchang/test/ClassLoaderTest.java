package com.liuchang.test;

/**
 * @Description: TODO
 * @Author: liuchang
 * @CreateTime: 2022-07-22  15:26
 */
public class ClassLoaderTest {

    public static void main(String[] args) {
        ClassLoaderTest.class.getResourceAsStream("/properties/globalConfig.properties");
        ClassLoaderTest.class.getClassLoader().getResourceAsStream("properties/globalConfig.properties");
        123
                大大大无多
    }

}
