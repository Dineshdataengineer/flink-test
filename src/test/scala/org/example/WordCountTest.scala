package org.example

import org.apache.flink.api.scala._

class WordCountTest extends TestSpec {
  it should "do a wordcount" in withExecutionEnvironment { env =>
    // get input data
    val text: DataSet[String] = env.fromElements("To be, or not to be,--that is the question:--",
      "Whether 'tis nobler in the mind to suffer", "The slings and arrows of outrageous fortune",
      "Or to take arms against a sea of troubles,")

    val counts: AggregateDataSet[(String, Int)] = {
      text
        .flatMap(_.toLowerCase.split("\\W+"))
        .map((_, 1))
        .groupBy(0)
        .sum(1)
      }

    // execute and print result
    counts.print()
  }
}
