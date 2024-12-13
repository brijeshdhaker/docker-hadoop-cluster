
### Start Cassandra Cluster
```shell
docker compose -f  bd-docker-sandbox/dc-cassandra.yml up -d
```

### Create Cassandra Table
```

CREATE KEYSPACE spark_stream WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};
use spark_stream;
CREATE TABLE tweeter_tweets(uuid text PRIMARY KEY, text text, words int, length int);

```

https://repo1.maven.org/maven2/com/datastax/spark/spark-cassandra-connector-assembly_2.11/2.5.1/spark-cassandra-connector-assembly_2.11-2.5.1.jar