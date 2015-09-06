// :load  /mnt/hgfs/f/code/spark-streaming/spark-streaming-sentiment-analysis2.scala
 
import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter._
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.rdd._

var ssc 		= new StreamingContext(sc, Seconds(15))
val probRDD  	= sc.textFile("/user/cloudera/sentiment/sentiment_dictionary.csv").map(s=>s.split(",")).map(s=> (s(0), (s(1).toFloat, s(2).toFloat)  ))
 

val consumerKey		 		=	"lcwDHVM9rIVC1dKi8AHFj76Wx"
val consumerSecret	 		=	"wedjiyVdK6upOhzS72J8PVffTH5GDKVXCmmAXQ9yRhRMIkx5fU"
val accessToken 	 		=	"51190643-CEYTplw7fPYyw7Ir5j2TyDOJMIkektKAmDkRNmt5r"
val accessTokenSecret		=	"OgzrKZFfBhMxej7pe72GJNTmJFxA4sLhgDsiy2fQAjtNu"
	
System.setProperty("twitter4j.oauth.consumerKey", consumerKey)
System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret)
System.setProperty("twitter4j.oauth.accessToken", accessToken)
System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret)
 
 
val filters = new Array[String] (2)
	filters(0) = "ISIS"
	filters(1) = "isis"

	  
var tweets = TwitterUtils.createStream(ssc, None,filters)
var tweet_line = tweets.map(_.getText)
 

tweet_line.foreachRDD (rdd => {

 	val tweet_bag = rdd.flatMap(s=>(s.split(' '))).map(s=>(s,1.toFloat))
	val tweetProbRDD = tweet_bag.join(probRDD)
	val posValue = tweetProbRDD.map(s=>(s._1,s._2._2._1  )  ).reduceByKey((v1,v2)=> (v1+v2)).map(s=>s._2)
	val negValue = tweetProbRDD.map(s=>(s._1,s._2._2._2  )  ).reduceByKey((v1,v2)=> (v1+v2)).map(s=>s._2)
    
	
	if (rdd.count() >0) {
		val posSum =  posValue.reduce((v1,v2)=> (v1+v2))
		val negSum =  negValue.reduce((v1,v2)=> (v1+v2))

		val	posProb = (1/((math.exp(negSum-posSum) +1)))
		println ("************************************************")
		println("Tweet:" + rdd.collect().foreach(println))
		 
		println ("The positive probabilty in % is :%,.2f "+ posProb)
		println ("The negative probability in %is :%,.2f "+ (1-posProb))
		println ("************************************************")

	}

})
 
	
 
ssc.start()