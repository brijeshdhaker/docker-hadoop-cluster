# Install Az (Azure CLI command)

sudo apt-get update
curl -sL https://aka.ms/InstallAzureCLIDeb | sudo bash

# Install kubectl
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl

#
#
#
az aks get-credentials --resource-group dhaker-aks-cluster --name aks-cluster

kubectl get nodes

az group delete --name dhaker-aks-cluster --yes --no-wait


sc._jsc.hadoopConfiguration().set("fs.azure","org.apache.hadoop.fs.azure.NativeAzureFileSystem")
sc._jsc.hadoopConfiguration().set("spark.hadoop.fs.wasbs.impl","org.apache.hadoop.fs.azure.NativeAzureFileSystem")
sc._jsc.hadoopConfiguration().set("fs.wasbs.impl","org.apache.hadoop.fs.azure.NativeAzureFileSystem")
sc._jsc.hadoopConfiguration().set("fs.AbstractFileSystem.wasbs.impl", "org.apache.hadoop.fs.azure.Wasbs")
sc._jsc.hadoopConfiguration().set("spark.hadoop.fs.adl.impl", "org.apache.hadoop.fs.adl.AdlFileSystem")

fs.azure.account.key.devstoreaccount1.blob.core.windows.net=Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==
val df = spark.read.parquet("wasbs://<container-name>@<storage-account-name>.blob.core.windows.net/<directory-name>")
#
#
#

config.set("spark.hadoop.fs.azure", "org.apache.hadoop.fs.azure.NativeAzureFileSystem");
config.set("spark.hadoop.fs.azure.account.key.devstoreaccount1.blob.core.windows.net", "Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==");

// wasbs://warehouse@devstoreaccount1.blob.core.windows.net/flight_data/airlines.csv

spark_df = spark.read.format('csv').\
option('header', True).\
load(" http://azurite:10000/devstoreaccount1/warehouse/flight_data/airlines.csv")
print(spark_df.show())

df=spark.read.format("csv").option("header",True).load("wasb://warehouse@devstoreaccount1.blob.core.windows.net/flight_data/airlines.csv")

pyspark --jars /path/to/hadoop-azure-3.2.1.jar,/path/to/azure-storage-8.6.4.jar

pyspark \
--packages org.apache.hadoop:hadoop-azure:3.3.4,com.microsoft.azure:azure-storage:7.0.1 \
--conf "spark.jars.ivy=/apps/.ivy2" \
--conf "spark.hadoop.fs.azure=org.apache.hadoop.fs.azure.NativeAzureFileSystem" \
--conf "spark.hadoop.fs.defaultFS=wasb://warehouse@devstoreaccount1.blob.core.windows.net" \
--conf "spark.hadoop.fs.azure.account.key.devstoreaccount1.blob.core.windows.net=Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==" \
--conf "spark.hadoop.fs.azure.storage.emulator.account.name=devstoreaccount1"

#
# In Case Of Blob Store
#

```shell

$SPARK_HOME/bin/pyspark \
  --packages org.apache.hadoop:hadoop-azure:3.3.4,com.microsoft.azure:azure-storage:7.0.1 \
  --conf spark.jars.ivy=/apps/.ivy2 \
  --conf spark.hadoop.fs.azure=org.apache.hadoop.fs.azure.NativeAzureFileSystem \
  --conf spark.hadoop.fs.azure.account.key.devstoreaccount1.blob.core.windows.net="Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==" \
  

```
  
#
# In Case Of ADLSv2
#  

```bash
 
$SPARK_HOME/bin/pyspark \
  --conf spark.hadoop.fs.azure.account.key.devstoreaccount1.dfs.core.windows.net="Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw=="  \
  --packages org.apache.hadoop:hadoop-azure:3.3.4,org.apache.hadoop:hadoop-azure-datalake:3.3.4

```

#
# Steps for AKS Cluster
#

kubectl proxy

kubectl create serviceaccount spark

kubectl create clusterrolebinding spark-role \
  --clusterrole=edit \
  --serviceaccount=default:spark \
  --namespace=default


$SPARK_HOME/bin/spark-submit \
  --master k8s://http://127.0.0.1:8001 \
  --deploy-mode cluster \
  --name spark-azure-test \
  --class org.apache.spark.examples.SparkPi \
  --conf spark.executor.instances=2 \
  --conf spark.kubernetes.authenticate.driver.serviceAccountName=spark \
  --conf spark.kubernetes.container.image=sptest.azurecr.io/spark-py:v1 \
  --conf spark.hadoop.fs.azure.account.key.csg100320025a786393.blob.core.windows.net="Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==" \
  --conf spark.kubernetes.container.image.pullSecrets=my-secret \
  --packages org.apache.hadoop:hadoop-azure:3.3.4,org.apache.hadoop:hadoop-azure-datalake:3.3.4,com.microsoft.azure:azure-storage:7.0.1 \
  --conf spark.driver.extraJavaOptions="-Divy.cache.dir=/tmp -Divy.home=/tmp" \
  --conf spark.kubernetes.file.upload.path=wasbs://container001@csg100320025a786393.blob.core.windows.net/ \
  file:///home/tsmatz/test.py
  
  
kubectl get pod

NAME                                READY   STATUS    RESTARTS   AGE
jobtest01-a3769b763c61c1de-driver   1/1     Running   0          6m23s
jobtest01-c9a1bc763c620059-exec-2   1/1     Running   0          6m9s
jobtest01-c9a1bc763c620059-exec-3   1/1     Running   0          6m9s
jobtest01-c9a1bc763c620059-exec-4   1/1     Running   0          6m9s



kubectl get pod

NAME                                READY   STATUS      RESTARTS   AGE
jobtest01-a3769b763c61c1de-driver   0/1     Completed   0          13m


kubectl logs jobtest01-a3769b763c61c1de-driver


