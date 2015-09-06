//:load /mnt/hgfs/f/code/spark-streaming/spark-streaming-streaminglogs.scala

// start the shell with this command:
// spark-shell --master local[2]

import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.streaming.Seconds

// Configure the Streaming Context based on the shell's Spark Context 
// with a 10 second batch duration

var ssc = new StreamingContext(sc,Seconds(1))

// Create a DStream of log data from the server at localhost:4445
// logs are of the type SocketInputDStream
val logs = ssc.socketTextStream("localhost",4447)
  
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

// Start the Streaming Context and await completion of all threads
ssc.start()
ssc.awaitTermination()
