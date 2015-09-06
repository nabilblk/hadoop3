val hc = new org.apache.spark.sql.hive.HiveContext(sc)
import hc.implicits._
hc.cacheTable("movies")
 

//Create an UDF
hc.udf.register("strLen", (s: String) => s.length())
val udfmovies = hc.sql("select strLen(title) from movies")
udfmovies.show()
