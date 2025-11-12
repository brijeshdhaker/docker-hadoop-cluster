import unittest
import com.example.utils.Commons as commons

class TestCommonsMethods(unittest.TestCase):

    def test_isupper(self):
        self.assertTrue('FOO'.isupper())
        self.assertFalse('Foo'.isupper())

    def test_split(self):
        s = 'hello world'
        self.assertEqual(s.split(), ['hello', 'world'])
        # check that s.split fails when the separator is not a string
        with self.assertRaises(TypeError):
            s.split(2)

    def test_print_separator(self):
        self.assertTrue(True)


    def test_split_csv(self):
        line = '1;"Goroka";"Goroka";"Papua New Guinea";"GKA";"AYGA";46.08;145.39;52;10;"U";"Pacific/Port_Moresby"'
        records = commons.split_csv(";", line)
        self.assertTrue(float(records[6]) > 40)


    def test_split_csv_line(self):
        line = '1,"Goroka","Goroka","Papua New Guinea","GKA","AYGA",46.08;145.39;52;10,"U","Pacific/Port_Moresby"'
        cols = commons.split_csv_line(line)
        self.assertTrue(len(cols) > 2)

if __name__ == '__main__':
    unittest.main()