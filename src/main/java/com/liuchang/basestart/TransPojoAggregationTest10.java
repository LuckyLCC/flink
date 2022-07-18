package com.liuchang.basestart;

import com.liuchang.pojo.Event;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TransPojoAggregationTest10 {


    /***
     * ⚫ sum()：在输入流上，对指定的字段做叠加求和的操作。
     * ⚫ min()：在输入流上，对指定的字段求最小值。
     * ⚫ max()：在输入流上，对指定的字段求最大值。
     * ⚫ minBy()：与 min()类似，在输入流上针对指定字段求最小值。不同的是，min()只计
     * 算指定字段的最小值，其他字段会保留最初第一个数据的值；而 minBy()则会返回包
     * 含字段最小值的整条数据。
     * ⚫ maxBy()：与 max()类似，在输入流上针对指定字段求最大值。两者区别与
     * min()/minBy()完全一致
     *
     * @param args
     * @return void
     * @author: ZhiHao
     * @date: 2022/7/5
     */

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<Event> stream = env.fromElements(
                new Event("Mary", "./home1", 1000L),
                new Event("Bob", "./cart1", 2000L),
                new Event("Mary", "./cart2", 3000L),
                new Event("Bob", "./cart2", 4000L),
                new Event("Bob", "./cart3", 5000L),
                new Event("Bob", "./cart3", 6000L)
        );
        stream.keyBy(e -> e.user)
                .max("timestamp")
                .print("max"); // 指定字段名称
        stream.keyBy(e -> e.user).maxBy("timestamp").print("maxBy"); // 指定字段名称

        env.execute();
    }
}
