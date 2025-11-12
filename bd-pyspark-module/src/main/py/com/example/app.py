import numpy as np  # type: ignore[import]
import os
import pandas as pd  # type: ignore[import]
from pyspark.sql import SparkSession
from pyspark.sql.functions import pandas_udf
from pyspark.sql.pandas.utils import require_minimum_pandas_version, require_minimum_pyarrow_version
from typing import Iterator

require_minimum_pandas_version()
require_minimum_pyarrow_version()

# This is only needed for windows
def setEnv():
    # Replace with your Spark dir in windows
    os.environ['VAR_1'] = '/opt/conda/envs/env_python3_11_13/bin/python'
    os.environ['VAR_2'] = '/opt/conda/envs/env_python3_11_13/bin/python'

def printEnv():
    print(f"PYSPARK_PYTHON : {os.environ['PYSPARK_PYTHON']}")
    print(f"PYSPARK_DRIVER_PYTHON :  {os.environ['PYSPARK_DRIVER_PYTHON']}")

def main(spark):
    pdf = pd.DataFrame([1, 2, 3], columns=["x"])
    df = spark.createDataFrame(pdf)

    # Declare the function and create the UDF
    @pandas_udf("long")
    def plus_one(iterator: Iterator[pd.Series]) -> Iterator[pd.Series]:
        for x in iterator:
            yield x + 1

    df.select(plus_one("x")).show()


if __name__ == "__main__":

    # If running on windows , set env variables , for Linux skip
    if os.name == 'nt':
        print("Windows OS , printing env variable set") 
        setEnv()
    else:
        print("Linux OS , printing env variable set") 
        printEnv()


    session = SparkSession.builder.getOrCreate()
    session.sparkContext.setLogLevel("WARN")
    # Enable Arrow-based columnar data transfers
    session.conf.set("spark.sql.execution.arrow.pyspark.enabled", "true")

    #
    main(session)