package com.hello.world.java.sql;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.lang.reflect.Field;
import java.util.HashMap;

public class Demo {
    public static void main(String[] args) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        map.put("execution.checkpointing.interval", "600s");
        map.put("rest.bind-port", "8081");
        Configuration configuration = Configuration.fromMap(map);
        LocalStreamEnvironment env = StreamExecutionEnvironment.createLocalEnvironment(configuration);


//        Field field = Class.forName("org.apache.flink.streaming.api.environment.StreamExecutionEnvironment")
//                .getDeclaredField("configuration");
//        field.setAccessible(true);
//        org.apache.flink.configuration.Configuration conf = (Configuration) field.get(env);
//        conf.setString("rest.bind-port", "8080");

        StreamTableEnvironment tbEnv = StreamTableEnvironment.create(env,
                EnvironmentSettings.newInstance().inStreamingMode().build());

        tbEnv.executeSql("CREATE TABLE ss (\n" +
                "    order_number BIGINT,\n" +
                "    price        DECIMAL(32,2),\n" +
                "    buyer        ROW<first_name STRING, last_name STRING>,\n" +
                "    order_time   TIMESTAMP(3)\n" +
                ") WITH (\n" +
                "  'connector' = 'datagen'\n" +
                ")");

        tbEnv.executeSql("select * from ss").print();
    }
}
