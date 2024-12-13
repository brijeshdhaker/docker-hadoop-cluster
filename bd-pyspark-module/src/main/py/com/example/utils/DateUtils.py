import datetime
def getCurrentDate() :
    # 2024-02-28 09:05:44.601233
    x = datetime.datetime.now()
    return x

def getMonth() :
    x = datetime.datetime(2018, 6, 1)
    print(x.strftime("%B"))

