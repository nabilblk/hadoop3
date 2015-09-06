//:load /mnt/hgfs/f/code/spark-streaming/spark-streaming-twitter-filtered.scala



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

/** 
val filters = new Array[String](6) 
    filters(0) = "ocwen"
    filters(1) = "altisource"
	filters(2) = "OCN"
	filters(3) = "ASPS"
	filters(4) = "Ocwen"
	filters(5) = "Altisource"

**/

/** 
val filters = new Array[String] (2)
	filters(0) = "Apache Spark"
	filters(1) = "Spark"
**/

// Find a movie from the URL below:
//http://www.movietickets.com/movie/mid/172875/n/Ted-2/ShowDate/0/searchZip/02111
val filters = new Array[String] (6)
	filters(0) = "Ted 2"
	filters(1) = "Tami-Lynn"
	filters(2) = "Tami Lynn"
	filters(3) = "Mark Wahlberg"
	filters(4) = "Seth MacFarlane"
	filters(5) = "Jessica Barth"


var ssc = new StreamingContext(sc, Seconds(10))
var tweets = TwitterUtils.createStream(ssc, None,filters)
var statuses = tweets.map(_.getText)
statuses.print()

ssc.start()