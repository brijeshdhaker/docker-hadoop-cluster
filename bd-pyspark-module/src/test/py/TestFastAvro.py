import unittest
from fastavro import schemaless_writer, schemaless_reader, parse_schema, json_reader, json_writer, writer, reader, block_reader
from io import BytesIO

class TestFastAvro(unittest.TestCase):
    #
    schema = {
        'doc': 'A weather reading.',
        'name': 'Weather',
        'namespace': 'test',
        'type': 'record',
        'fields': [
            {'name': 'station', 'type': 'string'},
            {'name': 'time', 'type': 'long'},
            {'name': 'temp', 'type': 'int'},
        ],
    }
    
    records = [
        {u'station': u'011990-99999', u'temp': 0, u'time': 1433269388},
        {u'station': u'011990-99999', u'temp': 22, u'time': 1433270389},
        {u'station': u'011990-99999', u'temp': -11, u'time': 1433273379},
        {u'station': u'012650-99999', u'temp': 111, u'time': 1433275478},
    ]

    #
    def test_schemaless_writer(self):
        rb = BytesIO()
        parsed_schema = parse_schema(self.schema)
        schemaless_writer(rb, parsed_schema, {u'station': u'011990-99999', u'temp': 0, u'time': 1433269388})
        raw_bytes = rb.getvalue()  # b'\x0cAlyssa\x00\x80\x04\x02'
        print(raw_bytes)
        self.assertTrue(len(raw_bytes) > 0)
        
    #
    def test_schemaless_reader(self):
        
        parsed_schema = parse_schema(self.schema)

        rb = BytesIO()
        rb = BytesIO(b'\x18011990-99999\x98\xd2\xef\xd6\n\x00')
        data = schemaless_reader(rb, parsed_schema)
        print(data)
        self.assertEqual(data['station'], '011990-99999')
        self.assertEqual(data['time'], 1433269388)
        self.assertEqual(data['temp'], 0)
        """ 
        with open('./resources/avro/weather.avro', 'wb') as fp:
            record = schemaless_reader(rb, parsed_schema)
            print(record)
        """

    #
    def test_writer(self):
        #
        parsed_schema = parse_schema(self.schema)
        
        fo = BytesIO()
        writer(fo, parsed_schema, self.records)
        fo.seek(0)
        for record in reader(fo):
            print(record)
 
    #
    def test_avro_writer(self):
        #
        parsed_schema = parse_schema(self.schema)
        with open('./resources/avro/weather.avro', 'wb') as out:
            writer(out, parsed_schema, self.records)
        
        fo = BytesIO()
        writer(fo, parsed_schema, self.records)
        fo.seek(0)
        for record in reader(fo):
            print(record)
    
    #
    def test_avro_reader(self):
        buffer = BytesIO()
        with open('./resources/avro/weather.avro', 'rb') as fo:
            avro_reader = reader(fo)
            for record in avro_reader:
                print(record)

    #
    def test_block_reader(self):
        with open('./resources/avro/weather.avro', 'rb') as fo:
            avro_blocks = block_reader(fo)
            for block in avro_blocks:
                print(f"Processing block with {block.num_records} records.")
                # Each 'block' object is an iterator that yields records
                for record in block:
                    print(record)
                # You can also access block metadata like writer_schema, codec, etc.
                    print(f"Block writer schema: {block.writer_schema}")
                    print(f"Block codec: {block.codec}")

    #
    def test_json_writer(self):
        #
        parsed_schema = parse_schema(self.schema)
        with open('./resources/json/records.json', 'w') as out:
            json_writer(out, parsed_schema, self.records)        

    #
    def test_json_reader(self):
        parsed_schema = parse_schema(self.schema)
        with open('./resources/json/records.json', 'r') as fo:
            reader = json_reader(fo, parsed_schema)
            for record in reader:
                print(record)


if __name__ == '__main__':
    unittest.main()






