python /apps/hostpath/python/bd-python-module-distro.zip#pie.py

PYTHONPATH=/apps/hostpath/python/bd-python-module-distro.zip python -m main.py [args]

python /apps/hostpath/python/bd-python-module-distro.zip/main.py

# 
export PYTHONPATH="$PYTHONPATH:/apps/hostpath/python/pyspark-module-distro.zip"

#
```python
import sys
sys.path.insert(0, "/apps/hostpath/python/pyspark-module-distro.zip")
sys.path[0]
import hello
hello.greet("Pythonista")
```

```shell
import sys
sys.path.insert(0, "/home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/bd-pyspark-module/src/main/py")

# or

PYTHONPATH=$PYTHONPATH:/home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/bd-pyspark-module/src/main/py
export WORK_DIR=/home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/bd-pyspark-module/src/main/py
/home/brijeshdhaker/.conda/envs/env_python_39/bin/python /home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/bd-pyspark-module/src/main/py/com/example/kafka/confluent/confluent_kafka_AvroProducer.py
/home/brijeshdhaker/.conda/envs/env_python_39/bin/python /home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/bd-pyspark-module/src/main/py/com/example/kafka/confluent/confluent_kafka_AvroConsumer.py
/home/brijeshdhaker/.conda/envs/env_python_39/bin/python ./bd-pyspark-module/src/main/py/com/example/utils/FileUtils.py

```

#
pyspark --archives /apps/hostpath/python/pyspark-module-distro.zip#
from com.example.models.Transaction import Transaction
from main import print_hi