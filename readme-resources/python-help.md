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

#
pyspark --archives /apps/hostpath/python/pyspark-module-distro.zip#
from com.example.models.Transaction import Transaction
from main import print_hi