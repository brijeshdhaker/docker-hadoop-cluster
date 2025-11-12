import time
from datetime import datetime
class TimeUtils:

    def __init__(self):
        pass

    def unix_time_millis(self, dt):
        epoch = datetime.utcfromtimestamp(0)
        return (dt - epoch).total_seconds() * 1000.0

    def getTime(self): 
        t2 = time.time()
        print(t2)
        print(time.gmtime(0))
        print()
        # localtime
        obj = time.localtime(1627987508.6496193)
        print(obj)
        
        s = time.strftime("%a, %d %b %Y %H:%M:%S", time.gmtime(1627987508.6496193))
        print(s)
        return s
