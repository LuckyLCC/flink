package com.liuchang.basestart;

import com.liuchang.pojo.ClickSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TransReduceTest11 {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // 这里的 ClickSource()使用了之前自定义数据源小节中的 ClickSource()
        env.addSource(new ClickSource())
                // 将 Event 数据类型转换成元组类型
                .map(e -> Tuple2.of(e.user, 1L))
                // 使用用户名来进行分流
                .keyBy(r -> r.f0)
                .reduce((value1, value2) -> {
                    // 每到一条数据，用户 pv 的统计值加 1
                    return Tuple2.of(value1.f0, value1.f1 + value2.f1);
                })
                // 为每一条数据分配同一个 key，将聚合结果发送到一条流中去
                .keyBy(r -> true)
                .reduce( (value1, value2) -> {
                    // 将累加器更新为当前最大的 pv 统计值，然后向下游发送累加器的值
                    return value1.f1 > value2.f1 ? value1 : value2;
                })
                .print();
        env.execute();


    }
}
