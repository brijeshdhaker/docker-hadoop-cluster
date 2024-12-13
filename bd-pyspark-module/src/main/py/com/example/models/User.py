#
#
#

import pytz
import random
from datetime import datetime
from faker import Faker
from uuid import uuid4


#
class User(object):


    # Use __slots__ to explicitly declare all data members.
    __slots__ = ["id", "uuid", "name", "emailAddr", "age",  "dob", "height", "roles", "status", "addTs", "updTs"]

    # Class Variable


    # The init method or constructor
    def __init__(self, uuid=None):
        event_datetime = datetime.now(pytz.utc)
        ist_event_datetime = datetime.now(pytz.timezone('Asia/Kolkata'))
        self.addTs = int(event_datetime.strftime("%s"))
        self.updTs = int(event_datetime.strftime("%s"))
        if uuid is None:
            self.uuid = str(uuid4())
        else:
            self.uuid = uuid


    #
    #
    @staticmethod
    def random():

        d1 = datetime.strptime('1/1/2008 1:30 PM', '%m/%d/%Y %I:%M %p')
        d2 = datetime.strptime('1/1/2009 4:50 AM', '%m/%d/%Y %I:%M %p')
        event_datetime = datetime.now().timestamp()
        int(event_datetime)

        #print(random_date(d1, d2))
        fake = Faker()
        u = User()
        u.id = random.randint(1000, 5000)
        u.name = fake.name()
        u.emailAddr = fake.email()
        u.age= random.randint(18, 99)
        u.dob= random.randint(60, 90)
        u.height = round(random.uniform(5.0, 7.0))
        u.roles = ['Admin', 'Technology']
        u.status = 'Active'
        return u

    #
    #
    #
    @staticmethod
    def to_obj(d, ctx=None):
        if d is None:
            return None
        u = User()
        u.id = d['id']
        u.uuid = d['uuid']
        u.name = d['name']
        u.emailAddr = d['emailAddr']
        u.age= d['age']
        u.dob= d['dob']
        u.height = d['height']
        u.roles = d['roles']
        u.status = d['status']
        u.addTs = d['addTs']
        u.updTs= d['updTs']
        return u

    #
    #
    #
    @staticmethod
    def to_dict(obj, ctx=None):
        if obj is None:
            return None
        # _udicti  =  obj.__dict__
        # _udictv  =  vars(obj)
        return dict(
            id=obj.id,
            uuid=obj.uuid,
            name=obj.name,
            emailAddr= obj.emailAddr,
            age= obj.age,
            dob=obj.dob,
            height=obj.height,
            roles=obj.roles,
            status=obj.status,
            addTs=obj.addTs,
            updTs=obj.updTs
        )

