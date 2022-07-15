package com.liuchang.processfunction;

import com.liuchang.pojo.ClickSource;
import com.liuchang.pojo.Event;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class ProcessAllWindowTopN05 {


    /***
     * 使用 ProcessAllWindowFunction
     *
     *  至于另一种窗口处理函数 ProcessAllWindowFunction，它的用法非常类似。区别在于它基
     * 于的是 AllWindowedStream，相当于对没有 keyBy 的数据流直接开窗并调用.process()方法:
     *
     *  窗口的计算处理，在实际应用中非常常见。对于一些比较复杂的需求，如果增量聚合函数
     * 无法满足，我们就需要考虑使用窗口处理函数这样的“大招”了。
     * 网站中一个非常经典的例子，就是实时统计一段时间内的热门 url。例如，需要统计最近
     * 10 秒钟内最热门的两个 url 链接，并且每 5 秒钟更新一次。我们知道，这可以用一个滑动窗口
     * 来实现，而“热门度”一般可以直接用访问量来表示。于是就需要开滑动窗口收集 url 的访问
     * 数据，按照不同的 url 进行统计，而后汇总排序并最终输出前两名。这其实就是著名的“Top N”
     * 问题。
     * 很显然，简单的增量聚合可以得到 url 链接的访问量，但是后续的排序输出 Top N 就很难
     * 实现了。所以接下来我们用窗口处理函数进行实现。
     *
     * 一种最简单的想法是，我们干脆不区分 url 链接，而是将所有访问数据都收集起来，统一
     * 进行统计计算。所以可以不做 keyBy，直接基于 DataStream 开窗，然后使用全窗口函数
     * ProcessAllWindowFunction 来进行处理。
     * 在窗口中可以用一个 HashMap 来保存每个 url 的访问次数，只要遍历窗口中的所有数据，
     * 自然就能得到所有 url 的热门度。最后把 HashMap 转成一个列表 ArrayList，然后进行排序、
     * 取出前两名输出就可以了。
     *
     * @param args
     * @return void
     * @author: liuchang
     * @date: 2022/7/15
     */
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<Event> eventStream = env.addSource(new ClickSource())
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forMonotonousTimestamps()
                                .withTimestampAssigner((SerializableTimestampAssigner<Event>) (element, recordTimestamp) -> element.timestamp));
        // 只需要 url 就可以统计数量，所以转换成 String 直接开窗统计
        SingleOutputStreamOperator<String> result = eventStream
                .map((MapFunction<Event, String>) value -> value.url)
                // 开滑动窗口
                .windowAll(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5)))
                .process(new ProcessAllWindowFunction<String, String, TimeWindow>() {
                    @Override
                    public void process(Context context, Iterable<String> elements, Collector<String> out) throws Exception {
                        HashMap<String, Long> urlCountMap = new HashMap<>();
                        // 遍历窗口中数据，将浏览量保存到一个 HashMap 中
                        for (String url : elements) {
                            if (urlCountMap.containsKey(url)) {
                                long count = urlCountMap.get(url);
                                urlCountMap.put(url, count + 1L);
                            } else {
                                urlCountMap.put(url, 1L);
                            }
                        }
                        ArrayList<Tuple2<String, Long>> mapList = new ArrayList<>();
                        // 将浏览量数据放入 ArrayList，进行排序
                        for (String key : urlCountMap.keySet()) {
                            mapList.add(Tuple2.of(key, urlCountMap.get(key)));
                        }
                        mapList.sort((o1, o2) -> o2.f1.intValue() - o1.f1.intValue());
                        // 取排序后的前两名，构建输出结果
                        StringBuilder result = new StringBuilder();

                        result.append("========================================\n");
                        for (int i = 0; i < 2; i++) {
                            Tuple2<String, Long> temp = mapList.get(i);
                            String info = "浏览量 No." + (i + 1) +
                                    " url：" + temp.f0 +
                                    " 浏览量：" + temp.f1 +
                                    " 窗 口 结 束 时 间 ： " + new
                                    Timestamp(context.window().getEnd()) + "\n";
                            result.append(info);
                        }

                        result.append("========================================\n");
                        out.collect(result.toString());
                    }
                });

        result.print();
        env.execute();
    }
}
