from __future__ import print_function

import sys

from pyspark import SparkContext
from pyspark.sql import SparkSession, SQLContext
from pyspark.streaming import StreamingContext

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: logs.py <hostname> <port>", file=sys.stderr)
        exit(-1)
    sc = SparkContext("local[2]", appName="Logs")
    ssc = StreamingContext(sc, 5)
    sc.setLogLevel("ERROR")
    lines = ssc.socketTextStream(sys.argv[1], int(sys.argv[2]))
    RDD_split = lines.filter(lambda line: line != "")
    RDD_split.pprint(100)
    RDD_log_malicious = RDD_split.filter(lambda l: " W " in l) #see if there is suspicious log
    RDD_log_malicious.saveAsTextFiles("AndroidLogs/log") #saves all logs processed
    if(RDD_log_malicious.count() != 0):
        rdd_m = RDD_log_malicious.map(lambda x: x.split())
        app_name = rdd_m.map(lambda l: (l[6].split(":")[0], 1))
        app_name_sort = app_name.reduceByKey(lambda u,num: u+num)
        app_name_sort.saveAsTextFiles("SuspiciousList/apps")

    ssc.start()
    ssc.awaitTermination()

