#!/bin/bash

#
#
REALM=SANDBOX.NET
DOMAIN_REALM=sandbox.net
ROOT_ADMIN_PRINCIPAL=root/admin@SANDBOX.NET
KADMIN_PRINCIPAL_FULL=kadmin/admin@SANDBOX.NET
KADMIN_PASSWORD=kadmin
KUSERS_PASSWORD=kuser

echo ""
echo "Adding kafka cluster kerberos principals."
echo ""

declare -A KAFKA_USERS=(
  [zookeeper]="zookeeper/zookeeper.sandbox.net"
  [zookeeper-a]="zookeeper/zookeeper-a.sandbox.net"
  [zookeeper-b]="zookeeper/zookeeper-b.sandbox.net"
  [zookeeper-c]="zookeeper/zookeeper-c.sandbox.net"
  [zkclient]="zkclient"
  #
  [kafkabroker]="kafka/kafkabroker.sandbox.net"
  [kafkabroker-a]="kafka/kafkabroker-a.sandbox.net"
  [kafkabroker-b]="kafka/kafkabroker-b.sandbox.net"
  [kafkabroker-c]="kafka/kafkabroker-c.sandbox.net"
  [schemaregistry]="schemaregistry/schemaregistry.sandbox.net"
  #
  [consumer]="consumer"
  [producer]="producer"
  [kafkaclient]="kafkaclient"
)
# for key in "${!KAFKA_USERS[@]}"; do echo "Key :- $key Value:- ${KAFKA_USERS[$key]}"; done
for key in "${!KAFKA_USERS[@]}"
do
  # delete existing keytab files
  rm -Rf /apps/security/keytabs/services/$key.keytab
  kadmin.local -q "delete_principal -force ${KAFKA_USERS[$key]}@$REALM"
  kadmin.local -q "addprinc -randkey ${KAFKA_USERS[$key]}@$REALM"
  kadmin.local -q "ktadd -k /apps/security/keytabs/services/$key.keytab ${KAFKA_USERS[$key]}@$REALM"
  chmod 444 /apps/security/keytabs/services/$key.keytab
done

#
echo ""
echo "Kafka cluster kerberos principles successfully generated."
echo ""

#
# Merge Keytab
#
rm -Rf /apps/security/keytabs/services/kafka.keytab
ktmerge=""
ktmerge+="rkt /apps/security/keytabs/services/zookeeper.keytab\n"
ktmerge+="rkt /apps/security/keytabs/services/zkclient.keytab\n"
ktmerge+="rkt /apps/security/keytabs/services/kafkabroker.keytab\n"
ktmerge+="rkt /apps/security/keytabs/services/schemaregistry.keytab\n"
ktmerge+="rkt /apps/security/keytabs/services/kafkaclient.keytab\n"
ktmerge+="wkt /apps/security/keytabs/services/kafka.keytab\nquit"
echo ""
echo -e "$ktmerge" | ktutil
chmod 444 /apps/security/keytabs/services/kafka.keytab
