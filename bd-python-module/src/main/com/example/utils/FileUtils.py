
import os
from os.path import expanduser
from pathlib import Path

# Report malformed record, discard results, continue polling
# FILE_PATH = os.path.join(expanduser("~"), "IdeaProjects", "spark-python-examples", "resources", "avro", "user-record.avsc")
# AVRO_PATH = Path(expanduser("~"), "IdeaProjects", "spark-python-examples","resources","avro","user-record.avsc")
def read(file_path):

    if os.path.exists(file_path):
        f = open(file_path, "r")
        for x in f:
            print(x)
        f.close()
    else:
        print("{} file does not exist".format(file_path))


def delete(file_path):
    if os.path.exists(file_path):
        os.remove(file_path)
    else:
        print("{} file does not exist".format(file_path))


if __name__ == "__main__":
    print("Executed when invoked directly")
    readFile("resources/avro/user-record.avsc")