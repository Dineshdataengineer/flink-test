package org.example.s3

import org.apache.flink.api.scala._
import org.apache.flink.core.fs.FileSystem
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.scala.DataStream
import org.apache.flink.streaming.api.windowing.time.Time
import org.example.TestSpec

class WriteS3AsCsvWithS3StateBackendTest extends TestSpec {
  it should "do a wordcount" in withDataStreamEnv { env =>
    env.setStateBackend(new FsStateBackend("s3://dnvriend-data/flink.state"))
    // source
    val text: DataStream[String] = env.fromElements("To be, or not to be,--that is the question:--",
      "Whether 'tis nobler in the mind to suffer", "The slings and arrows of outrageous fortune",
      "Or to take arms against a sea of troubles,")

    // transformations
    val counts: DataStream[(String, Int)] = {
      text
        .flatMap(_.toLowerCase.split("\\W+"))
        .map((_, 1))
        .keyBy(0)
        .timeWindow(Time.seconds(5))
        .sum(1)
      }

    counts.writeAsCsv("s3://dnvriend-data/flink-write-as-text-test.csv", writeMode = FileSystem.WriteMode.OVERWRITE)
    env.execute("WordCount s3 csv")
  }
}
