//:load /mnt/hgfs/f/code/spark-streaming/spark-streaming-top-ids_overtime.scala

//Run python streamtest.py localhost 4444 1000 /mnt/hgfs/f/data/hive_spark/weblogs/*

 

// start the shell with this command:
// spark-shell --master local[2]

import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.streaming._

// Configure the Streaming Context based on the shell's Spark Context 
// with a 10 second batch duration

var ssc = new StreamingContext(sc,Seconds(10))
val logs = ssc.socketTextStream("localhost",4445)

//Wake up every 30 seconds and reduceByKey on the window whose size is 5 min
//List the top users every 30 seconds 
val reqsByWin = logs.map(line => (line.split(' ')(2).toInt,1)).reduceByKeyAndWindow((x: Int, y: Int) => (x+y),Minutes(5),Seconds(30))

val topReqsByWin=reqsByWin.map(pair => pair.swap).transform(rdd => rdd.sortByKey(false))


//val topReqsByWin = reqsByWin.filter(s=>(s._1 == 22232))


topReqsByWin.map(pair => pair.swap).print()

ssc.start()
ssc.awaitTermination()