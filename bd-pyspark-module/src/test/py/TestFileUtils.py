import unittest
from com.example.utils.FileUtils import FileUtils

class TestFileUtils(unittest.TestCase):

    #
    def test_read(self):
        fileUtils = FileUtils()
        fileUtils.read("./resources/avro/user-record.avsc")
        self.assertIsNotNone(2)

    #
    def test_delete(self):
        fileUtils = FileUtils()
        fileUtils.delete("./resources/avro/user-record.avsc")
        

        
if __name__ == '__main__':
    unittest.main()