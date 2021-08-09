## Run Python in yarn cluster

Zeppelin supports to run python interpreter in yarn cluster which means the python interpreter runs in the yarn container.
This can achieve better multi-tenant for python interpreter especially when you already have a hadoop yarn cluster.

But there's one critical problem to run python in yarn cluster: how to manage the python environment in yarn container. Because yarn cluster is a distributed cluster environemt
which is composed many nodes, and your python interpreter can start in any node. It is not practical to manage python environment in each nodes.

So in order to run python in yarn cluster, we would suggest you to use conda to manage your python environment, and Zeppelin can ship your
codna environment to yarn container, so that each python interpreter can has its own python environment.

### Step 1
We would suggest you to use conda pack to create archives of conda environments, and ship it to yarn container. Otherwise python interpreter
will use the python executable in PATH of yarn container.

Here's one example of yml file which could be used to generate a conda environment with python 3 and some useful python libraries.

* Create yml file for conda environment, write the following content into file `env_python_3.yml`

```text
name: python_3_env
channels:
  - conda-forge
  - defaults
dependencies:
  - python=3.7 
  - pycodestyle
  - numpy
  - pandas
  - scipy
  - grpcio
  - protobuf
  - pandasql
  - ipython
  - ipykernel
  - jupyter_client
  - panel
  - pyyaml
  - seaborn
  - plotnine
  - hvplot
  - intake
  - intake-parquet
  - intake-xarray
  - altair
  - vega_datasets
  - pyarrow

```

* Create conda environment via this yml file using either `conda` or `mamba`

```bash

conda env create -f env_python_3.yml
```

```bash

mamba env create -f python_3_env
```


* Pack the conda environment using either `conda`

```bash

conda pack -n python_3
```

### Step 2

Specify the following properties to enable yarn mode for python interpreter, and specify the correct python environment.

```
zeppelin.interpreter.launcher yarn
zeppelin.yarn.dist.archives /apps/hostpath/uploads/python-3.7.10-venv.tar.gz#environment
zeppelin.interpreter.conda.env.name environment
```

`zeppelin.yarn.dist.archives` is the python conda environment tar which is created in step 1.
This tar will be shipped to yarn container and untar in the working directory of yarn container.
`environment` in `/home/hadoop/python_3.tar.gz#environment` is the folder name after untar. This folder name should be the same as `zeppelin.interpreter.conda.env.name`.

## Python environments (used for non-yarn mode)

### Default
By default, PythonInterpreter will use python command defined in `zeppelin.python` property to run python process.
The interpreter can use all modules already installed (with pip, easy_install...)

### Conda
[Conda](http://conda.pydata.org/) is an package management system and environment management system for python.
`%python.conda` interpreter lets you change between environments.

#### Usage

- get the Conda Information:

    ```
    %python.conda info
    ```

- list the Conda environments:

    ```
    %python.conda env list
    ```

- create a conda enviornment:

    ```
    %python.conda create --name [ENV NAME]
    ```

- activate an environment (python interpreter will be restarted):

    ```
    %python.conda activate [ENV NAME]
    ```

- deactivate

    ```
    %python.conda deactivate
    ```

- get installed package list inside the current environment

    ```
    %python.conda list
    ```

- install package

    ```
    %python.conda install [PACKAGE NAME]
    ```

- uninstall package

    ```
    %python.conda uninstall [PACKAGE NAME]
    ```

### Docker

`%python.docker` interpreter allows PythonInterpreter creates python process in a specified docker container.

#### Usage

- activate an environment

    ```
    %python.docker activate [Repository]
    %python.docker activate [Repository:Tag]
    %python.docker activate [Image Id]
    ```

- deactivate

    ```
    %python.docker deactivate
    ```

<br/>
Here is an example

```
# activate latest tensorflow image as a python environment
%python.docker activate gcr.io/tensorflow/tensorflow:latest
```

## Technical description

For in-depth technical details on current implementation please refer to [python/README.md](https://github.com/apache/zeppelin/blob/master/python/README.md).


## Some features not yet implemented in the vanilla Python interpreter

* Interrupt a paragraph execution (`cancel()` method) is currently only supported in Linux and MacOs.
  If interpreter runs in another operating system (for instance MS Windows) , interrupt a paragraph will close the whole interpreter.
  A JIRA ticket ([ZEPPELIN-893](https://issues.apache.org/jira/browse/ZEPPELIN-893)) is opened to implement this feature in a next release of the interpreter.
* Progression bar in webUI  (`getProgress()` method) is currently not implemented.