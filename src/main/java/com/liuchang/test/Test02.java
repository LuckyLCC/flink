package com.liuchang.test;

import java.io.File;
import java.io.IOException;

/**
 * @Description: TODO
 * @Author: liuchang
 * @CreateTime: 2022-07-20  14:08
 */
public class Test02 {

    public static void main(String[] args) throws IOException {
        String fileName = "HW_"+".json";
        File file = new File("gz_json/" + fileName);
        boolean isCreated = file.createNewFile();
        System.out.println(isCreated);
    }
}
