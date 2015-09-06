// Implement the copyFromLocal function - well not quiet
//Load from a local file system and move to HDFS
val fileRDD = sc.textFile("/user/cloudera/charles_dickens") 
val upperRDD = fileRDD.map(line=>line.toUpperCase )
val upperRDD.saveAsTextFile("/user/cloudera/charles_dickensUpper")
