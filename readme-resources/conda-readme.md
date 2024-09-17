
#
#### Install Conda
#

wget -nv https://repo.anaconda.com/miniconda/Miniconda3-py39_24.1.2-0-Linux-x86_64.sh -O /apps/python/Miniconda3-py39_24.1.2-0-Linux-x86_64.sh
sudo bash /apps/python/Miniconda3-py39_24.1.2-0-Linux-x86_64.sh -b -p /opt/conda
sudo chmod -Rf 775 /opt/conda
sudo chown -Rf brijeshdhaker:root /opt/conda

conda config --set auto_activate_base false

export PATH=/opt/conda/bin:$PATH
conda config --set always_yes yes --set changeps1 no
conda info -a
conda update -n base -c defaults conda
conda install mamba -c conda-forge
conda init bash
conda init --reverse --all
#
#### Path entry for conda package manager
#

export PATH=/opt/conda/bin:$PATH
PYSPARK_PYTHON=/opt/conda/envs/pyspark37/bin/python
PYSPARK_DRIVER_PYTHON=/opt/conda/envs/pyspark37/bin/python

#
#### Create Conda Virtual Env : Python 3.7
#
conda env create -f bd-pyspark-module/env_python_37.yml
mamba env update -f bd-pyspark-module/env_python_37.yml --prune
conda activate env_python_37
conda pack -f -o /apps/python/env_python_37_20221125.tar.gz

#
#### Create Conda Virtual Env : Python 3.8
#
conda create -y -n pyspark3.8 -c conda-forge pyarrow pandas conda-pack
conda activate env_python_38
conda pack -f -o /apps/hostpath/python/env_python_38_20221125.tar.gz

#
#### Create Conda Virtual Env : Python 3.9
#
conda create -y -n env_python_39 -c conda-forge python=3.9.18 pyarrow pandas conda-pack
mamba env update -f bd-pyspark-module/env_python_39.yml --prune
conda activate env_python_39
conda update -n base -c defaults conda
conda pack -f -o /apps/python/env_python_39_20221125.tar.gz


# The python conda tar should be public accessible, so need to change permission here.
hdfs dfs –put /apps/python/env_python_39_20221125.tar.gz /archives/pyspark/
hdfs dfs -copyFromLocal /apps/python/env_python_39_20221125.tar.gz /archives/pyspark/env_python_39_20221125.tar.gz
hadoop fs -chmod 775 /archives/pyspark/env_python_39_20221125.tar.gz

#
#### Install Package in Virtual Environment
#

conda install -c conda-forge grpcio protobuf pycodestyle numpy pandas scipy pandasql panel pyyaml seaborn plotnine hvplot intake intake-parquet intake-xarray altair vega_datasets pyarrow
conda install -c conda-forge pyspark=3.4.1

conda remove pyspark==3.5.0

#
####  
# 
conda env create -f bd-pyspark-module/env_python_39.yml
sudo -E /opt/conda/bin/conda env create -f bd-pyspark-module/env_python_39.yml
sudo -E /opt/conda/bin/conda update -n base -c defaults conda



conda activate mr-delta
pip install confluent-kafka[avro]==1.8.2 faker mysql-connector-python avro-python3 pycodestyle
pip install confluent-kafka[avro]==2.5.3 faker mysql-connector-python avro-python3 pycodestyle
pip install numpy pandas scipy grpcio pandasql ipython ipykernel
pip install jupyter_client nb_conda panel pyyaml seaborn plotnine hvplot intake
pip install intake-parquet intake-xarray altair vega_datasets pyarrow pytest

#
# List Conda Virtual Environments
#
conda env list
conda info --envs

#
# List Conda Virtual Environments Libraries
#
conda list

#
#
#
conda env remove --name pyspark37

#
#
#
conda rename -n base  env_python_39
conda rename -p /opt/conda base

#
#### Activate Virtual Env
#
conda activate env_python_39

#
# Update Virtual Env
#
conda env update --file env_python_39.yml --prune
conda env update --name env_python_39 --file env_python_39.yml --prune

#
#### Export Virtual Env
#
conda pack -n env_python_39 -o /apps/hostpath/python/pyspark39-$(date "+%Y%m%d").tar.gz
hdfs dfs -copyFromLocal /apps/hostpath/python/pyspark39-$(date "+%Y%m%d").tar.gz /archives/pyspark/

# The python conda tar should be public accessible, so need to change permission here.
hadoop fs -chmod 775 /archives/pyspark/pyspark39-20221125.tar.gz

#
#### Deactivate Cond Env
#

conda deactivate

#
#### 
#
conda install anaconda-clean

#
#### Remove all Anaconda-related files and directories with a confirmation prompt before deleting each one:
#
anaconda-clean --yes
