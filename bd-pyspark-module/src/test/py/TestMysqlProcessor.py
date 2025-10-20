import unittest
from com.example.mysql.MysqlProcessor import MysqlProcessor

class TestUsers(unittest.TestCase):

    #
    @classmethod
    def setUpClass(cls):
        cls.clsVar = 1
        print(f"cls.clsVar :: {cls.clsVar}")

    @classmethod
    def tearDownClass(cls):
        cls.clsVar = 0
        print(f"cls.clsVar :: {cls.clsVar}")
    #
    def setUp(self):
        self.processor = MysqlProcessor()
        self.processor.execute(sql="CREATE TABLE customers (name VARCHAR(255), address VARCHAR(255))")

    #
    def tearDown(self):
        cnt = self.processor.execute(sql="DROP TABLE customers")
        self.assertTrue(cnt == 0)
        self.processor.close()

    #
    def test_init(self):
        self.assertIsNotNone(self.processor.connection, "Pass")

    #
    def test_fetchAll(self):
        results = self.processor.fetchAll(sql="select * from KAFKA_OFFSETS")
        #for row in results:
        #    print(row)
        self.assertIsNotNone(results, "Pass")
        self.assertTrue(len(results) > 0)

    #
    def test_insert(self):
        sql = "INSERT INTO customers (name, address) VALUES (%s, %s)"
        val = [
            ('Peter', 'Lowstreet 4'),
            ('Amy', 'Apple st 652'),
            ('Hannah', 'Mountain 21'),
            ('Michael', 'Valley 345'),
            ('Sandy', 'Ocean blvd 2'),
        ]
        cnt = self.processor.insert(sql=sql, values=val)
        self.assertEqual(cnt,5)

    #
    def test_insertBlank(self):
        sql = "INSERT INTO customers (name, address) VALUES (%s, %s)"
        # check that s.split fails when the separator is not a string
        with self.assertRaises(TypeError):
            self.processor.insert(sql=sql, values=None)

    #
    def test_insertBlankSql(self):
        sql = ""
        values = [
            ('Chuck', 'Main Road 989'),
            ('Viola', 'Sideway 1633')
        ]
        # check that s.split fails when the separator is not a string
        with self.assertRaises(Exception):
            self.processor.insert(sql=sql, values=values)

    #
    def test_executeDelete(self):
        cnt = self.processor.execute(sql="DELETE FROM customers WHERE address = 'Mountain 21'")
        self.assertEqual(cnt,0)


if __name__ == '__main__':
    unittest.main()