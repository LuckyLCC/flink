package com.liuchang.window;

import com.liuchang.pojo.ClickSource;
import com.liuchang.pojo.Event;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.GlobalWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

public class CountWindow02 {

    /***
     * 计数窗口
     *
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

        //全局窗口:需要注意使用全局窗口，必须自行定义触发器才能实现窗口计算，否则起不到任何作用。
        globalExtracted();

    }

    private static void globalExtracted() {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // 这里的 ClickSource()使用了之前自定义数据源小节中的 ClickSource()
        WindowedStream<Event, String, GlobalWindow> window = env.addSource(new ClickSource())
                .keyBy(event -> event.getUser())
                .window(GlobalWindows.create());
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
