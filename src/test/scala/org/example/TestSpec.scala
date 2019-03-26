package org.example

import org.scalatest.{FlatSpec, Matchers}
import org.apache.flink.api.scala._
import org.slf4j.{Logger, LoggerFactory}

abstract class TestSpec extends FlatSpec with Matchers {
  val log: Logger = LoggerFactory.getLogger(getClass)
  def withExecutionEnvironment(f: ExecutionEnvironment => Unit): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    f(env)
  }
}
