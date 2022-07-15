package com.liuchang.window;

import com.liuchang.pojo.ClickSource;
import com.liuchang.pojo.Event;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

public class AllowedLatenessTest10 {


    /***
     *  在事件时间语义下，窗口中可能会出现数据迟到的情况。这是因为在乱序流中，水位线
     * （watermark）并不一定能保证时间戳更早的所有数据不会再来。当水位线已经到达窗口结束时
     * 间时，窗口会触发计算并输出结果，这时一般也就要销毁窗口了；如果窗口关闭之后，又有本
     * 属于窗口内的数据姗姗来迟，默认情况下就会被丢弃。这也很好理解：窗口触发计算就像发车，
     * 如果要赶的车已经开走了，又不能坐其他的车（保证分配窗口的正确性），那就只好放弃坐班
     * 车了。
     * 不过在多数情况下，直接丢弃数据也会导致统计结果不准确，我们还是希望该上车的人都
     * 能上来。为了解决迟到数据的问题，Flink 提供了一个特殊的接口，可以为窗口算子设置一个
     * “允许的最大延迟”（Allowed Lateness）。也就是说，我们可以设定允许延迟一段时间，在这段
     * 时间内，窗口不会销毁，继续到来的数据依然可以进入窗口中并触发计算。直到水位线推进到
     * 了 窗口结束时间 + 延迟时间，才真正将窗口的内容清空，正式关闭窗口。
     * 基于 WindowedStream 调用.allowedLateness()方法，传入一个 Time 类型的延迟时间，就可
     * 以表示允许这段时间内的延迟数据。
     *
     * @param args
     * @return void
     * @author: liuchang
     * @date: 2022/7/15
     */

    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        SingleOutputStreamOperator<Event> stream = env.addSource(new ClickSource()).assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forMonotonousTimestamps().withTimestampAssigner((SerializableTimestampAssigner<Event>) (element, recordTimestamp) -> element.timestamp));

        stream.keyBy(data -> data.url).window(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5)))
                //将迟到的数据放入侧输出流
                .allowedLateness(Time.minutes(1))
                // 同时传入增量聚合函数和全窗口函数
                .aggregate(new UrlViewCountExample07.UrlViewCountAgg(), new UrlViewCountExample07.UrlViewCountResult());
    }
}
