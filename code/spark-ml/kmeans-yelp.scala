//:load /mnt/hgfs/f/code/spark-ml/kmeans-yelp.scala

import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg.Vectors

//1.3
val df = sqlContext.jsonFile("/user/cloudera/yelp/yelp_academic_dataset_business.json").cache()


//1.4
val df = sqlContext.read.json("/user/cloudera/yelp/yelp_academic_dataset_business.json").cache()

val geoRDD = df.select("latitude","longitude").rdd
//val parsedData = geoRDD.map(s => Vectors.dense(s(0).toAny, s(1).toAny)).cache()
val parsedData = geoRDD.map(s => (s(0).toAny, s(1).toAny)).cache()



// Load and parse the data
//val data = sc.textFile("/user/cloudera/k-means/kmeans_data.txt")
//val parsedData = data.map(s => Vectors.dense(s.split(' ').map(_.toDouble))).cache()

// Cluster the data into two classes using KMeans
val numClusters = 9
val numIterations = 20
val clusters = KMeans.train(parsedData, numClusters, numIterations)
clusters.clusterCenters

// Evaluate clustering by computing Within Set Sum of Squared Errors
val WSSSE = clusters.computeCost(parsedData)
println("Within Set Sum of Squared Errors = " + WSSSE)

// Save and load model
//clusters.save(sc, "/user/cloudera/kmeans_results/myModelPath")
//val sameModel = KMeansModel.load(sc, "/user/cloudera/kmeans_results/myModelPath")