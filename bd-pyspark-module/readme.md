#
## Create Zip File
#
python -m zipfile -c bd-pyspark-module/target/bd-pyspark-module-1.0.0.zip bd-pyspark-module/src/main/py/*

#
# Run Python __main__.py get executed
#
python bd-pyspark-module/target/bd-pyspark-module-1.0.0.zip --Host localhost --App hello_py

#
#
#
export PYSPARK_DRIVER_PYTHON=/opt/conda/envs/env_python3_11_13/bin/python
export PYSPARK_PYTHON=/opt/conda/envs/env_python3_11_13/bin/python
export PYTHONPATH=$PYTHONPATH:./bd-pyspark-module/target/bd-pyspark-module-1.0.0.zip

python -m com.example.app --Host localhost --App hello_py
python -m com.example.hello --Host localhost --App hello_py

# Check the newly built image
```bash

$ docker run -it --rm brijeshdhaker/python-base:3.11.13 python /apps/bd-pyspark-module-1.0.0.zip --Host localhost --App hello_py

$ docker run -it --rm \
  -e PYSPARK_DRIVER_PYTHON=/usr/local/bin/python \
  -e PYSPARK_PYTHON=/usr/local/bin/python \
  brijeshdhaker/python-base:3.11.13 python -m com.example.app --Host localhost --App hello_py

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

# or

PYTHONPATH=$PYTHONPATH:/home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/bd-pyspark-module/src/main/py
export WORK_DIR=/home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/bd-pyspark-module/src/main/py
/home/brijeshdhaker/.conda/envs/env_python_39/bin/python /home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/bd-pyspark-module/src/main/py/com/example/kafka/confluent/confluent_kafka_AvroProducer.py
/home/brijeshdhaker/.conda/envs/env_python_39/bin/python /home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/bd-pyspark-module/src/main/py/com/example/kafka/confluent/confluent_kafka_AvroConsumer.py
/home/brijeshdhaker/.conda/envs/env_python_39/bin/python ./bd-pyspark-module/src/main/py/com/example/utils/FileUtils.py

```

#
pyspark --archives bd-pyspark-module/target/bd-pyspark-module-1.0.0.zip#
from com.example.models.Transaction import Transaction
from main import print_hi