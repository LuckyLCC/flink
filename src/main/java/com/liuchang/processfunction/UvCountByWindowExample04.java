package com.liuchang.processfunction;

import com.liuchang.pojo.ClickSource;
import com.liuchang.pojo.Event;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.HashSet;

public class UvCountByWindowExample04 {

    /***
     *进行窗口计算，我们可以直接调用现成的简单聚合方法（sum/max/min）,也可以通过调
     * 用.reduce()或.aggregate()来自定义一般的增量聚合函数（ReduceFunction/AggregateFucntion）；
     * 而对于更加复杂、需要窗口信息和额外状态的一些场景，我们还可以直接使用全窗口函数、把
     * 数据全部收集保存在窗口内，等到触发窗口计算时再统一处理。窗口处理函数就是一种典型的
     * 全窗口函数。
     *
     * 窗口处理函数的使用
     * @param
     * @return
     * @author: liuchang
     * @date: 2022/7/14
     */
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        SingleOutputStreamOperator<Event> stream = env.addSource(new ClickSource()).assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO).withTimestampAssigner((SerializableTimestampAssigner<Event>) (element, recordTimestamp) -> element.timestamp));

        // 将数据全部发往同一分区，按窗口统计 UV
        stream.keyBy(data -> true)
                .window(TumblingEventTimeWindows.of(Time.seconds(10)))
                .process(new UvCountByWindow()).print();
        env.execute();
    }

    // 自定义窗口处理函数
    public static class UvCountByWindow extends ProcessWindowFunction<Event, String, Boolean, TimeWindow> {
        @Override
        public void process(Boolean aBoolean, Context context, Iterable<Event> elements, Collector<String> out) throws Exception {
            HashSet<String> userSet = new HashSet<>();
            // 遍历所有数据，放到 Set 里去重
            for (Event event : elements) {
                userSet.add(event.user);
            }
            // 结合窗口信息，包装输出内容
            Long start = context.window().getStart();
            Long end = context.window().getEnd();
            out.collect(" 窗 口 : " + new Timestamp(start) + " ~ " + new Timestamp(end) + " 的独立访客数量是：" + userSet.size());
        }
    }
}
