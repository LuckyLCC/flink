package com.liuchang.test;

import java.util.Random;

public class RandomTest {

    public static void main(String[] args) {
        Random random = new Random();


        for (int i = 0; i < 10; i++) {
            int anInt = random.nextInt(100);
            System.out.println(anInt);
        }


    }
}
