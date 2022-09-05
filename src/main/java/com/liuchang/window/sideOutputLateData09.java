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
     *  我们自然会想到，即使可以设置窗口的延迟时间，终归还是有限的，后续的数据还是会被
     * 丢弃。如果不想丢弃任何一个数据，又该怎么做呢？
     * Flink 还提供了另外一种方式处理迟到数据。我们可以将未收入窗口的迟到数据，放入“侧
     * 输出流”（side output）进行另外的处理。所谓的侧输出流，相当于是数据流的一个“分支”，
     * 这个流中单独放置那些错过了该上的车、本该被丢弃的数据。
     * 基于 WindowedStream 调用.sideOutputLateData() 方法，就可以实现这个功能。方法需要
     * 传入一个“输出标签”（OutputTag），用来标记分支的迟到数据流。因为保存的就是流中的原
     * 始数据，所以 OutputTag 的类型与流中数据类型相同
     *
     * 窗口关闭后    将迟到的数据放入侧输出流
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
        OutputTag<Event> outputTag = new OutputTag<Event>("late") {};

        SingleOutputStreamOperator<UrlViewCount> aggregate = stream.keyBy(data -> data.url).window(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5)))
                //将迟到的数据放入侧输出流
                .sideOutputLateData(outputTag)
                // 同时传入增量聚合函数和全窗口函数
                .aggregate(new UrlViewCountExample07.UrlViewCountAgg(), new UrlViewCountExample07.UrlViewCountResult());

        aggregate.getSideOutput(outputTag).print("late");
        env.execute();

    }
}
