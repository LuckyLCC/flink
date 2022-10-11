package com.liuchang.window;

import com.liuchang.pojo.ClickSource;
import com.liuchang.pojo.Event;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.GlobalWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class CountWindow02 {

    /***
     * 计数窗口
     *
     *  计数窗口概念非常简单，本身底层是基于全局窗口（Global Window）实现的。Flink 为我
     * 们提供了非常方便的接口：直接调用.countWindow()方法。根据分配规则的不同，又可以分为
     * 滚动计数窗口和滑动计数窗口两类，下面我们就来看它们的具体实现。
     * @param args
     * @return void
     * @author: liuchang
     * @date: 2022/7/14
     */
    public static void main(String[] args) throws Exception {

        //滚动计数窗口:我们定义了一个长度为 10 的滚动计数窗口，当窗口中元素数量达到 10 的时候，就会触发计算执行并关闭窗口。
        countWindow();


        //滑动计数窗口:与滚动计数窗口类似，不过需要在.countWindow()调用时传入两个参数：size 和 slide，前者表示窗口大小，后者表示滑动步长
        slideExtracted();


    }

    private static void slideExtracted() {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // 这里的 ClickSource()使用了之前自定义数据源小节中的 ClickSource()
        WindowedStream<Event, String, GlobalWindow> window = env.addSource(new ClickSource())
                .keyBy(event -> event.getUser())

                .countWindow(10,3);

    }

    private static void countWindow() {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // 这里的 ClickSource()使用了之前自定义数据源小节中的 ClickSource()
        WindowedStream<Event, String, GlobalWindow> window = env.addSource(new ClickSource())
                .keyBy(event -> event.getUser())

                .countWindow(10);
    }
}
