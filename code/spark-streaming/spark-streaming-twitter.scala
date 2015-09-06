//:load /mnt/hgfs/f/code/spark-streaming/spark-streaming-twitter.scala



import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter._
import org.apache.spark.streaming.StreamingContext._

 
val consumerKey		 		=	"lcwDHVM9rIVC1dKi8AHFj76Wx"
val consumerSecret	 		=	"wedjiyVdK6upOhzS72J8PVffTH5GDKVXCmmAXQ9yRhRMIkx5fU"
val accessToken 	 		=	"51190643-CEYTplw7fPYyw7Ir5j2TyDOJMIkektKAmDkRNmt5r"
val accessTokenSecret		=	"OgzrKZFfBhMxej7pe72GJNTmJFxA4sLhgDsiy2fQAjtNu"
	
System.setProperty("twitter4j.oauth.consumerKey", consumerKey)
System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret)
System.setProperty("twitter4j.oauth.accessToken", accessToken)
System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret)

var ssc = new StreamingContext(sc, Seconds(10))
var tweets = TwitterUtils.createStream(ssc, None)
var statuses = tweets.map(_.getText)
statuses.print()

ssc.start()