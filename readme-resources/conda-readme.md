#### Install Conda
```bash

wget -nv https://repo.anaconda.com/miniconda/Miniconda3-latest-Linux-x86_64.sh -O ~/Downloads/Miniconda3-latest-Linux-x86_64.sh
sudo bash ~/Downloads/Miniconda3-latest-Linux-x86_64.sh -b -p /opt/conda
sudo chmod -Rf 775 /opt/conda
sudo chown -Rf brijeshdhaker:root /opt/conda
```

export PATH=/opt/conda/bin:$PATH

conda config --set auto_activate_base false
conda config --set always_yes yes
conda config --set changeps1 true

conda info -a
conda update -n base -c defaults conda
conda install mamba -c conda-forge
conda init bash

conda init --reverse --all
#
#### Path entry for conda package manager
```bash
export PATH=/opt/conda/bin:$PATH
export PYSPARK_PYTHON=/opt/conda/envs/env_python3_11_13/bin/python
export PYSPARK_DRIVER_PYTHON=/opt/conda/envs/env_python3_11_13/bin/python
```

#### Create Conda Virtual Env : Python 3.11.13
```bash

conda env create -f bd-pyspark-module/env_python3_11_13.yaml
sudo -E /opt/conda/bin/conda create -y -n env_python3_11_13 -c conda-forge python=3.11.13 pyarrow pandas conda-pack
sudo -E /opt/conda/bin/conda update -n base -c defaults conda
conda activate env_python3_11_13

conda install jupyter -c defaults
pip install confluent-kafka[avro]==2.10.1 faker mysql-connector-python avro-python3 pycodestyle
pip install numpy pandas scipy grpcio pandasql ipython ipykernel
pip install jupyter_client nb_conda panel pyyaml seaborn plotnine hvplot intake
pip install intake-parquet intake-xarray altair vega_datasets pyarrow pytest

YYYYMMDD=`(date "+%Y%m%d")`
conda pack -f -o /apps/python/env_python_31113_${YYYYMMDD}.tar.gz

```

#
#### Install Package in Virtual Environment
#

conda install -c conda-forge grpcio protobuf pycodestyle numpy pandas scipy pandasql panel pyyaml seaborn plotnine hvplot intake intake-parquet intake-xarray altair vega_datasets pyarrow
conda install -c conda-forge pyspark=3.5.3 delta-spark=3.3.2
pip install -U pip setuptools

#### Uninstall Package in Virtual Environment 
conda remove pyspark==3.5.3

#
#### The python conda tar should be public accessible, so need to change permission here.
#
hdfs dfs –put /apps/python/env_python_39_20221125.tar.gz /archives/pyspark/
hdfs dfs -copyFromLocal /apps/python/env_python_39_20221125.tar.gz /archives/pyspark/env_python_39_20221125.tar.gz
hadoop fs -chmod 775 /archives/pyspark/env_python_39_20221125.tar.gz

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
conda env remove --name env_python3_11_13

#
#
#
conda rename -n base  env_python3_11_13
conda rename -p /opt/conda base

#
#### Activate Virtual Env
#
conda activate env_python3_11_13

#
# Update Virtual Env
#
conda env update --file bd-pyspark-module/env_python3_11_13.yaml --prune
conda env update --name env_python3_11_13 --file bd-pyspark-module/env_python3_11_13.yaml --prune

#
#### Export Virtual Env
```bash

YYYYMMDD=`(date "+%Y%m%d")`
conda pack -n env_python3_11_13 -o /apps/hostpath/python/env_python3_11_13_${YYYYMMDD}.tar.gz
hdfs dfs -copyFromLocal /apps/hostpath/python/env_python3_11_13_${YYYYMMDD}.tar.gz /archives/pyspark/
# The python conda tar should be public accessible, so need to change permission here.
hadoop fs -chmod 775 /archives/pyspark/env_python3_11_13_${YYYYMMDD}.tar.gz

```

####
conda list --explicit > bd-pyspark-module/env_python3_11_13_spec_file.txt

#### Export Cond Env Configuration
```bash
conda env export > bd-pyspark-module/env_python3_11_13_packages.yaml
conda export --name env_python3_11_13 --format=environment-yaml --file=./bd-pyspark-module/env_python3_11_13_packages.yaml

conda export --file=environment.yaml    # Auto-detects YAML format
conda export --file=environment.json    # Auto-detects JSON format
conda export --file=explicit.txt        # Auto-detects explicit format
conda export --file=requirements.txt    # Auto-detects requirements format
```

#### Deactivate Cond Env
conda deactivate

#### 
conda install anaconda-clean

#### Remove all Anaconda-related files and directories with a confirmation prompt before deleting each one:
anaconda-clean --yes
