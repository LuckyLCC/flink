package com.liuchang.chapter11;

import com.liuchang.pojo.LoginEvent;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.List;
import java.util.Map;

/**
 * @Description: 需要引入包
 * <dependency>
 *  <groupId>org.apache.flink</groupId>
 *  <artifactId>flink-cep_${scala.binary.version}</artifactId>
 *  <version>${flink.version}</version>
 * </dependency>
 * @Author: liuchang
 * @CreateTime: 2022-07-26  13:41
 */
public class LoginFailDetect01 {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // 获取登录事件流，并提取时间戳、生成水位线
        KeyedStream<LoginEvent, String> stream = env.fromElements(
                new LoginEvent("user_1", "192.168.0.1", "fail", 2000L),
                new LoginEvent("user_1", "192.168.0.2", "fail", 3000L),
                new LoginEvent("user_2", "192.168.1.29", "fail", 4000L),
                new LoginEvent("user_1", "171.56.23.10", "fail", 5000L),
                new LoginEvent("user_2", "192.168.1.29", "success", 6000L),
                new LoginEvent("user_2", "192.168.1.29", "fail", 7000L),
                new LoginEvent("user_2", "192.168.1.29", "fail", 8000L))
                .assignTimestampsAndWatermarks(WatermarkStrategy.<LoginEvent>forMonotonousTimestamps().withTimestampAssigner((SerializableTimestampAssigner<LoginEvent>) (loginEvent, l) -> loginEvent.timestamp)).keyBy(r -> r.userId);
        // 1. 定义 Pattern，连续的三个登录失败事件
        Pattern<LoginEvent, LoginEvent> pattern = Pattern.<LoginEvent>begin("first") // 以第一个登录失败事件开始
                .where(new SimpleCondition<LoginEvent>() {
                    @Override
                    public boolean filter(LoginEvent loginEvent) throws Exception {
                        return loginEvent.eventType.equals("fail");
                    }
                }).next("second") // 接着是第二个登录失败事件
                .where(new SimpleCondition<LoginEvent>() {
                    @Override
                    public boolean filter(LoginEvent loginEvent) throws Exception {
                        return loginEvent.eventType.equals("fail");
                    }
                }).next("third") // 接着是第三个登录失败事件
                .where(new SimpleCondition<LoginEvent>() {
                    @Override
                    public boolean filter(LoginEvent loginEvent) throws Exception {
                        return loginEvent.eventType.equals("fail");
                    }
                });

//        extracted();

        // 2. 将 Pattern 应用到流上，检测匹配的复杂事件，得到一个 PatternStream
        PatternStream<LoginEvent> patternStream = CEP.pattern(stream, pattern);
        // 3. 将匹配到的复杂事件选择出来，然后包装成字符串报警信息输出
        SingleOutputStreamOperator<String> select = patternStream.select((PatternSelectFunction<LoginEvent, String>) map -> {
            LoginEvent first = map.get("first").get(0);
            LoginEvent second = map.get("second").get(0);
            LoginEvent third = map.get("third").get(0);
            return first.userId + " 连续三次登录失败！登录时间：" + first.timestamp + ", " + second.timestamp + ", " + third.timestamp;
        });
        select.print("warning");
        env.execute();
    }



    /**
     *
     * @Description:
     *  // 1. 定义 Pattern，登录失败事件，循环检测 3 次,
     *         //等同于上面，使用了times（）量词，量词属于宽松近邻关系，但使用了consecutive（）后就变为严格近邻关系
     * @Author: liuchang
     * @Date: 2022/7/26 15:26
     * @Param: []
     * @Return: void
     **/
    private static void extracted() {
        Pattern<LoginEvent, LoginEvent> pattern2 = Pattern
                .<LoginEvent>begin("fails")
                .where(new SimpleCondition<LoginEvent>() {
                    @Override
                    public boolean filter(LoginEvent loginEvent) throws Exception {
                        return loginEvent.eventType.equals("fail");
                    }
                }).times(3).consecutive();
    }
}
