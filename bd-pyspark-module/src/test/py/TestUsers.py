import unittest
from com.example.models.User import User

class TestUsers(unittest.TestCase):

    #
    def test_random(self):
        u = User.random()
        self.assertTrue(len(u.uuid) > 0)
        
    #
    def test_to_obj(self):
        u = User.random()
        d = User.to_dict(u,None)
        u1 = User.to_obj(d)
        self.assertTrue(len(u1.uuid) > 0)

    #
    def test_to_dict(self):
        u = User.random()
        d = User.to_dict(u,None)
        self.assertTrue(len(d.values()) > 0)

if __name__ == '__main__':
    unittest.main()