from com.example.models.User import User


def test_metaphone():
    u = User.random()
    assert u is not None
