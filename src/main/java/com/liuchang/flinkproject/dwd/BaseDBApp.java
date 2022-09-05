package com.liuchang.flinkproject.dwd;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.liuchang.flinkproject.utils.MyKafkaUtil;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

/**
 * Author: Felix
 * Date: 2021/7/31
 * Desc: 业务数据动态分流
 */
public class BaseDBApp {
    public static void main(String[] args) throws Exception {
        //TODO 1.基本环境准备
        //1.1 流处理环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //1.2 设置并行度
        env.setParallelism(4);
        /*
        //TODO 2.检查点设置
        //2.1 开启检查点
        env.enableCheckpointing(5000L, CheckpointingMode.EXACTLY_ONCE);
        //2.2 设置检查点超时时间
        env.getCheckpointConfig().setCheckpointTimeout(60000L);
        //2.3 设置重启策略
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3,3000L));
        //2.4 设置job取消后，检查点是否保留
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        //2.5 设置状态后端   内存|文件系统|RocksDB
        env.setStateBackend(new FsStateBackend("xxx"));
        //2.6 指定操作HDFS的用户
        System.setProperty("HADOOP_USER_NAME","atguigu");
        */
        //TODO 3.从Kafka中读取数据
        //3.1 声明消费主题以及消费者组
        String topic = "ods_base_db_m";
        String groupId = "base_db_app_group";

        //3.2 获取消费者对象
        FlinkKafkaConsumer<String> kafkaSource = MyKafkaUtil.getKafkaSource(topic, groupId);

        //3.3 读取数据  封装流
        DataStreamSource<String> kafkaDS = env.addSource(kafkaSource);

        //TODO 4.对数据类型进行转换  String->JSONObject
        SingleOutputStreamOperator<JSONObject> jsonObjDS = kafkaDS.map(JSON::parseObject);

        //TODO 5.简单的ETL
        SingleOutputStreamOperator<JSONObject> filterDS = jsonObjDS.filter(
            new FilterFunction<JSONObject>() {
                @Override
                public boolean filter(JSONObject jsonObj) throws Exception {
                    boolean flag =
                        jsonObj.getString("table") != null
                            && jsonObj.getString("table").length() > 0
                            && jsonObj.getJSONObject("data") != null
                            && jsonObj.getString("data").length() > 3;
                    return flag;
                }
            }
        );

        filterDS.print(">>>>");

        //TODO 6.动态分流  ****

        //TODO 7.将维度侧输出流的数据写到Hbase中

        //TODO 8.将主流数据写回到Kafka的dwd层


        env.execute();
    }
}