from com.example.models.Order import Order

def test__wrap():
    my_dict = {
        'name': 'bobbyhadz',
        'address': {
            'country': 'Country A',
            'city': 'City A',
            'codes': [1, 2, 3]
        },
    }

    obj = Order(**my_dict)

    print(obj.address.country)  #  Country A
    print(obj.address.codes)  #  [1, 2, 3]
    print(obj.name)  #  bobbyhadz
    # assert False
