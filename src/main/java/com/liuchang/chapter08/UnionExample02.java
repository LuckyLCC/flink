package com.liuchang.chapter08;

import com.liuchang.pojo.Event;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;

/**
 * @Author: liuchang
 * @CreateTime: 2022-07-18  10:22
 * @Description: 联合（Union）
 * 最简单的合流操作，就是直接将多条流合在一起，叫作流的“联合”（union），
 * 所示。联合操作要求必须流中的数据类型必须相同，合并之后的新流会包括所有流中的元素，
 * 数据类型不变。这种合流方式非常简单粗暴，就像公路上多个车道汇在一起一样
 */
public class UnionExample02 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        SingleOutputStreamOperator<Event> stream1 = env.socketTextStream("hadoop102", 7777)
                .map(data -> {
                    String[] field = data.split(",");
                    return new Event(field[0].trim(), field[1].trim(),
                            Long.valueOf(field[2].trim()));
                })
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ofSeconds(2))
                                .withTimestampAssigner((SerializableTimestampAssigner<Event>) (element, recordTimestamp) -> element.timestamp)
                );
        stream1.print("stream1");
        SingleOutputStreamOperator<Event> stream2 = env.socketTextStream("hadoop103", 7777)
                        .map(data -> {
                            String[] field = data.split(",");
                            return new Event(field[0].trim(), field[1].trim(),
                                    Long.valueOf(field[2].trim()));
                        })
                        .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ofSeconds(5))
                                        .withTimestampAssigner((SerializableTimestampAssigner<Event>) (element, recordTimestamp) -> element.timestamp)
                        );
        stream2.print("stream2");
        // 合并两条流
        stream1.union(stream2)
                .process(new ProcessFunction<Event, String>() {
                    @Override
                    public void processElement(Event value, Context ctx, Collector<String> out) throws Exception {
                        out.collect(" 水 位 线 ： " + ctx.timerService().currentWatermark());
                    }
                })
                .print();
        env.execute();
    }
}
