import unittest
from com.example.models.User import User
from com.example.kafka.DataPartitioner import DataPartitioner


class TestDataPartitioner(unittest.TestCase):

    #
    def test_key_partitioner(self):
        partitioner = DataPartitioner(None)
        all_partitions = [0,1,2,3,4,5,6,7,8,9]
        key_abc = "ABC"
        key_xyz = "XYZ"
        key_tess = "TESS"

        partition_abc = partitioner.key_partitioner(key_abc, all_partitions, all_partitions)
        self.assertEqual(partition_abc, partitioner.key_partitioner(key_abc, all_partitions, all_partitions))
        
        partition_xyz = partitioner.key_partitioner(key_xyz, all_partitions, all_partitions)
        self.assertEqual(partition_xyz, partitioner.key_partitioner(key_xyz, all_partitions, all_partitions))

        partition_tess = partitioner.key_partitioner(key_tess, all_partitions, all_partitions)
        self.assertEqual(partition_tess, partitioner.key_partitioner(key_tess, all_partitions, all_partitions))

        print("============ key_partitioner =============")
        print("Key = {} - Partition = {}".format(key_abc, partition_abc))
        print("Key = {} - Partition = {}".format(key_xyz, partition_xyz))
        print("Key = {} - Partition = {}".format(key_tess, partition_tess))
        print("Key = {} - Partition = {}".format(key_abc, partitioner.key_partitioner(key_abc, all_partitions, all_partitions)))
        print("Key = {} - Partition = {}".format(key_xyz, partitioner.key_partitioner(key_xyz, all_partitions, all_partitions)))
        print("Key = {} - Partition = {}".format(key_tess, partitioner.key_partitioner(key_tess, all_partitions, all_partitions)))


    #
    def test_sticky_key_partitioner(self):
        partitioner = DataPartitioner(None)
        all_partitions = [0,1,2,3,4,5,6,7,8,9]
        key_abc = "ABC"
        key_xyz = "XYZ"
        key_tess = "TESS"

        partition_abc = partitioner.sticky_key_partitioner(key_abc, all_partitions, all_partitions)
        self.assertEqual(partition_abc, partitioner.sticky_key_partitioner(key_abc, all_partitions, all_partitions))
        
        partition_xyz = partitioner.sticky_key_partitioner(key_xyz, all_partitions, all_partitions)
        self.assertEqual(partition_xyz, partitioner.sticky_key_partitioner(key_xyz, all_partitions, all_partitions))

        partition_tess = partitioner.sticky_key_partitioner(key_tess, all_partitions, all_partitions)
        self.assertEqual(partition_tess, partitioner.sticky_key_partitioner(key_tess, all_partitions, all_partitions))

        print("============ sticky_key_partitioner =============")
        print("Key = {} - Partition = {}".format(key_abc, partition_abc))
        print("Key = {} - Partition = {}".format(key_xyz, partition_xyz))
        print("Key = {} - Partition = {}".format(key_tess, partition_tess))
        print("Key = {} - Partition = {}".format(key_abc, partitioner.sticky_key_partitioner(key_abc, all_partitions, all_partitions)))
        print("Key = {} - Partition = {}".format(key_xyz, partitioner.sticky_key_partitioner(key_xyz, all_partitions, all_partitions)))
        print("Key = {} - Partition = {}".format(key_tess, partitioner.sticky_key_partitioner(key_tess, all_partitions, all_partitions)))

    #
    def test_hash_partitioner(self):
        partitioner = DataPartitioner(None)
        all_partitions = [0,1,2,3,4,5,6,7,8,9]
        key_abc = "ABC"
        key_xyz = "XYZ"
        key_tess = "TESS"

        partition_abc = partitioner.hash_partitioner(key_abc, all_partitions, all_partitions)
        self.assertEqual(partition_abc, partitioner.hash_partitioner(key_abc, all_partitions, all_partitions))
        
        partition_xyz = partitioner.hash_partitioner(key_xyz, all_partitions, all_partitions)
        self.assertEqual(partition_xyz, partitioner.hash_partitioner(key_xyz, all_partitions, all_partitions))

        partition_tess = partitioner.hash_partitioner(key_tess, all_partitions, all_partitions)
        self.assertEqual(partition_tess, partitioner.hash_partitioner(key_tess, all_partitions, all_partitions))

        print("============ hash_partitioner =============")
        print("Key = {} - Partition = {}".format(key_abc, partition_abc))
        print("Key = {} - Partition = {}".format(key_xyz, partition_xyz))
        print("Key = {} - Partition = {}".format(key_tess, partition_tess))
        print("Key = {} - Partition = {}".format(key_abc, partitioner.hash_partitioner(key_abc, all_partitions, all_partitions)))
        print("Key = {} - Partition = {}".format(key_xyz, partitioner.hash_partitioner(key_xyz, all_partitions, all_partitions)))
        print("Key = {} - Partition = {}".format(key_tess, partitioner.hash_partitioner(key_tess, all_partitions, all_partitions)))


if __name__ == '__main__':
    unittest.main()