<h6>
This is an initial draft version of a dynamic data platform leveraging PySpark.
</h6>

This platform allows for seamless ingestion and ETL processes, driven by configuration-based files for generic data pipelines.

We have followed medallion/multi-hop architecture, to incrementally and progressively improve the structure and quality of data as it flows through each layer of the architecture.
Bronze ⇒ Silver ⇒ Gold layer tables

In our bronze layer, we house raw feeds, from CSV to JSON, allowing ingestion using generic code lines.
The silver layer elevates data with each day's feed, setting the stage for external Hive tables for streamlined access.
In our gold layer, where PySpark orchestrates transformative ETL processes such as joins, filters, and aggregations and
finally produces the report which will be partitioned on date_id for easy querying.

<h6> Future Addition: </h6>
1. Different new data sources : Clouds buckets, streaming data
2. QA framework for data validation at the silver layer.
3. SCD1 and SCD2 implementation.
4. Incremental Surrogate key generation.
5. Containerized airflow and spark env for easy dev & deployments.
6. Create Airflow dags for the whole orchestration.
7. Dashboard to visualise datasets on a day to day basis.
8. setup hive/hive metastore and create external tables and use those tables.

## Create Zip File
```bash

python -m zipfile -c bd-pyspark-module/target/bd-pyspark-module-1.0.0.zip bd-pyspark-module/src/main/py/*
```

## Run Python __main__.py get executed
```bash

python bd-pyspark-module/target/bd-pyspark-module-1.0.0.zip --Host localhost --App hello_py
````

## Run Python module
```bash

export PYSPARK_DRIVER_PYTHON=/opt/conda/envs/env_python3_11_13/bin/python
export PYSPARK_PYTHON=/opt/conda/envs/env_python3_11_13/bin/python

export PYTHONPATH=$PYTHONPATH:./bd-pyspark-module/target/bd-pyspark-module-1.0.0.zip
# or
export PYTHONPATH=$PYTHONPATH:~/IdeaProjects/docker-hadoop-cluster/bd-pyspark-module/src/main/py
export WORK_DIR=~/IdeaProjects/docker-hadoop-cluster/bd-pyspark-module/src/main/py

python -m com.example.app --Host localhost --App hello_py
python -m com.example.hello --Host localhost --App hello_py
```

# Check the newly built image
```bash

$ docker run -it --rm \
  -e PYSPARK_DRIVER_PYTHON=/usr/local/bin/python \
  -e PYSPARK_PYTHON=/usr/local/bin/python \
  brijeshdhaker/python-base:3.11.13 python /apps/bd-pyspark-module-1.0.0.zip --Host localhost --App hello_py
```
```bash

$ docker run -it --rm \
  -e PYSPARK_DRIVER_PYTHON=/usr/local/bin/python \
  -e PYSPARK_PYTHON=/usr/local/bin/python \
  brijeshdhaker/python-base:3.11.13 python -m com.example.app --Host localhost --App hello_py
```
```bash

$ docker run -it --rm \
  -e PYSPARK_DRIVER_PYTHON=/usr/local/bin/python \
  -e PYSPARK_PYTHON=/usr/local/bin/python \
  brijeshdhaker/python-base:3.11.13 python -m com.example.hello --Host localhost --App hello_py
```

#
```python
import sys
sys.path.insert(0, "/apps/hostpath/python/pyspark-module-distro.zip")
sys.path[0]
import hello
hello.greet("Pythonista")
```

```python
import sys
sys.path.insert(0, "/home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/bd-pyspark-module/src/main/py")
```
# or
```bash

export PYTHONPATH=$PYTHONPATH:/home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/bd-pyspark-module/src/main/py
export WORK_DIR=/home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/bd-pyspark-module/src/main/py
/home/brijeshdhaker/.conda/envs/env_python_39/bin/python /home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/bd-pyspark-module/src/main/py/com/example/kafka/confluent/confluent_kafka_AvroProducer.py
/home/brijeshdhaker/.conda/envs/env_python_39/bin/python /home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/bd-pyspark-module/src/main/py/com/example/kafka/confluent/confluent_kafka_AvroConsumer.py
/home/brijeshdhaker/.conda/envs/env_python_39/bin/python ./bd-pyspark-module/src/main/py/com/example/utils/FileUtils.py

```

## Import modules in pyspark
```bash

pyspark --archives bd-pyspark-module/target/bd-pyspark-module-1.0.0.zip#
from com.example.models.Transaction import Transaction
from main import print_hi
```

## Run Python Unit Test
```bash

export PYTHONPATH=$PYTHONPATH:~/IdeaProjects/docker-hadoop-cluster/bd-pyspark-module/target/bd-pyspark-module-1.0.0.zip
export PYTHONPATH=$PYTHONPATH:~/IdeaProjects/docker-hadoop-cluster/bd-pyspark-module/src/main/py
python -m unittest ~/IdeaProjects/docker-hadoop-cluster/bd-pyspark-module/src/test/py/TestUsers.py
```