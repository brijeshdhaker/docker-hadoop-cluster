

class Order(object):

    # The *args will give you all positional arguments as a tuple:

    # The **kwargs will give you all keyword arguments as a dictionary:
    def __init__(self, **kwargs):
        for name, value in kwargs.items():
            setattr(self, name, self._wrap(value))

        # for key, value in kwargs.items():
        #     if isinstance(value, dict):
        #         self.__dict__[key] = Order(**value)
        #     else:
        #         self.__dict__[key] = value

    def _wrap(self, value):
        if isinstance(value, (tuple, list, set, frozenset)):
            return type(value)([self._wrap(v) for v in value])
        else:
            return Order(value) if isinstance(value, dict) else value


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