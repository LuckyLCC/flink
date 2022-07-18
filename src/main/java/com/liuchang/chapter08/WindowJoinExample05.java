package com.liuchang.chapter08;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * @Description: 双流联结（Join）-窗口联结（Window Join）
 * 基于时间的操作，最基本的当然就是时间窗口了。我们之前已经介绍过 Window API 的用
 * 法，主要是针对单一数据流在某些时间段内的处理计算。那如果我们希望将两条流的数据进行
 * 合并、且同样针对某段时间进行处理和统计，又该怎么做呢？
 * Flink 为这种场景专门提供了一个窗口联结（window join）算子，可以定义时间窗口，并
 * 将两条流中共享一个公共键（key）的数据放在窗口中进行配对处理。
 * @Author: liuchang
 * @CreateTime: 2022-07-18  11:05
 */
public class WindowJoinExample05 {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStream<Tuple2<String, Long>> stream1 = env
                .fromElements(Tuple2.of("a", 1000L), Tuple2.of("b", 1000L), Tuple2.of("a", 2000L), Tuple2.of("b", 2000L))
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Tuple2<String, Long>>forMonotonousTimestamps().withTimestampAssigner((SerializableTimestampAssigner<Tuple2<String, Long>>) (stringLongTuple2, l) -> stringLongTuple2.f1));

        DataStream<Tuple2<String, Long>> stream2 = env
                .fromElements(Tuple2.of("a", 3000L), Tuple2.of("b", 3000L), Tuple2.of("a", 4000L), Tuple2.of("b", 4000L))
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Tuple2<String, Long>>forMonotonousTimestamps().withTimestampAssigner((SerializableTimestampAssigner<Tuple2<String, Long>>) (stringLongTuple2, l) -> stringLongTuple2.f1));

        stream1.join(stream2)
                .where(r -> r.f0)
                .equalTo(r -> r.f0)
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .apply((JoinFunction<Tuple2<String, Long>, Tuple2<String, Long>, String>) (left, right) -> left + "=>" + right)
                .print();
        env.execute();
    }
}
