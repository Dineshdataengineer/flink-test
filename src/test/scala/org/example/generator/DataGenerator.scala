package org.example.generator

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.functions.source.{RichParallelSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

import scala.compat.Platform
import scala.util.Random

object DataGenerator {
  def randomIntegers(env: StreamExecutionEnvironment): DataStream[Integer] = {
    env.addSource(new RichParallelSourceFunction[Integer]() {
      private var running = true
      override def run(ctx: SourceFunction.SourceContext[Integer]): Unit = {
        while(running) {
          Thread.sleep(500)
          ctx.collect(Random.nextInt(1000))
        }
      }
      override def cancel(): Unit = {
        running = false
      }
    })
  }

  def randomSensorData(env: StreamExecutionEnvironment): DataStream[String] = {
    env.addSource(new RichParallelSourceFunction[String]() {
      private var running = true
      override def run(ctx: SourceFunction.SourceContext[String]): Unit = {
        while(running) {
          Thread.sleep(500)
          val data = SensorGenerator.iter.next()
          ctx.collectWithTimestamp(data.productIterator.map(_.toString).mkString(","), Platform.currentTime)
        }
      }
      override def cancel(): Unit = {
        running = false
      }
    })
  }
}
