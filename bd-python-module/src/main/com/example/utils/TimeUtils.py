import time
from datetime import datetime

def unix_time_millis(dt):
    epoch = datetime.utcfromtimestamp(0)
    return (dt - epoch).total_seconds() * 1000.0

def getTime() :
    t2 = time.time()
    print(t2)
    print(time.gmtime(0))
    print()
    # localtime
    obj = time.localtime(1627987508.6496193)
    print(obj)
    from time import gmtime, strftime
    s = strftime("%a, %d %b %Y %H:%M:%S",
             gmtime(1627987508.6496193))
    print(s)

getTime()