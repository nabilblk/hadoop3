//:load /mnt/hgfs/f/code/spark-streaming/windowed-count.scala

//Spark Streaming Hello World Example
//This app listens on port 9997 for streaming text data and does a word count on it

// To feed data into 9998, we can use netcat 
// Example - taken from https://spark.apache.org/docs/latest/streaming-programming-guide.html


//Start with atleast 2 threads
//$ spark-shell --master local[2]

import org.apache.spark._
import org.apache.spark.streaming._

//For App use the following syntax. For spark-shell skip it
 
val ssc = new StreamingContext(sc, Seconds(10))

// Create a DStream that will connect to hostname:port, like localhost:9996

//lines is of the type DStream(socketInputStream)
val lines = ssc.socketTextStream("localhost", 4444)

//windowed_lines is a DStream
val windowed_lines=lines.window(Seconds(60), Seconds(30))

//window_counts is a DStream

val window_counts = windowed_lines.count()

window_counts.print()

 
ssc.start()             // Start the computation
ssc.awaitTermination()  // Wait for the computation to terminate


//Separately on a different terminal 
//$nc -lk 4446(and type few sentences)
