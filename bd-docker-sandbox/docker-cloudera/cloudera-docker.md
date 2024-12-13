sudo docker run --hostname=quickstart.cloudera --privileged=true -t -i -v /home/chris/clouderacontainer:/src -m 12G -p 80:80 -p 8888:8888 -p 50070:50070 -p 1004:1004 -p 1006:1006 -p 7180:7180 -p 8020:8020 -p 8990:8990 -p 21050:21050 -p 88:88 -p 88:88/udp cloudera/quickstart /usr/bin/docker-quickstart


docker run -m 8G --hostname=cloudera.sandbox-cdh:6.3.0 --privileged=true -t -i --publish-all=true -p 8888 -p 7180:7180  39f9547152c6  /usr/sbin/int docker-clouderasandbox-cdh:6.3.0 --platform amd64

docker run -m 8G --hostname=quickstart.cloudera --privileged=true -t -i --publish-all=true -p 8888 -p 7180:7180  cloudera/quickstart  /usr/bin/docker-quickstart --platform amd64

#
#
#
docker run --privileged --name sandbox-cdh -h sandbox-cdh --network=cda --network-alias=sandbox-cdh --publish-all=true -d cloudera/sandbox-cdh:6.3.0

docker exec -it sandbox-cdh /bin/bash

docker exec -it sandbox-cdh /bin/bash
#
#
#
cat /etc/cloudera-scm-agent/config.ini

sudo -u cloudera-scm systemctl status cloudera-scm-agent

sudo -u cloudera-scm systemctl status cloudera-scm-server

sudo -u cloudera-scm systemctl status cloudera-scm-server-db -l

sudo -u cloudera-scm systemctl start cloudera-scm-server-db


#
#
#

sudo systemctl start postgresql
sudo systemctl restart postgresql
sudo systemctl stats postgresql
sudo systemctl stop postgresql

# /opt/cloudera/cm/bin/initialize_embedded_db.sh

su -s /bin/bash cloudera-scm -c "pg_ctl start -w -D /var/lib/cloudera-scm-server-db/data -l /var/log/cloudera-scm-server/db.log -o \"-k /var/run/cloudera-scm-server/\""


sudo -u postgres psql -l


echo 'LC_ALL="en_US.UTF-8"' >> /etc/locale.conf
sudo su -l postgres -c "postgresql-setup initdb"

#
# head -1  /var/lib/cloudera-scm-server-db/data/generated_password.txt
#

Xel9Y60fMQ

$SU -c "pg_ctl start -w -D /var/lib/cloudera-scm-server-db/data -l $SERVER_LOGFILE -o \"$EXTRA_PG_ARGS\""


sudo -u postgres psql
CREATE ROLE scm LOGIN PASSWORD 'E8X5b6DylD';
CREATE DATABASE scm OWNER scm ENCODING 'UTF8';
ALTER DATABASE scm SET standard_conforming_strings=off;


sudo /opt/cloudera/cm/schema/scm_prepare_database.sh postgresql -h sandbox-cdh scm scm E8X5b6DylD

sudo cat /etc/cloudera-scm-server/db.mgmt.properties
sudo mv /etc/cloudera-scm-server/db.mgmt.properties /etc/cloudera-scm-server/db.mgmt.properties.old


com.cloudera.cmf.ACTIVITYMONITOR.db.type=postgresql
com.cloudera.cmf.ACTIVITYMONITOR.db.host=sandbox-cdh:7432
com.cloudera.cmf.ACTIVITYMONITOR.db.name=amon
com.cloudera.cmf.ACTIVITYMONITOR.db.user=amon
com.cloudera.cmf.ACTIVITYMONITOR.db.password=3SRrmQF88H
#
com.cloudera.cmf.REPORTSMANAGER.db.type=postgresql
com.cloudera.cmf.REPORTSMANAGER.db.host=sandbox-cdh:7432
com.cloudera.cmf.REPORTSMANAGER.db.name=rman
com.cloudera.cmf.REPORTSMANAGER.db.user=rman
com.cloudera.cmf.REPORTSMANAGER.db.password=MJvToiUVd4
#
com.cloudera.cmf.NAVIGATOR.db.type=postgresql
com.cloudera.cmf.NAVIGATOR.db.host=sandbox-cdh:7432
com.cloudera.cmf.NAVIGATOR.db.name=nav
com.cloudera.cmf.NAVIGATOR.db.user=nav
com.cloudera.cmf.NAVIGATOR.db.password=XEkv5IrSlk
#
com.cloudera.cmf.NAVIGATORMETASERVER.db.type=postgresql
com.cloudera.cmf.NAVIGATORMETASERVER.db.host=sandbox-cdh:7432
com.cloudera.cmf.NAVIGATORMETASERVER.db.name=navms
com.cloudera.cmf.NAVIGATORMETASERVER.db.user=navms
com.cloudera.cmf.NAVIGATORMETASERVER.db.password=YDTGUo1lVi


CREATE ROLE amon LOGIN PASSWORD '3SRrmQF88H';
CREATE DATABASE amon OWNER amon ENCODING 'UTF8';

CREATE ROLE rman LOGIN PASSWORD 'MJvToiUVd4';
CREATE DATABASE rman OWNER rman ENCODING 'UTF8';

CREATE ROLE nav LOGIN PASSWORD 'XEkv5IrSlk';
CREATE DATABASE nav OWNER nav ENCODING 'UTF8';

CREATE ROLE navms LOGIN PASSWORD 'YDTGUo1lVi';
CREATE DATABASE navms OWNER navms ENCODING 'UTF8';