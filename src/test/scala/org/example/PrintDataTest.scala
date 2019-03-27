package org.example

import org.example.generator.DataGenerator

class PrintDataTest extends TestSpec {
  it should "generate numbers" in withDataStreamEnv { env =>
//    val stream = DataGenerator.randomIntegers(env)
    val stream = DataGenerator.randomSensorData(env)
    stream.print()
    env.execute("GenerateAndShowDataTest")
  }
}
