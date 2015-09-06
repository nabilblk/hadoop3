# Druid set up  http://druid.io/docs/latest/tutorials/tutorial-a-first-look-at-druid.html
mkdir /opt/druid
cd /opt/druid
wget http://static.druid.io/artifacts/releases/druid-0.8.1-rc2-bin.tar.gz
tar -zxvf druid-0.8.1-rc2-bin.tar.gz
cd druid-0.8.1-rc2

#start zookeeper first
nohup /opt/cloudera/parcels/CDH/lib/zookeeper/bin/zkServer.sh &

# Run wikipedia collector server
nohup ./run_example_server.sh &

# Query it using client app
./run_example_client.sh

# create timeseries.json for a CURL query
[ {
  "timestamp" : "2013-09-04T21:44:00.000Z",
  "result" : {
    "minTime" : "2013-09-04T21:44:00.000Z",
    "maxTime" : "2013-09-04T21:47:00.000Z"
  }
} ]

curl -X POST 'http://localhost:8084/druid/v2/?pretty' -H 'content-type: application/json'  -d  @timeseries.json


create topn.json
{
  "queryType": "topN",
  "dataSource": "wikipedia", 
  "granularity": "all", 
  "dimension": "page",
  "metric": "edit_count",
  "threshold" : 10,
  "aggregations": [
    {"type": "longSum", "fieldName": "count", "name": "edit_count"}
  ], 
  "filter": { "type": "selector", "dimension": "country", "value": "United States" }, 
  "intervals": ["2012-10-01T00:00/2020-01-01T00"]
}


curl -X POST 'http://localhost:8084/druid/v2/?pretty' -H 'content-type: application/json'  -d @topn.json


###########################


# Install SQL4D (Sql For Druid) http://druidwithsql.tumblr.com/post/98578718282/a-first-look-at-druid-with-sql

mkdir /opt/SQL4D
cd /opt/SQL4D

#install git if required
apt-get install git

git clone https://github.com/srikalyc/Sql4D.git

# install maven (Not maven 2)
apt-get install maven
mvn package

Currently maven is failing


