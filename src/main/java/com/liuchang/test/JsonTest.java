package com.liuchang.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @Description: TODO
 * @Author: liuchang
 * @CreateTime: 2022-07-21  10:33
 */
public class JsonTest {

    public static void main(String[] args) {
        String str="{\n" +
                "\t\"items\": [{\n" +
                "\t\t\"companyId\": \"RY\",\n" +
                "\t\t\"registerDate\": \"\",\n" +
                "\t\t\"passengerPhone\": \"13012312311\",\n" +
                "\t\t\"passengerName\": \"\",\n" +
                "\t\t\"passengerSex\": \"\",\n" +
                "\t\t\"state\": 0,\n" +
                "\t\t\"flag\": 1,\n" +
                "\t\t\"updateTime\": \"2016-12-23 16:16:16\",\n" +
                "\t\t\"success\": 1,\n" +
                "\t\t\"reason\": null\n" +
                "\t}, {\n" +
                "\t\t\"companyId\": \"RY\",\n" +
                "\t\t\"registerDate\": \"\",\n" +
                "\t\t\"passengerPhone\": \"13012312312\",\n" +
                "\t\t\"passengerName\": \"\",\n" +
                "\t\t\"passengerSex\": \"\",\n" +
                "\t\t\"state\": 0,\n" +
                "\t\t\"flag\": 1,\n" +
                "\t\t\"updateTime\": \"2016-12-23 16:16:16\",\n" +
                "\t\t\"success\": 1,\n" +
                "\t\t\"reason\": null\n" +
                "\t}, {\n" +
                "\t\t\"companyId\": \"RY\",\n" +
                "\t\t\"registerDate\": \"\",\n" +
                "\t\t\"passengerPhone\": \"13012312313\",\n" +
                "\t\t\"passengerName\": \"\",\n" +
                "\t\t\"passengerSex\": \"\",\n" +
                "\t\t\"state\": 0,\n" +
                "\t\t\"flag\": 1,\n" +
                "\t\t\"updateTime\": \"2016-12-23 16:16:16\",\n" +
                "\t\t\"success\": 1,\n" +
                "\t\t\"reason\": null\n" +
                "\t}, {\n" +
                "\t\t\"companyId\": \"RY\",\n" +
                "\t\t\"registerDate\": \"\",\n" +
                "\t\t\"passengerPhone\": \"13012312314\",\n" +
                "\t\t\"passengerName\": \"\",\n" +
                "\t\t\"passengerSex\": \"\",\n" +
                "\t\t\"state\": 0,\n" +
                "\t\t\"flag\": 1,\n" +
                "\t\t\"updateTime\": \"2016-12-23 16:16:16\",\n" +
                "\t\t\"success\": 1,\n" +
                "\t\t\"reason\": null\n" +
                "\t}, {\n" +
                "\t\t\"companyId\": \"RY\",\n" +
                "\t\t\"registerDate\": \"\",\n" +
                "\t\t\"passengerPhone\": \"13012312315\",\n" +
                "\t\t\"passengerName\": \"\",\n" +
                "\t\t\"passengerSex\": \"\",\n" +
                "\t\t\"state\": 0,\n" +
                "\t\t\"flag\": 1,\n" +
                "\t\t\"updateTime\": \"yyyy-MM-dd HH:mm:ss\",\n" +
                "\t\t\"success\": 0,\n" +
                "\t\t\"reason\": \"更新时间格式有误;\"\n" +
                "\t}]\n" +
                "}";
        JSONObject jsonObject = JSON.parseObject(str);


    }
}
