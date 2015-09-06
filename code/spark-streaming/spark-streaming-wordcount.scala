//:load /mnt/hgfs/f/code/spark-streaming/spark-streaming-wordcount.scala

//Spark Streaming Hello World Example
//This app listens on port 9997 for streaming text data and does a word count on it

// To feed data into 9998, we can use netcat 
// Example - taken from https://spark.apache.org/docs/latest/streaming-programming-guide.html


//Start with atleast 2 threads
//$ spark-shell --master local[2]

import org.apache.spark._
import org.apache.spark.streaming._

//For App use the following syntax. For spark-shell skip it
//val sc = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount")

val ssc = new StreamingContext(sc, Seconds(1))

// Create a DStream that will connect to hostname:port, like localhost:9996

//lines is of the type DStream(socketInputStream)
val lines = ssc.socketTextStream("localhost", 4446)

// Split each line into words
// words are of the type DStream(String)
val words = lines.flatMap(_.split(" "))

// Count each word in each batch
// pairs are of the type DStream(String, int)
val pairs = words.map(word => (word, 1))
val wordCounts = pairs.reduceByKey(_ + _)

// Print the first ten elements of each RDD generated in this DStream to the console
wordCounts.print()
wordCounts.saveAsTextFiles("/user/cloudera/nwc/nwc")

ssc.start()             // Start the computation
ssc.awaitTermination()  // Wait for the computation to terminate


//Separately on a different terminal 
$nc -lk 9999 (and type few sentences)
