//:load /mnt/hgfs/f/code/spark-streaming/spark-streaming-streaminglogs.scala

// start the shell with this command:
// spark-shell --master local[2]

import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.streaming.Seconds

// Configure the Streaming Context based on the shell's Spark Context 
// with a 10 second batch duration

var ssc = new StreamingContext(sc,Seconds(10))

// Create a DStream of log data from the server at localhost:4445
// logs are of the type SocketInputDStream
val logs = ssc.socketTextStream("localhost",4445)
  
//There is one RDD per DStream. Each RDD contains 100 lines if the following conditions are met:
// Run the py script with a frequence of 10 lines per second for a total of 100 lines per ssc fetch (defined in line #13)
logs.foreachRDD(s=>(println("Size of each  RDD is :" + s.count() )) )


//Optionally, print 10 lines on the client (driver's) side
//logs.print()

//Optionally, you can print all lines of each RDD
// Note: This is an O(n2) operation as there is a foreach within a foreach
//N should be small enough by using a checkpoint
//logs.foreachRDD(s=>(println("Value of each log RDD is :" + s.collect().foreach(println) )) )


// Save the contents in a log
logs.saveAsTextFiles("/user/cloudera/streaming/logs")

// Every 10 seconds, display the total number of requests over the 
// last 60 seconds
ssc.checkpoint("logcheckpt")

//This value should be 60 if you run the python code with 1 line per second as output


//DStreams are populated every 5 seconds as defined in ssc
//The window period is 60 seconds
//The logs are counted once in every 10 seconds 
//In other words, a window operation is done every 10 second
//req_counts is of the type DStream[long]
val req_counts = logs.countByWindow(Seconds(60),Seconds(10))
req_counts.print() //printed every 10 seconds

// Start the Streaming Context and await completion of all threads
ssc.start()
ssc.awaitTermination()
