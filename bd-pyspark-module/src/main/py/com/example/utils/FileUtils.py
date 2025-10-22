
import os
from pathlib import Path

class FileUtils:

    def __init__(self):
        pass

    # Report malformed record, discard results, continue polling
    # FILE_PATH = os.path.join(expanduser("~"), "IdeaProjects", "spark-python-examples", "resources", "avro", "user-record.avsc")
    # AVRO_PATH = Path(expanduser("~"), "IdeaProjects", "spark-python-examples","resources","avro","user-record.avsc")
    def read(self,file_path):
        file = Path(__file__)
        parent = file.parent
        print("File : {} \n".format(file))
        print("Parent : {} \n".format(parent))
        print("Os CWD : {} \n".format(os.getcwd()))
        work_dir= os.environ.get("WORK_DIR","/home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/bd-pyspark-module/src/main/py")
        print("Working: {} \n".format(work_dir))
        file_path= work_dir+"/"+file_path
        if os.path.exists(file_path):
            f = open(file_path, "r")
            for x in f:
                print(x)
            f.close()
        else:
            print("{} file does not exist".format(file_path))


    def delete(self,file_path):
        work_dir= os.environ.get("WORK_DIR","/home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/bd-pyspark-module/src/main/py")
        print("Working: {} \n".format(work_dir))
        file_path= work_dir+"/"+file_path
        if os.path.exists(file_path):
            os.remove(file_path)
        else:
            print("{} file does not exist".format(file_path))

