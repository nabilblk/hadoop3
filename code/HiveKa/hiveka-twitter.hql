-- export CLASSPATH =.:/opt/kafka/kafka_2.9.1-0.8.2.1/libs/*:.:/opt/cloudera/parcels/CDH/lib/avro/*:/opt/hiveka/HiveKa/target/*

java   –cp ${CLASSPATH}   org.apache.hadoop.hive.kafka.demoproducer.FakeTweetProducer tweet 10  localhost:9092

hive --auxpath {$CLASSPATH}

drop table tweets;
  

create external table tweets (username string, text string, timestamp bigint) 
stored by 'org.apache.hadoop.hive.kafka.KafkaStorageHandler' 
tblproperties('kafka.topic'='tweet', 
'kafka.service.uri'='104.155.76.87:9092',
 'kafka.whitelist.topics'='tweet', 
'kafka.avro.schema.file'='/tmp/twitter.avsc');

select username,count(*) from tweets group by username;


java -classpath {$CLASSPATH} org.apache.hadoop.hive.kafka.demoproducer.AvroConsoleProducer console localhost:9092

create external table console (message string) stored by 'org.apache.hadoop.hive.kafka.KafkaStorageHandler' tblproperties('kafka.service.uri'='hivekafka-1.ent.cloudera.com:9092', 'kafka.whitelist.topics'='console','kafka.avro.schema.file'='/tmp/console.avsc');


 