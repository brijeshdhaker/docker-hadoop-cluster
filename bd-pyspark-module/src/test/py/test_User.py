from com.example.models.User import User
def test_random():
    u = User.random()
    assert len(u.uuid) > 0


def test_to_obj():
    u = User.random()
    d = User.to_dict(u,None)
    u1 = User.to_obj(d)
    assert len(u1.uuid) > 0


def test_to_dict():
    u = User.random()
    u.__dict__
    assert False
