package com.liuchang.basestart;

import com.liuchang.pojo.Event;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TransMapTest5 {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<Event> stream = env.fromElements(
                new Event("Mary", "./home", 1000L),
                new Event("Bob", "./cart", 2000L)
        );
        // 传入匿名类，实现 MapFunction
        SingleOutputStreamOperator<String> map = stream.map(event -> event.getUser());
        // 传入 MapFunction 的实现类
        map.print();
        env.execute();
    }
}
