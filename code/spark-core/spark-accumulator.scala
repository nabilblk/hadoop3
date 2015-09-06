val jpgcount = sc.accumulator(0)
val htmlcount = sc.accumulator(0)
val csscount = sc.accumulator(0)

val filename="/tmp/weblogs/*"
val logs = sc.textFile(filename)

 logs.foreach(line => {
     if (line.contains(".html")) htmlcount += 1
     else if (line.contains(".jpg")) jpgcount += 1
     else if (line.contains(".css")) csscount += 1
 })

 println("Request Totals:")
 println(".css requests: "+ csscount.value)
 println(".html requests: " + htmlcount.value)
 println(".jpg requests: " + jpgcount.value)
 