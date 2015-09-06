val movies = sc.textFile("/user/cloudera/movies/movies").map(s=>s.split(","))   // movieId,title,genres
val links = sc.textFile("/user/cloudera/movies/links").map(s=>s.split(","))     //movieId,imdbId,tmdbId
val ratings = sc.textFile("/user/cloudera/movies/ratings").map(s=>s.split(",")) //userId,movieId,rating,timestamp
val tags = sc.textFile("/user/cloudera/movies/tags").map(s=>s.split(","))       //userId,movieId,tag,timestamp




val rRDD = ratings.map(s=>(s(1).toInt,(s(2).toFloat))).cache()
rRDD.take(5).foreach(println)


//First count how many users have rated every movie
val countRDD = rRDD.map(pair=>(pair._1,1)).reduceByKey((v1,v2)=>(v1+v2)) //8552 unique movies countRDD.count() will reveal that
countRDD.takeOrdered(5).foreach(println)  // For example, 232 users have rated movie ID 1
val crossCheck = rRDD.filter(pair=>pair._1 ==1).count() //should be 232

//Next, sum up all the ratings for each movie
val sumRDD = rRDD.reduceByKey((v1,v2)=>(v1+v2)) // Movie ID 1 has a rating sum of 888.5
sumRDD.takeOrdered(5).foreach(println)

//An alternative way of achieving the same is 
val fRDD=rRDD.foldByKey(0)( (v1,v2)=>(v1+v2))
fRDD.takeOrdered(5).foreach(println)
 
countRDD.count() // 8552. Has a structure <Key=movieID> <value = # of users who have rated this movie>
sumRDD.count() //both should be 8552.  Has a structure <Key=movieID> <value = sum of ratings>

val jRDD = countRDD.join(sumRDD)   //<key=movieID> value=<#of users, total rating>
jRDD.takeOrdered(5).foreach(println)
val avgRDD = jRDD.map( pair  =>(pair._1, (pair._2._2/pair._2._1)))
avgRDD.takeOrdered(5).foreach(println) 
avgRDD.count() // Always ensure that the count = 8552 across any RDDs that we have manipulated so far

//Now filter for average >=4.0
val goodmovies = avgRDD.filter(s=>s._2>=4.0)
goodmovies.takeOrdered(5).foreach(println) 
goodmovies.count() //There are 1809 movies with ratings of 4.0 or higher)


//Now filter for movies released in 1995 from the moviesRDD
val mRDD=movies.map(s=>(s(0).toInt, s(1))).filter(pair=>(pair._2.contains("(1995)"))) 
mRDD.takeOrdered(5).foreach(println)
mRDD.count() //215 movies were released in 1995

//Okie... Now we are ready to join...Inner Join?
val resultRDD = mRDD.join(goodmovies)
resultRDD.takeOrdered(5).foreach(println)
resultRDD.count()  //33 of them got a rating of 4.0 or higher


//Toy Story did not get a rating of 4.0 or higher??? Crosscheck



