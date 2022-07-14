package com.liuchang.window;

import com.liuchang.pojo.ClickSource;
import com.liuchang.pojo.Event;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class UrlViewCountExample07 {


    /***
     *我们已经了解了 Window API 中两类窗口函数的用法，下面我们先来做个简单的总结。
     * 增量聚合函数处理计算会更高效。举一个最简单的例子，对一组数据求和。大量的数据连
     * 续不断到来，全窗口函数只是把它们收集缓存起来，并没有处理；到了窗口要关闭、输出结果
     * 的时候，再遍历所有数据依次叠加，得到最终结果。而如果我们采用增量聚合的方式，那么只
     * 需要保存一个当前和的状态，每个数据到来时就会做一次加法，更新状态；到了要输出结果的
     * 时候，只要将当前状态直接拿出来就可以了。增量聚合相当于把计算量“均摊”到了窗口收集
     * 数据的过程中，自然就会比全窗口聚合更加高效、输出更加实时。
     * 而全窗口函数的优势在于提供了更多的信息，可以认为是更加“通用”的窗口操作。它只
     * 负责收集数据、提供上下文相关信息，把所有的原材料都准备好，至于拿来做什么我们完全可
     * 以任意发挥。这就使得窗口计算更加灵活，功能更加强大。
     * 所以在实际应用中，我们往往希望兼具这两者的优点，把它们结合在一起使用。Flink 的
     * Window API 就给我们实现了这样的用法。
     * 我们之前在调用 WindowedStream 的.reduce()和.aggregate()方法时，只是简单地直接传入
     * 了一个 ReduceFunction 或 AggregateFunction 进行增量聚合。除此之外，其实还可以传入第二
     * 个参数：一个全窗口函数，可以是 WindowFunction 或者 ProcessWindowFunction
     *
     *
     *  这样调用的处理机制是：基于第一个参数（增量聚合函数）来处理窗口数据，每来一个数
     * 据就做一次聚合；等到窗口需要触发计算时，则调用第二个参数（全窗口函数）的处理逻辑输
     * 出结果。需要注意的是，这里的全窗口函数就不再缓存所有数据了，而是直接将增量聚合函数
     * 的结果拿来当作了 Iterable 类型的输入。一般情况下，这时的可迭代集合中就只有一个元素了。
     *
     * @param args
     * @return void
     * @author: liuchang
     * @date: 2022/7/14
     */

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        SingleOutputStreamOperator<Event> stream = env.addSource(new ClickSource())
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forMonotonousTimestamps()
                        .withTimestampAssigner((SerializableTimestampAssigner<Event>) (element, recordTimestamp) -> element.timestamp));
        // 需要按照 url 分组，开滑动窗口统计
        stream.keyBy(data -> data.url)
                .window(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5)))
                // 同时传入增量聚合函数和全窗口函数
                .aggregate(new UrlViewCountAgg(), new UrlViewCountResult())
                .print();
        env.execute();
    }

    // 自定义增量聚合函数，来一条数据就加一
    public static class UrlViewCountAgg implements AggregateFunction<Event, Long, Long> {
        @Override
        public Long createAccumulator() {
            return 0L;
        }

        @Override
        public Long add(Event value, Long accumulator) {
            return accumulator + 1;
        }

        @Override
        public Long getResult(Long accumulator) {
            return accumulator;
        }

        @Override
        public Long merge(Long a, Long b) {
            return null;
        }
    }

    // 自定义窗口处理函数，只需要包装窗口信息
    public static class UrlViewCountResult extends ProcessWindowFunction<Long, UrlViewCount, String, TimeWindow> {
        @Override
        public void process(String url, Context context, Iterable<Long> elements, Collector<UrlViewCount> out) throws Exception {
            // 结合窗口信息，包装输出内容
            Long start = context.window().getStart();
            Long end = context.window().getEnd();
            // 迭代器中只有一个元素，就是增量聚合函数的计算结果
            out.collect(new UrlViewCount(url, elements.iterator().next(), start, end));
        }
    }
}
