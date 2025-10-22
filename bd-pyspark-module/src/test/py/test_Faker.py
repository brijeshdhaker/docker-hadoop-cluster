from faker import Faker

def test__Faker():
    fake = Faker("en_IN")
    #fake.randint(1, 100)
    print(fake.email())
    print(fake.country())
    print(fake.name())
    print(fake.text())
    print(fake.latitude(), fake.longitude())
    print(fake.url())
    print(fake.address())
    print(str(fake.latitude()))
    print(str(fake.longitude()))
    print(fake.city())
    print(fake.postcode())
    print(fake.currency())
    print(fake.currency_code())
    print(fake.currency_name())