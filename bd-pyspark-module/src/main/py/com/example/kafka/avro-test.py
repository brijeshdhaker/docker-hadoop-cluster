import os
import io, random
import avro.io


# Get the current working directory
cwd = os.getcwd()

# Open a file in the root directory
with open(os.path.join(cwd, 'resources/avro/test_schema.avsc'), 'r') as file:
    data = file.read()



# Path to user.avsc avro schema
schema_path= "resources/avro/test_schema.avsc"
schema = avro.schema.parse(open(schema_path).read())


print(data)

for i in range(1):
    writer = avro.io.DatumWriter(schema)
    bytes_writer = io.BytesIO()
    encoder = avro.io.BinaryEncoder(bytes_writer)
    writer.write({"name": "123", "favorite_color": "111", "favorite_number": random.randint(0, 10)}, encoder)
    raw_bytes = bytes_writer.getvalue()

    print(raw_bytes)

    bytes_reader = io.BytesIO(raw_bytes)
    decoder = avro.io.BinaryDecoder(bytes_reader)
    reader = avro.io.DatumReader(schema)
    user1 = reader.read(decoder)
    print(" USER = {}".format(user1))