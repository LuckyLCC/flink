package com.liuchang.chapter08;

import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;

/**
 * @Description: 流的联合虽然简单，不过受限于数据类型不能改变，灵活性大打折扣，所以实际应用较少
 * 出现。除了联合（union），Flink 还提供了另外一种方便的合流操作——连接（connect）。顾名
 * 思义，这种操作就是直接把两条流像接线一样对接起来。
 * 1. 连接流（ConnectedStreams）
 * 为了处理更加灵活，连接操作允许流的数据类型不同。但我们知道一个 DataStream 中的
 * 数据只能有唯一的类型，所以连接得到的并不是 DataStream，而是一个“连接流”
 * （ConnectedStreams）。连接流可以看成是两条流形式上的“统一”，被放在了一个同一个流中；
 * 事实上内部仍保持各自的数据形式不变，彼此之间是相互独立的。要想得到新的 DataStream，
 * 还需要进一步定义一个“同处理”（co-process）转换操作，用来说明对于不同来源、不同类型
 * 的数据，怎样分别进行处理转换、得到统一的输出类型。所以整体上来，两条流的连接就像是
 * “一国两制”，两条流可以保持各自的数据类型、处理方式也可以不同，不过最终还是会统一到
 * 同一个 DataStream 中，
 * @Author: liuchang
 * @CreateTime: 2022-07-18  10:33
 */
public class CoMapExample03 {



    /**
     *
     * @Description: ConnectedStreams 有两个类型参数，分别表示内部包含的两条流各自的数
     * 据类型；由于需要“一国两制”，因此调用.map()方法时传入的不再是一个简单的 MapFunction，
     * 而是一个 CoMapFunction，表示分别对两条流中的数据执行 map 操作。这个接口有三个类型
     * 参数，依次表示第一条流、第二条流，以及合并后的流中的数据类型。需要实现的方法也非常
     * 直白：.map1()就是对第一条流中数据的 map 操作，.map2()则是针对第二条流。这里我们将一
     * 条 Integer 流和一条 Long 流合并，转换成 String 输出。所以当遇到第一条流输入的整型值时，
     * 调用.map1()；而遇到第二条流输入的长整型数据时，调用.map2():最终都转换为字符串输出，
     * 合并成了一条字符串流。
     * @Author: liuchang
     * @Date: 2022/7/18 10:36
     * @Param: [args]
     * @Return: void
     **/
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStream<Integer> stream1 = env.fromElements(1, 2, 3);
        DataStream<Long> stream2 = env.fromElements(1L, 2L, 3L);
        ConnectedStreams<Integer, Long> connectedStreams = stream1.connect(stream2);
        SingleOutputStreamOperator<String> result = connectedStreams.map(new CoMapFunction<Integer, Long, String>() {
            @Override
            public String map1(Integer value) {
                return "Integer: " + value;
            }

            @Override
            public String map2(Long value) {
                return "Long: " + value;
            }
        });
        result.print();
        env.execute();
    }
}
