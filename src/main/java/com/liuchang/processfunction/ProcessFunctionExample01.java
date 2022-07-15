package com.liuchang.processfunction;

import com.liuchang.pojo.ClickSource;
import com.liuchang.pojo.Event;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;




public class ProcessFunctionExample01 {





    /***
     *基本处理函数（ProcessFunction）
     *
     *  我们之前学习的转换算子，一般只是针对某种具体操作来定义的，能够拿到的信息比较有
     * 限。比如 map 算子，我们实现的 MapFunction 中，只能获取到当前的数据，定义它转换之后
     * 的形式；而像窗口聚合这样的复杂操作，AggregateFunction 中除数据外，还可以获取到当前的
     * 183
     * 状态（以累加器 Accumulator 形式出现）。另外我们还介绍过富函数类，比如 RichMapFunction，
     * 它提供了获取运行时上下文的方法 getRuntimeContext()，可以拿到状态，还有并行度、任务名
     * 称之类的运行时信息。
     * 但是无论那种算子，如果我们想要访问事件的时间戳，或者当前的水位线信息，都是完全
     * 做不到的。在定义生成规则之后，水位线会源源不断地产生，像数据一样在任务间流动，可我
     * 们却不能像数据一样去处理它；跟时间相关的操作，目前我们只会用窗口来处理。而在很多应
     * 用需求中，要求我们对时间有更精细的控制，需要能够获取水位线，甚至要“把控时间”、定义
     * 什么时候做什么事，这就不是基本的时间窗口能够实现的了。
     * 于是必须祭出大招——处理函数（ProcessFunction）了。处理函数提供了一个“定时服务”
     * （TimerService），我们可以通过它访问流中的事件（event）、时间戳（timestamp）、水位线
     * （watermark），甚至可以注册“定时事件”。而且处理函数继承了 AbstractRichFunction 抽象类，
     * 所以拥有富函数类的所有特性，同样可以访问状态（state）和其他运行时信息。此外，处理函
     * 数还可以直接将数据输出到侧输出流（side output）中。所以，处理函数是最为灵活的处理方
     * 法，可以实现各种自定义的业务逻辑；同时也是整个 DataStream API 的底层基础。
     * 处理函数的使用与基本的转换操作类似，只需要直接基于 DataStream 调用.process()方法
     * 就可以了。方法需要传入一个 ProcessFunction 作为参数，用来定义处理逻辑。
     *
     *  抽象方法.processElement()
     * 用于“处理元素”，定义了处理的核心逻辑。这个方法对于流中的每个元素都会调用一次，
     * 参数包括三个：输入数据值 value，上下文 ctx，以及“收集器”（Collector）out。方法没有返
     * 回值，处理之后的输出数据是通过收集器 out 来定义的。
     * @param args
     * @return void
     * @author: liuchang
     * @date: 2022/7/15
     */

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.addSource(new ClickSource())
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forMonotonousTimestamps()
                                .withTimestampAssigner((SerializableTimestampAssigner<Event>) (event, l) -> event.timestamp)
                )
                .process(new ProcessFunction<Event, String>() {
                    @Override
                    public void processElement(Event value, Context ctx, Collector<String> out) throws Exception {
                        if (value.user.equals("Mary")) {
                            out.collect(value.user);
                        } else if (value.user.equals("Bob")) {
                            out.collect(value.user);
                            out.collect(value.user);
                        }
                        System.out.println(ctx.timerService().currentWatermark());
                    }
                })
                .print();
        env.execute();
    }
}
