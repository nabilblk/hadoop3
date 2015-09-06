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
//import collection.mutable.HashMap
import java.util.HashMap
import org.apache.log4j.PropertyConfigurator
object KafkaClickStreamConsumer {
        val STATE_MAP = new HashMap[String,String]()
        val sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Logger.getLogger("org").setLevel(Level.OFF)
        Logger.getLogger("akka").setLevel(Level.OFF)
        STATE_MAP.put("AL","Alabama")
        STATE_MAP.put("AK", "Alaska");
    STATE_MAP.put("AB", "Alberta");
    STATE_MAP.put("AZ", "Arizona");
    STATE_MAP.put("AR", "Arkansas");
    STATE_MAP.put("BC", "British Columbia");
    STATE_MAP.put("CA", "California");
    STATE_MAP.put("CO", "Colorado");
    STATE_MAP.put("CT", "Connecticut");
    STATE_MAP.put("DE", "Delaware");
    STATE_MAP.put("DC", "District Of Columbia");
    STATE_MAP.put("FL", "Florida");
    STATE_MAP.put("GA", "Georgia");
    STATE_MAP.put("GU", "Guam");
    STATE_MAP.put("HI", "Hawaii");
    STATE_MAP.put("ID", "Idaho");
    STATE_MAP.put("IL", "Illinois");
    STATE_MAP.put("IN", "Indiana");
    STATE_MAP.put("IA", "Iowa");
    STATE_MAP.put("KS", "Kansas");
    STATE_MAP.put("KY", "Kentucky");
    STATE_MAP.put("LA", "Louisiana");
    STATE_MAP.put("ME", "Maine");
    STATE_MAP.put("MB", "Manitoba");
    STATE_MAP.put("MD", "Maryland");
    STATE_MAP.put("MA", "Massachusetts");
	STATE_MAP.put("MI", "Michigan");
    STATE_MAP.put("MN", "Minnesota");
    STATE_MAP.put("MS", "Mississippi");
    STATE_MAP.put("MO", "Missouri");
    STATE_MAP.put("MT", "Montana");
    STATE_MAP.put("NE", "Nebraska");
    STATE_MAP.put("NV", "Nevada");
    STATE_MAP.put("NB", "New Brunswick");
    STATE_MAP.put("NH", "New Hampshire");
    STATE_MAP.put("NJ", "New Jersey");
    STATE_MAP.put("NM", "New Mexico");
    STATE_MAP.put("NY", "New York");
    STATE_MAP.put("NF", "Newfoundland");
    STATE_MAP.put("NC", "North Carolina");
    STATE_MAP.put("ND", "North Dakota");
    STATE_MAP.put("NT", "Northwest Territories");
    STATE_MAP.put("NS", "Nova Scotia");
    STATE_MAP.put("NU", "Nunavut");
    STATE_MAP.put("OH", "Ohio");
    STATE_MAP.put("OK", "Oklahoma");
    STATE_MAP.put("ON", "Ontario");
    STATE_MAP.put("OR", "Oregon");
    STATE_MAP.put("PA", "Pennsylvania");
    STATE_MAP.put("PE", "Prince Edward Island");
    STATE_MAP.put("PR", "Puerto Rico");
    STATE_MAP.put("QC", "Quebec");
    STATE_MAP.put("RI", "Rhode Island");
    STATE_MAP.put("SK", "Saskatchewan");
    STATE_MAP.put("SC", "South Carolina");
    STATE_MAP.put("SD", "South Dakota");
    STATE_MAP.put("TN", "Tennessee");
    STATE_MAP.put("TX", "Texas");
    STATE_MAP.put("UT", "Utah");
    STATE_MAP.put("VT", "Vermont");
    STATE_MAP.put("VI", "Virgin Islands");
    STATE_MAP.put("VA", "Virginia");
    STATE_MAP.put("WA", "Washington");
    STATE_MAP.put("WV", "West Virginia");
    STATE_MAP.put("WI", "Wisconsin");
    STATE_MAP.put("WY", "Wyoming");
    STATE_MAP.put("YT", "Yukon Territory");
        def  getBasicAuthenticationEncoding():String ={
                val up = "admin" + ":" + "Altisource123";
                return new String(Base64.encodeBase64(up.getBytes()));
        }
        def getAdjTime():String = {
                val cal = Calendar.getInstance()
                cal.add(Calendar.SECOND,1)
                return sdf.format( cal.getTime())
        }
		
