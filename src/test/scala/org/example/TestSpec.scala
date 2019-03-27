package org.example

import org.apache.flink.api.scala.ExecutionEnvironment
import org.scalatest.{FlatSpec, Matchers}
import org.apache.flink.configuration.{Configuration, GlobalConfiguration}
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.slf4j.{Logger, LoggerFactory}

abstract class TestSpec extends FlatSpec with Matchers {
  val log: Logger = LoggerFactory.getLogger(getClass)

  def loadConfiguration(): Configuration = {
    GlobalConfiguration.loadConfiguration()
  }

  /**
    * Flink [DataSet API](https://ci.apache.org/projects/flink/flink-docs-release-1.7/dev/batch/index.html)
    * for bounded data sets
    */
  def withDataSetEnv(f: ExecutionEnvironment => Unit): Unit = {
    log.info("Testing withDataSetEnv")
    val env = ExecutionEnvironment.createLocalEnvironment(loadConfiguration())
    f(env)
  }

  /**
    * Flink [DataStream API](https://ci.apache.org/projects/flink/flink-docs-release-1.7/dev/datastream_api.html#flink-datastream-api-programming-guide),
    * for bounded or unbounded streams of data
    */
  def withDataStreamEnv(f: StreamExecutionEnvironment => Unit): Unit = {
    log.info("Testing withDataStreamEnv, num-cpu={}", StreamExecutionEnvironment.getDefaultLocalParallelism)
    val env = StreamExecutionEnvironment.createLocalEnvironment(StreamExecutionEnvironment.getDefaultLocalParallelism, loadConfiguration())
    f(env)
  }
}
