
name := "flink-test"

organization := "org.example"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.12"

val flinkVersion = "1.7.2"
val hadoopVersion = "2.9.2"

val `flink-test` = (project in file("."))
  .settings(
    libraryDependencies += "org.apache.flink" %% "flink-scala" % flinkVersion,
    libraryDependencies += "org.apache.flink" %% "flink-streaming-scala" % flinkVersion,
    libraryDependencies += "org.apache.flink" % "flink-hadoop-fs" % flinkVersion,
    libraryDependencies += "org.apache.flink" % "flink-s3-fs-hadoop" % flinkVersion,
    libraryDependencies += "org.apache.hadoop" % "hadoop-common" % hadoopVersion,
    libraryDependencies += "binx.io" % "flink-gcs-fs" % "1.0.0-SNAPSHOT",

    // test
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.7" % Test,
    libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.25" % Test,
    libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.25" % Test,
    libraryDependencies += "log4j" % "log4j" % "1.2.17" % Test
  )

// testing configuration
fork in Test := true
parallelExecution := false

licenses := Seq(("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt")))

