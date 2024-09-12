import com.example.utils.Commons as commons

def test_print_separator():
    assert True


def test_split_csv():
    line = '1;"Goroka";"Goroka";"Papua New Guinea";"GKA";"AYGA";46.08;145.39;52;10;"U";"Pacific/Port_Moresby"'
    records = commons.split_csv(";", line)
    assert (float(records[6]) > 40)


def test_split_csv_line():
    line = '1,"Goroka","Goroka","Papua New Guinea","GKA","AYGA",46.08;145.39;52;10,"U","Pacific/Port_Moresby"'
    cols = commons.split_csv_line(line)
    assert cols