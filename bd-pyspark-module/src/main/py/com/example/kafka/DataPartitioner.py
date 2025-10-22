import hashlib
from kafka.partitioner import murmur2
from random import random

class DataPartitioner:
    
    #
    def __init__(self, data) :
        self.data = data
        
    #
    #
    #
    def key_partitioner(self, key, all_partitions, available):
        """
        Customer Kafka partitioner to get the partition corresponding to key
        :param key: partitioning key
        :param all_partitions: list of all partitions sorted by partition ID
        :param available: list of available partitions in no particular order
        :return: one of the values from all_partitions or available
        """

        numPartitions = len(all_partitions)
        sp = abs(numPartitions*0.3)
        p = 0

        if key is None:
            if available:
                return random.choice(available)
            return random.choice(all_partitions)

        if key == "TESS":
            p = numPartitions % sp
        else:
            p = int(hashlib.sha1(key.encode()).hexdigest(), 16) % (numPartitions - sp) + sp

        #print("Key = " + str(key) + " Partition = " + str(p))
        return all_partitions[int(p)]

    #
    #
    #
    def sticky_key_partitioner(self, key, all_partitions, available):
        """
        Customer Kafka partitioner to get the partition corresponding to key
        :param key: partitioning key
        :param all_partitions: list of all partitions sorted by partition ID
        :param available: list of available partitions in no particular order
        :return: one of the values from all_partitions or available
        """
        numPartitions = len(all_partitions)
        sp = int(abs(numPartitions*0.3))
        p = 0
        if key is None:
            raise Exception("All messages must have sensor name as key")

        m = murmur2(key.encode()) # bytes(key, 'utf-8')
        if key == b"TESS":
            p = int(abs(m) % sp)
        else:
            p = int(abs(m) % (numPartitions - sp) + sp)
        return all_partitions.index(p)

    #
    #
    #
    def hash_partitioner(self, key, all_partitions, available):
        """
        Customer Kafka partitioner to get the partition corresponding to key
        :param key: partitioning key
        :param all_partitions: list of all partitions sorted by partition ID
        :param available: list of available partitions in no particular order
        :return: one of the values from all_partitions or available
        """

        if key is None:
            if available:
                return random.choice(available)
            return random.choice(all_partitions)

        idx = int(hashlib.sha1(key.encode()).hexdigest(), 16) % (10 ** 8)
        idx &= 0x7fffffff
        idx %= len(all_partitions)
        return all_partitions[idx]

#
#
#
if __name__ == '__main__':
    pass


