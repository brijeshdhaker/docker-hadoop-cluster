import os
import re
import time
import uuid
from pyspark import SparkContext, SparkConf

APP_NAME = 'in-reduce-secondary-sort-print'
INPUT_FILE = '/apps/sandbox/defaultfs/trip_data.txt'
OUTPUT_DIR = '/data/output-in-reduce-sort-print-{timestamp}.txt'
OUTPUT_FILE = OUTPUT_DIR + '/part-{identifier}'

COMMA_DELIMITER = re.compile(''',(?=(?:[^"]*"[^"]*")*[^"]*$)''')

taxi_data = [
    ('b258df504d2fbf85','b7b0be6d3ec6c589aeac84c0','07/19/2016 02:45:00 PM','07/19/2016 02:48:00 PM'),
    ('b258df6e5ace922','0d61c4f9c8cb8d280fce3887','01/22/2015 04:15:00 PM','01/22/2015 04:30:00 PM'),
    ('b258df504d2fbf86','b7b0be6d3ec6c589aeac84c0','07/19/2016 03:00:00 PM','07/19/2016 03:15:00 PM'),
    ('b258df6e5ace922','0d61c4f9c8cb8d280fce3887','01/22/2015 04:45:00 PM','01/22/2015 05:30:00 PM'),
    ('b258df504d2fbf87','b7b0be6d3ec6c589aeac84c0','07/19/2016 03:25:00 PM','07/19/2016 03:45:00 PM'),
    ('b258df6e5ace922','0d61c4f9c8cb8d280fce3887','01/22/2015 05:45:00 PM','01/22/2015 06:30:00 PM'),
    ('b258df504d2fbf88','b7b0be6d3ec6c589aeac84c0','07/19/2016 03:55 PM','07/19/2016 04:05:00 PM'),
    ('b258df6e5ace922','0d61c4f9c8cb8d280fce3887','01/22/2015 06:45:00 PM','01/22/2015 07:10:00 PM'),
    ('b258df504d2fbf89','b7b0be6d3ec6c589aeac84c0','07/19/2016 04:25:00 PM','07/19/2016 04:45:00 PM'),
    ('b258df6e5ace922','0d61c4f9c8cb8d280fce3887','01/22/2015 07:15:00 PM','01/22/2015 08:30:00 PM'),
    ('b258df504d2fbf90','b7b0be6d3ec6c589aeac84c0','07/19/2016 04:50:00 PM','07/19/2016 05:05:00 PM'),
    ('b258df6e5ace922','0d61c4f9c8cb8d280fce3887','01/22/2015 08:35:00 PM','01/22/2015 08:50:00 PM'),
    ('b258df504d2fbf91','b7b0be6d3ec6c589aeac84c0','07/19/2016 05:15:00 PM','07/19/2016 06:45:00 PM'),
    ('b258df6e5ace922','0d61c4f9c8cb8d280fce3887','01/22/2015 09:15:00 PM','01/22/2015 10:30:00 PM')
]

TAXI_ID = 1
SECOND_KEY = 2
TRIP_END_TIMESTAMP = 3

TIMESTAMP = int(time.time())

# (taxi_id -> ('trip_id',	'taxi_id',	'trip_start_timestamp',	'trip_end_timestamp'))
def make_pair(entry):
    key = entry[TAXI_ID]
    return key, entry


def create_pair_rdd(ctx):
    rawRDD = ctx.parallelize(taxi_data)
    #headerlessRDD = rawRDD.filter(lambda x: not x.startswith('trip_id'))
    #rdd = rawRDD.map(lambda x: COMMA_DELIMITER.split(x))
    #validRDD = rdd.filter(lambda x: len(x[TAXI_ID]) > 0 and len(x[SECOND_KEY]) > 0 and len(x[TRIP_END_TIMESTAMP]) > 0)
    pairRDD = rawRDD.map(make_pair)
    compressedRDD = pairRDD.mapValues(lambda x: (x[SECOND_KEY], x[TRIP_END_TIMESTAMP]))

    return compressedRDD


def sort_group(group):
    return sorted(group, key=lambda entry: entry[0], reverse=False)


def save_as_sorted_group(lines):
    try:
        os.mkdir(OUTPUT_DIR.format(timestamp=TIMESTAMP))
    except Exception as e:
        print(e)

    identifier = uuid.uuid1()

    output_file = os.path.join(OUTPUT_FILE.format(timestamp=TIMESTAMP, identifier=str(identifier)))
    with open(output_file, 'w') as f:

        for line in lines:
            k = line[0]
            g = line[1]
            f.write(str(k) + ": ")
            for item in g:
                f.write('(' + str(item[0]) + ',' + str(item[1]) + ')')
            f.write('\n')


if __name__ == '__main__':
    conf = SparkConf()
    ctx = SparkContext(master='local[*]', appName=APP_NAME, conf=conf)
    ctx.setLogLevel('INFO')

    rdd = create_pair_rdd(ctx)
    

    groupedRDD = rdd.groupByKey(numPartitions=2)
    print(groupedRDD.collect())

    #sortedRDD = groupedRDD.mapValues(sort_group)

    #sortedRDD.foreachPartition(save_as_sorted_group)

    ctx.stop()
