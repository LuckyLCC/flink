package com.liuchang.window;

import com.liuchang.pojo.ClickSource;
import com.liuchang.pojo.Event;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.OutputTag;

public class sideOutputLateData09 {


    /***
     *
     *将迟到的数据放入侧输出流
     * @param args
     * @return void
     * @author: liuchang
     * @date: 2022/7/14
     */

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        SingleOutputStreamOperator<Event> stream = env.addSource(new ClickSource()).assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forMonotonousTimestamps().withTimestampAssigner((SerializableTimestampAssigner<Event>) (element, recordTimestamp) -> element.timestamp));
        //定义标签
        OutputTag<Event> outputTag = new OutputTag<Event>("late") {
        };

        stream.keyBy(data -> data.url)
                .window(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5)))
                //将迟到的数据放入侧输出流
                .sideOutputLateData(outputTag)
                // 同时传入增量聚合函数和全窗口函数
                .aggregate(new UrlViewCountExample07.UrlViewCountAgg(), new UrlViewCountExample07.UrlViewCountResult())
                .print();
        env.execute();

    }
}
