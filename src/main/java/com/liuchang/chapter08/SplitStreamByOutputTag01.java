package com.liuchang.chapter08;

import com.liuchang.pojo.ClickSource;
import com.liuchang.pojo.Event;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;


/***
 *
 * 使用侧输出流来分流
 *
 * 在 Flink 1.13 版本中，已经弃用了.split()方法，取而代之的是直接用处理函数（process
 * function）的侧输出流（side output）。
 * 我们知道，处理函数本身可以认为是一个转换算子，它的输出类型是单一的，处理之后得
 * 到的仍然是一个 DataStream；而侧输出流则不受限制，可以任意自定义输出数据，它们就像从
 * “主流”上分叉出的“支流”。尽管看起来主流和支流有所区别，不过实际上它们都是某种类型
 * 的 DataStream，所以本质上还是平等的。利用侧输出流就可以很方便地实现分流操作，而且得
 * 到的多条 DataStream 类型可以不同，这就给我们的应用带来了极大的便利。
 * 关于处理函数中侧输出流的用法，我们已经在 7.5 节做了详细介绍。简单来说，只需要调
 * 用上下文 ctx 的.output()方法，就可以输出任意类型的数据了。而侧输出流的标记和提取，都
 * 离不开一个“输出标签”（OutputTag），它就相当于 split()分流时的“戳”，指定了侧输出流的
 * id 和类型。
 *
 * @param
 * @return
 * @author: liuchang
 * @date: 2022/7/18
 */



public class SplitStreamByOutputTag01 {

    // 定义输出标签，侧输出流的数据类型为三元组(user, url, timestamp)
    private static OutputTag<Tuple3<String, String, Long>> MaryTag = new OutputTag<Tuple3<String, String, Long>>("Mary-pv") {};
    private static OutputTag<Tuple3<String, String, Long>> BobTag = new OutputTag<Tuple3<String, String, Long>>("Bob-pv") {};

    
    /**
     *
     * @Description: 使用侧输出流来分流
     * @Author: liuchang
     * @Date: 2022/7/18 10:20
     * @Param: [args]
     * @Return: void
     **/
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        SingleOutputStreamOperator<Event> stream = env.addSource(new ClickSource());
        SingleOutputStreamOperator<Event> processedStream = stream.process(new ProcessFunction<Event, Event>() {
            @Override
            public void processElement(Event value, Context ctx, Collector<Event> out) throws Exception {
                if (value.user.equals("Mary")) {
                    ctx.output(MaryTag, new Tuple3<>(value.user, value.url, value.timestamp));
                } else if (value.user.equals("Bob")) {
                    ctx.output(BobTag, new Tuple3<>(value.user, value.url, value.timestamp));
                } else {
                    out.collect(value);
                }
            }
        });
        processedStream.getSideOutput(MaryTag).print("Mary pv");
        processedStream.getSideOutput(BobTag).print("Bob pv");
        processedStream.print("else");
        env.execute();
    }
}
