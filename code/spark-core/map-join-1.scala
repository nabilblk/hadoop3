val pageNames=sc.textFile("pages.txt").map(...)
val pageMap = pageNames.collect().toMap()
val visits = sc.textFile("visits.txt").map(..)
val joined = visits.map(v=>(v._1,(pageMap(v._1), v._2)))