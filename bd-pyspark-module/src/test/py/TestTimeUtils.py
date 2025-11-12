import unittest
from com.example.utils.TimeUtils import TimeUtils

class TestTimeUtils(unittest.TestCase):

    #
    def test_getTime(self):
        tutils = TimeUtils()
        dt = tutils.getTime()
        self.assertIsNotNone(dt)

    #
    def test_unix_time_millis(self):
        tutils = TimeUtils()
        from datetime import datetime
        dt = datetime(2021, 8, 2, 12, 0, 0)
        millis = tutils.unix_time_millis(dt)
        self.assertEqual(millis, 1627905600000.0)
        
if __name__ == '__main__':
    unittest.main()