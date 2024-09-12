
from pyflink.datastream import StreamExecutionEnvironment
from pyflink.table import StreamTableEnvironment, DataTypes
from pyflink.table.expressions import call, col
from pyflink.table.udf import udf


provinces = ("Beijing", "Shanghai", "Hangzhou", "Shenzhen", "Jiangxi", "Chongqing", "Xizang")


@udf(input_types=[DataTypes.STRING()], result_type=DataTypes.STRING())
def province_id_to_name(id):
    return provinces[id]


def log_processing():
    env = StreamExecutionEnvironment.get_execution_environment()
    t_env = StreamTableEnvironment.create(stream_execution_environment=env)
    t_env.get_config().get_configuration().set_boolean("python.fn-execution.memory.managed", True)

    create_kafka_source_ddl = """
            CREATE TABLE payment_msg(
                createTime VARCHAR,
                orderId BIGINT,
                payAmount DOUBLE,
                payPlatform INT,
                provinceId INT
            ) WITH (
              'connector' = 'kafka',
              'topic' = 'payment_msg',
              'properties.bootstrap.servers' = 'kafkabroker.sandbox.net:9092',
              'properties.group.id' = 'test_3',
              'scan.startup.mode' = 'latest-offset',
              'format' = 'json'
            )
            """

    create_es_sink_ddl = """
            CREATE TABLE es_sink(
                province VARCHAR PRIMARY KEY,
                pay_amount DOUBLE
            ) with (
                'connector' = 'elasticsearch-7',
                'hosts' = 'http://elasticsearch:9200',
                'index' = 'platform_pay_amount_1',
                'document-id.key-delimiter' = '$',
                'sink.bulk-flush.max-size' = '42mb',
                'sink.bulk-flush.max-actions' = '32',
                'sink.bulk-flush.interval' = '1000',
                'sink.bulk-flush.backoff.delay' = '1000',
                'format' = 'json'
            )
    """

    t_env.execute_sql(create_kafka_source_ddl)
    t_env.execute_sql(create_es_sink_ddl)
    t_env.register_function('province_id_to_name', province_id_to_name)

    t_env.from_path("payment_msg") \
        .select(call('province_id_to_name', col('provinceId')).alias("province"), col('payAmount')) \
        .group_by(col('province')) \
        .select(col('province'), call('sum', col('payAmount').alias("pay_amount"))) \
        .execute_insert("es_sink")


if __name__ == '__main__':
    log_processing()

