package com.liuchang;

import com.liuchang.pojo.Event;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TransFilterTest6 {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<Event> stream = env.fromElements(
                new Event("Mary", "./home", 1000L),
                new Event("Bob", "./cart", 2000L)
        );
        // 传入匿名类实现 FilterFunction
        SingleOutputStreamOperator<Event> dataStream = stream.filter(event -> event.getUser().equalsIgnoreCase("bob"));
        dataStream.print();
        // 传入 FilterFunction 实现类
        env.execute();
    }
}
