import scala.io.Source

// web server log file(s)
val logfile="/tmp/weblogs/*"
    
// Read in list of target models from a local file
val targetfile = "/tmp/targetmodels.txt"
val targetlist = Source.fromFile(targetfile).getLines.toList

// broadcast the target list to all workers
val targetlistbc = sc.broadcast(targetlist)

// filter out requests from devices not in the target list
val targetreqs = sc.textFile(logfile).filter(line => targetlistbc.value.count(line.contains(_))  > 0)

targetreqs.take(5).foreach(println)

