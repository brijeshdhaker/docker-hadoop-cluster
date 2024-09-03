#
#
#

$ jupyter lab --port=8888 --ip=0.0.0.0 --no-browser --allow-root --NotebookApp.token='' --notebook-dir=/home/notebooks/

# Check the newly built image
$ docker run -it --rm quay.io/jupyter/all-spark-notebook:spark-3.5.2 pyspark --version

Entered start.sh with args: pyspark --version
Running hooks in: /usr/local/bin/start-notebook.d as uid: 1000 gid: 100
Done running hooks in: /usr/local/bin/start-notebook.d
Running hooks in: /usr/local/bin/before-notebook.d as uid: 1000 gid: 100
Sourcing shell script: /usr/local/bin/before-notebook.d/10activate-conda-env.sh
Sourcing shell script: /usr/local/bin/before-notebook.d/10spark-config.sh
Done running hooks in: /usr/local/bin/before-notebook.d
Executing the command: pyspark --version
Welcome to
____              __
/ __/__  ___ _____/ /__
_\ \/ _ \/ _ `/ __/  '_/
/___/ .__/\_,_/_/ /_/\_\   version 3.5.2
/_/

Using Scala version 2.12.18, OpenJDK 64-Bit Server VM, 17.0.12
Branch HEAD
Compiled by user ubuntu on 2024-08-06T11:36:15Z
Revision bb7846dd487f259994fdc69e18e03382e3f64f42
Url https://github.com/apache/spark
Type --help for more information.
