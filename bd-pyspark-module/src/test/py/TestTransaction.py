from com.example.models.Transaction import Transaction
from faker import Faker

def test_random():
    for _ in range(100):
        pass

    transaction = Transaction.random()
    record_key = str(transaction.uuid)
    record_value = str(transaction.to_dict())
    # json.dumps() function converts a Python object into a json string.
    # record_value = json.dumps({'count': random.randint(1000, 5000)})
    print("{}\t{}".format(record_key, record_value))
    #assert False


def test_dict_to_name():
    assert False


def test_name_to_dict():
    assert False


def test_to_dict():
    assert False


def test_to_json():
    assert False


def test_to_delimited_text():
    assert False
