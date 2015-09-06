sudo sed -i "s/mirrorlist=https/mirrorlist=http/" /etc/yum.repos.d/epel.repo

http://xmodulo.com/how-to-set-up-epel-repository-on-centos.html
http://ask.xmodulo.com/install-pip-linux.html

tail2Kafka for Kafka 0.8 http:#blog.mmlac.com/log-transport-with-apache-kafka/
tail2Kafka for Kafka 0.7


#Start kafka
http://kafka.apache.org/documentation.html#quickstart


#Install Kafka as follows

mkdir /opt/kafka
cd /opt/kafka
wget http://apache.mesi.com.ar/kafka/0.8.2.1/kafka_2.9.1-0.8.2.1.tgz
tar -xvf kafka_2.9.1-0.8.2.1.tgz
cd kafka_2.9.1-0.8.2.1

#Start zookeeper
nohup bin/zookeeper-server-start.sh config/zookeeper.properties & 

#Start Kafka Server 
nohup bin/kafka-server-start.sh config/server.properties & 


bin/kafka-topics.sh --delete --zookeeper localhost:2181   --topic clickstream


#create a topic
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1   --topic clickstream


#Test the topic
bin/kafka-topics.sh --list --zookeeper localhost:2181

#Produce a message
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic clickstream   

#Consume the message
bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic clickstream --from-beginning

bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic tweet --from-beginning



#Now install tail2Kafka
apt-get install git
#Git install kafka python - which is a dependency
git clone https://github.com/mumrah/kafka-python

#Install PIP and Kafka-python 
sudo apt-get install python-pip
#now install kafka-python using pip
pip install ./kafka-python

#Download Tail2Kafka
git clone https://github.com/mmlac/tail2kafka

chmod +x \opt\tail2kafka\tail2kafka\tail2kafka.py 
#tail2kafka -l <log> -t <topic> -s <host> -p <port>
nohup python tail2kafka.py -l log -t clickstream -s localhost -p 9092 & 

# Log some contents 


# Treat SSHs
openssl s_client -connect 103.19.88.246:8443 < /dev/null | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > public.crt

$JAVA_HOME/keytool -import -alias <server_name> -keystore $JAVA_HOME/lib/security/cacerts -file public.crt

$JAVA_HOME/keytool -import -alias 103.19.88.246  -keystore $JAVA_HOME/lib/security/cacerts -file public.crt


sudo exec java $JAVA_OPTS  -Dcom.sun.net.ssl.checkRevocation=false -Dsbt.main.class=sbt.ScriptMain -jar "./root/.ivy2/cache/org.scala-sbt/sbt-launch" $0 "$@"

sudo  java $JAVA_OPTS  -Dcom.sun.net.ssl.checkRevocation=false -Dsbt.main.class=sbt.ScriptMain -jar "/root/.ivy2/cache/org.scala-sbt/sbt-launch/jars/sbt-launch-0.13.7.jar" "$@"
