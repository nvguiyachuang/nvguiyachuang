package com.hello.world.sql2

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
import org.apache.flink.types.Row
import org.apache.flink.util.CloseableIterator

object Demo9 {
  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val settings = EnvironmentSettings.newInstance().inStreamingMode().build()
    val tableEnv: StreamTableEnvironment = StreamTableEnvironment.create(env, settings)
    env.setParallelism(1)

    tableEnv.executeSql(
      """
        |CREATE TABLE ss (
        |    order_number BIGINT,
        |    price        DECIMAL(32,2),
        |    buyer        ROW<first_name STRING, last_name STRING>,
        |    order_time   TIMESTAMP(3)
        |) WITH (
        |  'connector' = 'datagen'
        |)
        |""".stripMargin)

    val result = tableEnv.executeSql(
      """
        |select * from ss
        |""".stripMargin)

//    println(result.getResultKind)
//    println(result.getJobClient.get())
//    println(result.getResolvedSchema)

    val value: CloseableIterator[Row] = result.collect()
    println(value)

    result.print()
  }
}
