import unittest
from com.example.models.User import User
from com.example.utils.AvroUtils import load_avro_schema, load_avro_str, load_avro_json

class TestAvroUtils(unittest.TestCase):

    #
    def test_load_avro_schema(self):
        key_schema, value_schema = load_avro_schema('./resources/avro/user-record.avsc')
        self.assertTrue(len(str(key_schema)) > 0)
        self.assertTrue(len(str(value_schema)) > 0)
        
    #
    def test_load_avro_str(self):
        key_schema_str, value_schema_str = load_avro_str('./resources/avro/user-record.avsc')
        self.assertTrue(len(key_schema_str) > 0)
        self.assertTrue(len(value_schema_str) > 0)

    #
    def test_load_avro_json(self):
        key_schema_str, value_schema_str = load_avro_json('./resources/avro/user-record.avsc')
        self.assertTrue(len(key_schema_str) > 0)
        self.assertTrue(len(value_schema_str) > 0)

if __name__ == '__main__':
    unittest.main()