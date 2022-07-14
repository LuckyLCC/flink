package com.liuchang.window;

import com.liuchang.pojo.ClickSource;
import com.liuchang.pojo.Event;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.time.Duration;

public class WindowReduceExample04 {

    /***
     *   窗口函数（Window Functions）-增量聚合函数（incremental aggregation functions）-归约函数（ReduceFunction）
     *
     *  ReduceFunction 可以解决大多数归约聚合的问题，但是这个接口有一个限制，就是聚合状
     * 态的类型、输出结果的类型都必须和输入数据类型一样。这就迫使我们必须在聚合前，先将数
     * 据转换（map）成预期结果类型；而在有些情况下，还需要对状态进行进一步处理才能得到输
     * 出结果，这时它们的类型可能不同，使用 ReduceFunction 就会非常麻烦。
     *
     * @param args
     * @return void
     * @author: liuchang
     * @date: 2022/7/14
     */
    public static void main(String[] args) throws Exception {
        //归约函数（ReduceFunction）
        ReduceFunction();

    }

    private static void ReduceFunction() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // 从自定义数据源读取数据，并提取时间戳、生成水位线
        SingleOutputStreamOperator<Event> stream = env.addSource(new ClickSource())
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO)
                        .withTimestampAssigner((SerializableTimestampAssigner<Event>) (element, recordTimestamp) -> element.timestamp));

        stream.map(value -> {
                    // 将数据转换成二元组，方便计算
                    return Tuple2.of(value.user, 1L);
                })
                .keyBy(r -> r.f0)
                // 设置滚动事件时间窗口
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .reduce((value1, value2) -> {
                    // 定义累加规则，窗口闭合时，向下游发送累加结果
                    return Tuple2.of(value1.f0, value1.f1 + value2.f1);
                })
                .print();
        env.execute();
    }
}