		 def getJSONData (log2:String  ):String = {
                val log = log2.split("\t")
                val ts = getAdjTime()
                val click_ts=log(1)
                val ip  = log(7)
                val url  = log(12)
                val swid  = log(13)
                val isp = log(38)
                val channel = log (43)
                val city  = log(49)
                val country  = log(50)
                val state  = log(52)
                //Create JSONObject here
                val jsonParam = new JSONObject()
                jsonParam.put("_ts", ts)
                jsonParam.put("click_ts", click_ts)
                jsonParam.put("ip", ip)
                jsonParam.put("swid", swid);
                jsonParam.put("url", url);
                jsonParam.put("isp", isp);
                jsonParam.put("channel", channel);
                jsonParam.put("city", city);
                jsonParam.put("country", country);
                jsonParam.put("state", STATE_MAP.get(state.toUpperCase() ));
                return jsonParam.toString();
        }
        def getHTTPConn():HttpURLConnection  = {
                val url ="https://103.19.88.246:8443/zoomdata/service/upload?source=CSRT2"
                val conn : HttpURLConnection = new URL(url).openConnection().asInstanceOf[HttpURLConnection]
                conn.setDoOutput(true);
                //conn.setDoInput(true);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestMethod("POST");
                val userPassword = "admin"+ ":" + "Altisource123";
val encoded = new String(Base64.encodeBase64(userPassword.getBytes()));
                val encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
                //conn.setRequestProperty("Authorization", "Basic " + encoding);
conn.setRequestProperty("Authorization", String.format("Basic %s", encoded));
                return conn;
        }

		
		def main(args: Array[String]) {
                if (args.length != 1) {
                        println("Usage: sbt run <kafka-server>");
                        System.exit(1);
                }
                SSLUtilities.trustAllHostnames();
                SSLUtilities.trustAllHttpsCertificates();
                javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                        new javax.net.ssl.HostnameVerifier(){
                                def verify(hostname:String,sslSession:javax.net.ssl.SSLSession) :Boolean =  {
                                return true;
                        }
                });
                val sc = new SparkConf().setAppName("KafkaClickStreamConsumer").setMaster("local[4]")
                val ssc = new StreamingContext(sc, Seconds(4))
                val logger = Logger.getLogger("KafkaClickStreamConsumer")
                logger.setLevel(org.apache.log4j.Level.OFF)
                // hostname:port for Kafka brokers, not Zookeeper
                val ks = args(0) + ":9092"
                val kafkaParams = Map("metadata.broker.list" -> ks)
                val topics = Set("clickstream")
                val stream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder] (ssc, k
afkaParams, topics)
                stream.foreachRDD (rdd=>  {
                        val records =rdd.map(s=>s._2).collect().toArray
                        for(record<- records) {
//println("==========================================================================")
//println(record)
//println("==========================================================================")
//println(getJSONData(record))
//println("==========================================================================")
                                val conn = getHTTPConn()
                                val out = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
                                conn.connect()
                                out.write(getJSONData(record));
                                out.flush()
                                if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                                        println (" HTTP code : "
                                        + conn.getResponseMessage () +":" +
                                        + conn.getResponseCode());
                                }
                        }
                })
                //out.close();
                ssc.start()
                ssc.awaitTermination()
        }
}
                                
		
		
		
		

	
                                               