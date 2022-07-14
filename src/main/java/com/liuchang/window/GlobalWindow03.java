package com.liuchang.window;

import com.liuchang.pojo.ClickSource;
import com.liuchang.pojo.Event;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.GlobalWindows;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;

public class GlobalWindow03 {

    /***
     * 全局窗口:需要注意使用全局窗口，必须自行定义触发器才能实现窗口计算，否则起不到任何作用。
     *
     * @param args
     * @return void
     * @author: liuchang
     * @date: 2022/7/14
     */
    public static void main(String[] args) throws Exception {


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
}
