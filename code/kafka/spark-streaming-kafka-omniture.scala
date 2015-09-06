//Spark Streaming + Kafka for omniture data


import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka.KafkaUtils
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.net.HttpURLConnection;
import org.apache.commons.codec.binary.Base64;
import java.net.URL;




def  getBasicAuthenticationEncoding():String ={

        val up = "admin" + ":" + "Altisource123";
        return new String(Base64.encodeBase64(up.getBytes()));
}


def getAdjTime():String = {
	val cal = Calendar.getInstance()
	cal.add(Calendar.SECOND,5)
	return sdf.format( cal.getTime())
}


def getJSONData(String log):String = {

	val ts = getAdjTime()
	val ip  = log.map(s=>s(7));
	val url  = log.map(s=>s(12));
	val swid  = log.map(s=>s(13));
	val city  = log.map(s=>s(49));
	val country  = log.map(s=>s(50));
	val state  = log.map(s=>s(52));

    ts.print()
	key.print()
	val.print()

	ip.print()
	city.print()
	
	
	//Create JSONObject here
	JSONObject jsonParam = new JSONObject();
	jsonParam.put("ip", ip);
	jsonParam.put("url", url);
	jsonParam.put("swid", swid);
	jsonParam.put("city", city);
	jsonParam.put("country", country);
	jsonParam.put("state", state);

	return jsonParam.toString();
}


def getHTTPConn(): HttpURLConnection(String urlstr) {
	val httpcon: HttpURLConnection = new URL(urlstr).openConnection().asInstanceOf[HttpURLConnection]
	  
	httpcon.setDoOutput(true);
	httpcon.setRequestProperty("Content-Type", "application/json");
	httpcon.setRequestProperty("Accept", "application/json");
	httpcon.setRequestMethod("POST");
	
	return httpcon;
	
}

object KafkaClickStreamConsumer { 
	def main(args: Array[String]) {
		val sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		val urlstr="https://103.19.88.246:8443/zoomdata/service/upload?source=Clistream%20RT%20(Omniture)"

		val httpcon = getHTTPConn(urlstr)
		httpcon.connect();
		OutputStreamWriter out = new   OutputStreamWriter(httpcon.getOutputStream());
		val ssc = new StreamingContext(sc, Seconds(30))
		
		// hostname:port for Kafka brokers, not Zookeeper
		val kafkaParams = Map("metadata.broker.list" -> "localhost:9092")
		val topics = Set("test")
		val stream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder] (ssc, kafkaParams, topics)
		val log = stream.map(_._2)

		out.write(getJSonData(log));
		out.flush()
		//out.close();  
		 
		  

		ssc.start()
		ssc.awaitTermination()
	}
}