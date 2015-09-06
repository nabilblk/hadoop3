//How to pick the ith element of an RDD

rdd.zipWithIndex().map(s=>s(1),(0)).lookup(43) //pick the 44th element (0 base)
rdd.zipWithIndex().map(s=>s(1),(0)).lookup(rdd.count()-1) //will pick the last element
   