//Spark Streaming + Kafka for omniture data
import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka._
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.net.HttpURLConnection;
import org.apache.commons.codec.binary.Base64;
import java.net.URL;
import org.json.JSONObject
import java.io.OutputStreamWriter
//import scala.util.parsing.json.JSONObject
import org.apache.log4j.Logger
import org.apache.log4j.Level
object KafkaClickStreamConsumer {
        val sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        val urlstr="https://103.19.88.246:8443/zoomdata/service/upload?source=CSRT"
        val userPassword = "admin:Altisource123"
        val encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
        SSLUtilities.trustAllHostnames();
        SSLUtilities.trustAllHttpsCertificates();
        def  getBasicAuthenticationEncoding():String ={
                val up = "admin" + ":" + "Altisource123";
                return new String(Base64.encodeBase64(up.getBytes()));
        }
        def getAdjTime():String = {
                val cal = Calendar.getInstance()
                cal.add(Calendar.SECOND,5)
                return sdf.format( cal.getTime())
        }
        def getJSONData (log2:String  ):String = {
                val log = log2.split("\t")
                val ts = getAdjTime()
                val ip  = log(7)
                val url  = log(12)
                val swid  = log(13)
                val city  = log(49)
                val country  = log(50)
                val state  = log(52)
                //Create JSONObject here
                val jsonParam = new JSONObject()
                jsonParam.put("ts", ts)
       jsonParam.put("ip", ip)
                jsonParam.put("url", url);
                jsonParam.put("swid", swid);
                jsonParam.put("city", city);
                jsonParam.put("country", country);
                jsonParam.put("state", state);
println("+++++")
println(jsonParam.toString());
println("+++++")
                return jsonParam.toString();
        }
        def getHTTPConn(urlstr:String):HttpURLConnection  = {
                val httpcon: HttpURLConnection = new URL(urlstr).openConnection().asInstanceOf[HttpURLConnection
]
                httpcon.setDoOutput(true);
                httpcon.setRequestProperty("Content-Type", "application/json");
                httpcon.setRequestProperty("Accept", "application/json");
                httpcon.setRequestMethod("POST");
                return httpcon;
        }
        def main(args: Array[String]) {
                if (args.length != 1) {
                        println("Usage: sbt run <kafka-server>");
                        System.exit(1);
                }
                val httpcon = getHTTPConn(urlstr)
                httpcon.setRequestProperty("Authorization", "Basic " + encoding);
                httpcon.connect();
                val out = new   OutputStreamWriter(httpcon.getOutputStream());
                val sc = new SparkConf().setAppName("KafkaClickStreamConsumer").setMaster("local[4]")
                val ssc = new StreamingContext(sc, Seconds(30))
                val logger = Logger.getLogger("KafkaClickStreamConsumer")
                logger.setLevel(Level.ERROR)
                // hostname:port for Kafka brokers, not Zookeeper
                val ks = args(0) + ":9092"
                val kafkaParams = Map("metadata.broker.list" -> ks)
                val topics = Set("clickstream")
                val stream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder] (ssc, k
afkaParams, topics)
                stream.foreachRDD (rdd=>  {
                        val records =rdd.map(s=>s._2).collect().toArray
                  for(record<- records) {
println("-----------------------------------------------------------------------------------")
println(record)
println("-----------------------------------------------------------------------------------")
                                out.write(getJSONData(record));
                                out.flush()
                        }
                })
                //out.close();
                ssc.start()
                ssc.awaitTermination()
        }
}
                                                                                               
																			  
																			  
		