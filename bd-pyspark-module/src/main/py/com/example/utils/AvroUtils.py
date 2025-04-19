import os
import json
from confluent_kafka import avro

def load_avro_str(schema_file):
    #
    key_schema_string = """
    {"type": "string"}
    """
    # Get the current working directory
    cwd = os.getcwd()
    #
    with open(os.path.join(cwd, schema_file), 'r') as file:
        value_schema_string = file.read().rstrip()

    return key_schema_string, str(value_schema_string)

def load_avro_json(schema_file):
    #
    key_schema_string = """
    {"type": "string"}
    """

    # Get the current working directory
    cwd = os.getcwd()
    work_dir= os.environ.get("WORK_DIR","/home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/bd-pyspark-module/src/main/py")

    #
    with open(os.path.join(work_dir, schema_file), 'r') as file:
        value_schema_string = file.read().rstrip()

    return json.loads(key_schema_string), json.loads(value_schema_string)


def load_avro_schema(schema_file):
    key_schema_string = """
    {"type": "string"}
    """
    # Get the current working directory
    cwd = os.getcwd()
    work_dir= os.environ.get("WORK_DIR","/home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/bd-pyspark-module/src/main/py")

    key_schema = avro.loads(key_schema_string)
    value_schema = avro.load(os.path.join(work_dir, schema_file))

    return key_schema, value_schema