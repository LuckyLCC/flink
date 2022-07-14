package com.liuchang.window;

import com.liuchang.pojo.ClickSource;
import com.liuchang.pojo.Event;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.*;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

public class TimeWindow01 {

    /***
     * 时间窗口
     *
     * @param args
     * @return void
     * @author: liuchang
     * @date: 2022/7/14
     */
    public static void main(String[] args) throws Exception {
        //处理时间窗口-滚动
        TumblingProcessingTimeWindowsExtracted();

        //处理时间窗口-滑动
        SlidingProcessingTimeWindowsExtracted();

        //处理时间窗口-会话
        ProcessingTimeSessionWindowsExtracted();

        //事件时间窗口-滚动
        TumblingEventTimeWindowsExtracted();

        //事件时间窗口-滑动
        SlidingEventTimeWindowsExtracted();

        //事件时间窗口-会话
        EventTimeSessionWindowsExtracted();

    }
    /***
     * 事件时间窗口-会话
     *
     * @return void
     * @author: liuchang
     * @date: 2022/7/14
     */
    private static void EventTimeSessionWindowsExtracted() {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // 这里的 ClickSource()使用了之前自定义数据源小节中的 ClickSource()
        WindowedStream<Event, String, TimeWindow> window = env.addSource(new ClickSource())
                .keyBy(event -> event.getUser())
                //这里.withGap()方法需要传入一个 Time 类型的参数 size，表示会话的超时时间，也就是最
                //小间隔 session gap。我们这里创建了静态会话超时时间为 10 秒的会话窗口。
                .window((EventTimeSessionWindows.withGap(Time.seconds(10))));
    }
    /***
     * 事件时间窗口-滑动
     *
     * @return void
     * @author: liuchang
     * @date: 2022/7/14
     */
    private static void SlidingEventTimeWindowsExtracted() {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // 这里的 ClickSource()使用了之前自定义数据源小节中的 ClickSource()
        WindowedStream<Event, String, TimeWindow> window = env.addSource(new ClickSource())
                .keyBy(event -> event.getUser())
                //这里.of()方法需要传入两个 Time 类型的参数：size 和 slide，前者表示滑动窗口的大小，
                //后者表示滑动窗口的滑动步长。我们这里创建了一个长度为 10 秒、滑动步长为 5 秒的滑动窗
                //口。
                //滑动窗口同样可以追加第三个参数，用于指定窗口起始点的偏移量，用法与滚动窗口完全
                //一致。
                .window((SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5))));
    }

    /***
     * 事件时间窗口-滚动
     *
     * @return void
     * @author: liuchang
     * @date: 2022/7/14
     */
    private static void TumblingEventTimeWindowsExtracted() {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // 这里的 ClickSource()使用了之前自定义数据源小节中的 ClickSource()
        WindowedStream<Event, String, TimeWindow> window = env.addSource(new ClickSource())
                .keyBy(event -> event.getUser())
                //这里.of()方法需要传入一个 Time 类型的参数 size，表示滚动窗口的大小，我们这里创建了一个长度为 5 秒的滚动窗口。
                .window((TumblingEventTimeWindows.of(Time.seconds(5))));
    }

    /***
     *
     * 处理时间窗口-会话
     * @return void
     * @author: liuchang
     * @date: 2022/7/14
     */

    private static void ProcessingTimeSessionWindowsExtracted() {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // 这里的 ClickSource()使用了之前自定义数据源小节中的 ClickSource()
        WindowedStream<Event, String, TimeWindow> window = env.addSource(new ClickSource())
                .keyBy(event -> event.getUser())
                //这里.withGap()方法需要传入一个 Time 类型的参数 size，表示会话的超时时间，也就是最
                //小间隔 session gap。我们这里创建了静态会话超时时间为 10 秒的会话窗口。
                .window((ProcessingTimeSessionWindows.withGap(Time.seconds(10))));
    }

    /***
     *
     * 处理时间窗口-滑动
     * @return void
     * @author: liuchang
     * @date: 2022/7/14
     */
    private static void SlidingProcessingTimeWindowsExtracted() {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // 这里的 ClickSource()使用了之前自定义数据源小节中的 ClickSource()
        WindowedStream<Event, String, TimeWindow> window = env.addSource(new ClickSource())
                .keyBy(event -> event.getUser())
                //这里.of()方法需要传入两个 Time 类型的参数：size 和 slide，前者表示滑动窗口的大小，
                //后者表示滑动窗口的滑动步长。我们这里创建了一个长度为 10 秒、滑动步长为 5 秒的滑动窗
                //口。
                //滑动窗口同样可以追加第三个参数，用于指定窗口起始点的偏移量，用法与滚动窗口完全
                //一致。
                .window((SlidingProcessingTimeWindows.of(Time.seconds(10), Time.seconds(5))));
    }
    /***
     *
     * 处理时间窗口-滚动
     * @return void
     * @author: liuchang
     * @date: 2022/7/14
     */
    private static void TumblingProcessingTimeWindowsExtracted() {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // 这里的 ClickSource()使用了之前自定义数据源小节中的 ClickSource()
        WindowedStream<Event, String, TimeWindow> window = env.addSource(new ClickSource())
                .keyBy(event -> event.getUser())
                //这里.of()方法需要传入一个 Time 类型的参数 size，表示滚动窗口的大小，我们这里创建了一个长度为 5 秒的滚动窗口。
                .window((TumblingProcessingTimeWindows.of(Time.seconds(5))));
    }
}
