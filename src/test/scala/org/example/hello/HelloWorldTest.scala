package org.example.hello

import org.example.TestSpec

class HelloWorldTest extends TestSpec {
  it should "hello shouldBe hello" in {
    "hello" shouldBe "hello"
  }
}
