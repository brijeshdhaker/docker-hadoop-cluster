#
# MySql Password
#
admin/hortonworks1

#
# Ranger Password
#
admin/hortonworks1

#
# sandbox-hdp.hortonworks.com
#
docker run --name nifi \
  -p 19090:8080 \
  -d \
  hortonworks/nifi:latest

http://sandbox-hdp.hortonworks.com:19090/nifi

#
#
#
bin/nifi.sh set-single-user-credentials nifi-admin nifiadmin1234$