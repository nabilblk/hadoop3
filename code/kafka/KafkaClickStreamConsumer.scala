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


object KafkaClickStreamConsumer {

        val sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
        def  getBasicAuthenticationEncoding():String ={
                val up = "admin" + ":" + "Altisource123";
                return new String(Base64.encodeBase64(up.getBytes()));
        }
		
        def getAdjTime():String = {
                val cal = Calendar.getInstance()
                cal.add(Calendar.SECOND,5)
                return sdf.format( cal.getTime())
        }
		
        def getJSONData(log:String  ):String = {
		
				val log = log.map(s=>s.split("\t"))
                val ts = getAdjTime()
                val ip  = log.map(s=>s(7));
                val url  = log.map(s=>s(12));
                val swid  = log.map(s=>s(13));
                val city  = log.map(s=>s(49));
                val country  = log.map(s=>s(50));
                val state  = log.map(s=>s(52));
                //Create JSONObject here
                val jsonParam = new JSONObject()
                jsonParam.put("ip", ip)
                jsonParam.put("url", url);
                jsonParam.put("swid", swid);
                jsonParam.put("city", city);
                jsonParam.put("country", country);
                jsonParam.put("state", state);

println("===== Json Parameters :")
println(jsonParam.toString())
println("=========== ")
                return jsonParam.toString();
        }
		
		def getHTTPConn(urlstr:String): HttpURLConnection = {
                val httpcon: HttpURLConnection = new URL(urlstr).openConnection().asInstanceOf[HttpURLConnection]
                httpcon.setDoOutput(true);
                httpcon.setRequestProperty("Content-Type", "application/json");
                httpcon.setRequestProperty("Accept", "application/json");
                httpcon.setRequestMethod("POST");
                return httpcon;
        }
		
        def main(args: Array[String]) {
                //val sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                val urlstr="https://103.19.88.246:8443/zoomdata/service/upload?source=CSRT"
                val httpcon = getHTTPConn(urlstr)
                httpcon.connect();
                val out = new   OutputStreamWriter(httpcon.getOutputStream());
                val sc = new SparkConf().setAppName("Kafka Click Stream Consumer")
                val ssc = new StreamingContext(sc, Seconds(30))
				
                // hostname:port for Kafka brokers, not Zookeeper
                val kafkaParams = Map("metadata.broker.list" -> "localhost:9092")
                val topics = Set("clickstream")
                val stream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder] (ssc, kafkaParams, topics)
                stream.foreachRDD (rdd=>  {
                        for(record<- rdd.map(s=>s._2).collect().toArray) {
                           val log = record.map(_._2)
                           //val log = record.split("\t")
                           out.write(getJSONData(log));
                           out.flush()
                        }
                })
				
                //out.close();
                ssc.start()
                ssc.awaitTermination()
        }
}
                