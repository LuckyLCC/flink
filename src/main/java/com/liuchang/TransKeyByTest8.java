package com.liuchang;

import com.liuchang.pojo.Event;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TransKeyByTest8 {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<Event> stream = env.fromElements(
                new Event("Mary", "./home", 1000L),
                new Event("Bob", "./cart", 2000L),
        new Event("Bob", "./cart111", 202200L)
        );
        // 使用 Lambda 表达式
        KeyedStream<Event, String> keyedStream = stream.keyBy(e -> e.user);
        keyedStream.print();
        env.execute();
    }
}
