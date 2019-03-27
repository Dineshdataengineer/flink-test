# flink-test
A small study project on [Apache Flink](https://flink.apache.org/).

## Introduction
[Apache Flink](https://flink.apache.org/) is an open source stream processing framework developed by the Apache Software Foundation. The core of Apache Flink is a distributed streaming dataflow engine written in Java and Scala. Flink executes arbitrary dataflow programs in a data-parallel and pipelined manner. Flink's pipelined runtime system enables the execution of bulk/batch and stream processing programs. Furthermore, Flink's runtime supports the execution of iterative algorithms natively.

Flink provides a high-throughput, low-latency streaming engine, as well as support for event-time processing and state management. Flink applications are fault-tolerant in the event of machine failure and support exactly-once semantics. Programs can be written in Java, Scala, Python, and SQL, and are automatically compiled and optimized into dataflow programs that are executed in a cluster or cloud environment.

Flink does not provide its own data storage system, but provides data source and sink connectors to systems such as Amazon Kinesis, Apache Kafka, Alluxio, HDFS, Apache Cassandra, and ElasticSearch.

The creators of Apache Flink is [Data Artisans](https://www.ververica.com/). 

## Overview
Apache Flink’s dataflow programming model provides event-at-a-time processing on both finite and infinite datasets. At a basic level, Flink programs consist of streams and transformations. Conceptually, a stream is an infinite flow of data records, and a transformation is an operation that takes one or more streams as input, and produces one or more output streams as a result.

Apache Flink includes two core APIs: a [DataStream API](https://ci.apache.org/projects/flink/flink-docs-release-1.7/dev/datastream_api.html#flink-datastream-api-programming-guide) for bounded or unbounded streams of data and a [DataSet API](https://ci.apache.org/projects/flink/flink-docs-release-1.7/dev/batch/index.html) for bounded data sets. Flink also offers a Table API, which is a SQL-like expression language for relational stream, and batch processing that can be easily embedded in Flink’s DataStream and DataSet APIs. The highest-level language supported by Flink is SQL, which is semantically similar to the Table API and represents programs as SQL query expressions.

## Programming model and distributed runtime
Upon execution, Flink programs are mapped to streaming dataflows. Every Flink dataflow starts with one or more sources (a data input, e.g., a collection, a message queue or a file system) and ends with one or more sinks (a data output, e.g., a message queue, file system, or database). An arbitrary number of transformations can be performed on the stream. These streams can be arranged as a directed, acyclic dataflow graph, allowing an application to branch and merge dataflows.

Flink offers ready-built source and sink connectors with Alluxio, Apache Kafka, Amazon Kinesis, HDFS, Apache Cassandra, and more.

Flink programs run as a distributed system within a cluster and can be deployed in a standalone mode as well as on YARN, Mesos, Docker-based setups along with other resource management frameworks, but can also be run locally in a single JVM.

## State: Checkpoints, Savepoints, and Fault-tolerance
Apache Flink includes a lightweight fault tolerance mechanism based on distributed checkpoints. A checkpoint is an automatic, asynchronous snapshot of the state of an application and the position in a source stream. In the case of a failure, a Flink program with checkpointing enabled will, upon recovery, resume processing from the last completed checkpoint, ensuring that Flink maintains exactly-once state semantics within an application. The checkpointing mechanism exposes hooks for application code to include external systems into the checkpointing mechanism as well (like opening and committing transactions with a database system).

Flink also includes a mechanism called savepoints, which are manually-triggered checkpoints. A user can generate a savepoint, stop a running Flink program, then resume the program from the same application state and position in the stream. Savepoints enable updates to a Flink program or a Flink cluster without losing the application's state . As of Flink 1.2, savepoints also allow to restart an application with a different parallelism—allowing users to adapt to changing workloads.

## Savepoints
Programs written in the **Data Stream API** can resume execution from a savepoint. Savepoints allow both updating your programs and your Flink cluster without losing any state.

Savepoints are manually triggered checkpoints, which take a snapshot of the program and write it out to a state backend. They rely on the regular checkpointing mechanism for this. During execution programs are periodically snapshotted on the worker nodes and produce checkpoints. For recovery only the last completed checkpoint is needed and older checkpoints can be safely discarded as soon as a new one is completed.

Savepoints are similar to these periodic checkpoints except that they are triggered by the user and don’t automatically expire when newer checkpoints are completed. Savepoints can be created from the command line or when cancelling a job via the REST API.

## State Backends
The exact data structures in which the key/values indexes are stored depends on the chosen state backend. One state backend stores data in an in-memory hash map, another state backend uses RocksDB as the key/value store. In addition to defining the data structure that holds the state, the state backends also implement the logic to take a point-in-time snapshot of the key/value state and store that snapshot as part of a checkpoint.

## Available APIs
Apache Flink provides API to develop streaming (batch) applications on different levels of abstraction:

- High level: SQL API
- Declarative DSL: Table API
- Core API: DataSet and DataStream APIs
- Low-level: Stateful Stream Processing APIs

## DataSet - Core API
Flink’s [DataSet API](https://ci.apache.org/projects/flink/flink-docs-release-1.7/dev/batch/index.html) enables transformations (e.g., filters, mapping, joining, grouping) on bounded datasets. The API is available in Java, Scala and an experimental Python API. Flink’s DataSet API is conceptually similar to the DataStream API. The DataSet API offers additional primitives on bounded data sets, like loops/iterations.

## DataStream - Core API
Flink’s [DataStream API](https://ci.apache.org/projects/flink/flink-docs-release-1.7/dev/datastream_api.html#flink-datastream-api-programming-guide) enables transformations (e.g. filters, aggregations, window functions) on bounded or unbounded streams of data. The API is available in Java and Scala.

## Apache Beam Flink Runner
[Apache Beam](https://beam.apache.org/) provides an advanced unified programming model, allowing a developer to implement batch and streaming data processing jobs that can run on any execution engine. The Apache Flink-on-Beam runner is the most feature-rich [according to a capability matrix maintained by the Beam community](https://beam.apache.org/documentation/runners/capability-matrix/).

Data Artisans, in conjunction with the Apache Flink community, worked closely with the Beam community to develop a Flink runner. Read [Why Apache Beam? A Google Perspective](https://cloud.google.com/blog/products/gcp/why-apache-beam-a-google-perspective). TL;DR "For Apache Beam to achieve its goal of pipeline portability, we needed to have at least one runner that was sophisticated enough to be a compelling alternative to Cloud Dataflow when running on premise or on non-Google clouds. Apache Flink is the runner that currently fulfills those requirements. With Flink, Beam becomes a truly compelling platform for the industry."

## Windows
Aggregating events (e.g., counts, sums) works differently on streams than in batch processing. For example, it is impossible to count all elements in a stream, because streams are in general infinite (unbounded). Instead, aggregates on streams (counts, sums, etc), are scoped by windows, such as “count over the last 5 minutes”, or “sum of the last 100 elements”.

Windows can be time driven (example: every 30 seconds) or data driven (example: every 100 elements). One typically distinguishes different types of windows, such as tumbling windows (no overlap), sliding windows (with overlap), and session windows (punctuated by a gap of inactivity).

## Time
When referring to time in a streaming program (for example to define windows), one can refer to different notions of time:

- **Event Time**: is the time when an event was created. It is usually described by a timestamp in the events, for example attached by the producing sensor, or the producing service. Flink accesses event timestamps via timestamp assigners.
- **Ingestion time**: is the time when an event enters the Flink dataflow at the source operator.
- **Processing Time**: is the local time at each operator that performs a time-based operation.

## Stateful Operations
While many operations in a dataflow simply look at one individual event at a time (for example an event parser), some operations remember information across multiple events (for example window operators). These operations are called stateful.

The state of stateful operations is maintained in what can be thought of as an embedded key/value store. The state is partitioned and distributed strictly together with the streams that are read by the stateful operators. Hence, access to the key/value state is only possible on keyed streams, after a keyBy() function, and is restricted to the values associated with the current event’s key. Aligning the keys of streams and state makes sure that all state updates are local operations, guaranteeing consistency without transaction overhead. This alignment also allows Flink to redistribute the state and adjust the stream partitioning transparently.

## Checkpoints for Fault Tolerance
Flink implements fault tolerance using a combination of stream replay and checkpointing. A checkpoint is related to a specific point in each of the input streams along with the corresponding state for each of the operators. A streaming dataflow can be resumed from a checkpoint while maintaining consistency (exactly-once processing semantics) by restoring the state of the operators and replaying the events from the point of the checkpoint.

The checkpoint interval is a means of **trading off the overhead of fault tolerance during execution with the recovery time**, the number of events that need to be replayed.

## Batch on Streaming
Flink executes batch programs as a special case of streaming programs, where the streams are bounded, finite number of elements. A DataSet is treated internally as a stream of data. The concepts above thus apply to batch programs in the same way as well as they apply to streaming programs, with minor exceptions:

Fault tolerance for batch programs does not use checkpointing. Recovery happens by fully replaying the streams. That is possible, because inputs are bounded. This pushes the cost more towards the recovery, but makes the regular processing cheaper, because it avoids checkpoints.

Stateful operations in the DataSet API use simplified in-memory/out-of-core data structures, rather than key/value indexes.

The DataSet API introduces special synchronized (superstep-based) iterations, which are only possible on bounded streams. 

## Data Sources
Data sources create the initial data sets, such as from files or from Java collections. The general mechanism of creating data sets is abstracted behind an InputFormat. Flink comes with several built-in formats to create data sets from common file formats. Many of them have shortcut methods on the ExecutionEnvironment.

### File-based
The following file based data sources are available:

- readTextFile(path) / TextInputFormat - Reads files line wise and returns them as Strings.
- readTextFileWithValue(path) / TextValueInputFormat - Reads files line wise and returns them as StringValues. StringValues are mutable strings.
- readCsvFile(path) / CsvInputFormat - Parses files of comma (or another char) delimited fields. Returns a DataSet of tuples, case class objects, or POJOs. Supports the basic java types and their Value counterparts as field types.
- readFileOfPrimitives(path, delimiter) / PrimitiveInputFormat - Parses files of new-line (or another char sequence) delimited primitive data types such as String or Integer using the given delimiter.
- readSequenceFile(Key, Value, path) / SequenceFileInputFormat - Creates a JobConf and reads file from the specified path with type SequenceFileInputFormat, Key class and Value class and returns them as Tuple2<Key, Value>.

### Collection-based
The following collection based data sources are available:

- fromCollection(Seq) - Creates a data set from a Seq. All elements in the collection must be of the same type.
- fromCollection(Iterator) - Creates a data set from an Iterator. The class specifies the data type of the elements returned by the iterator.
- fromElements(elements: _*) - Creates a data set from the given sequence of objects. All objects must be of the same type.
- fromParallelCollection(SplittableIterator) - Creates a data set from an iterator, in parallel. The class specifies the data type of the elements returned by the iterator.
- generateSequence(from, to) - Generates the sequence of numbers in the given interval, in parallel.

### Generic
The following generic data sources are available:
- readFile(inputFormat, path) / FileInputFormat - Accepts a file input format.
- createInput(inputFormat) / InputFormat - Accepts a generic input format.

## Data Sinks
Data sinks consume DataSets and are used to store or return them. Data sink operations are described using an OutputFormat. Flink comes with a variety of built-in output formats that are encapsulated behind operations on the DataSet:

- writeAsText() / TextOutputFormat - Writes elements line-wise as Strings. The Strings are obtained by calling the toString() method of each element.
- writeAsCsv(...) / CsvOutputFormat - Writes tuples as comma-separated value files. Row and field delimiters are configurable. The value for each field comes from the toString() method of the objects.
- print() / printToErr() - Prints the toString() value of each element on the standard out / standard error stream.
- write() / FileOutputFormat - Method and base class for custom file outputs. Supports custom object-to-bytes conversion.
- output()/ OutputFormat - Most generic output method, for data sinks that are not file based (such as storing the result in a database).

A DataSet can be input to multiple operations. Programs can write or print a data set and at the same time run additional transformations on them.

## S3
Providers:
- [BasicAWSCredentialsProvider](https://github.com/Aloisius/hadoop-s3a/blob/master/src/main/java/org/apache/hadoop/fs/s3a/BasicAWSCredentialsProvider.java): requires an accessKey and secretKey 
- [EnvironmentVariableCredentialsProvider](https://github.com/puppetlabs/aws-sdk-for-java/blob/master/src/main/java/com/amazonaws/auth/EnvironmentVariableCredentialsProvider.java): requires AWS_ACCESS_KEY_ID and AWS_SECRET_KEY
- [InstanceProfileCredentialsProvider](https://github.com/puppetlabs/aws-sdk-for-java/blob/master/src/main/java/com/amazonaws/auth/InstanceProfileCredentialsProvider.java)

## Releases
- v1.7.2: 2019-02-15
- v1.7.0: 2018-11-30
- v1.6.0: 2018-08-08
- v1.5.0: 2018-05-25
- v1.4.0: 2017-12-12
- v1.3.0: 2017-06-01
- v1.2.0: 2017-02-06
- v1.1.0: 2016-08-08
- v1.0.0: 2016-03-08
- v0.9.0: 2015-06-24


## Resources
- [Wikipedia - Apache FLink](https://en.wikipedia.org/wiki/Apache_Flink)
- [Apache Flink Documentation](https://ci.apache.org/projects/flink/flink-docs-release-1.7/)
- [Apache Flink - Scala API Extensions](https://ci.apache.org/projects/flink/flink-docs-stable/dev/scala_api_extensions.html)

## Videos
- [(Past), Present, and Future of Apache Flink](https://www.youtube.com/watch?v=cb5Val090cg)
- [Robust Stream Processing with Apache Flink](https://www.youtube.com/watch?v=kyLtjuz5A0c)