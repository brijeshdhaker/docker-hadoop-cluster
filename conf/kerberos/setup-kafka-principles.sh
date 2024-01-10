#!/bin/bash

#
#
ROOT_ADMIN_PRINCIPAL=root/admin@SANDBOX.NET
KADMIN_PRINCIPAL_FULL=kadmin/admin@SANDBOX.NET
KADMIN_PASSWORD=kadmin
KUSERS_PASSWORD=kuser

# delete existing keytab files
echo "Adding OS User Principal"
echo ""
OS_USERS=("kafka/kafkabroker.sandbox.net" "schemaregistry/schemaregistry.sandbox.net" "zkclient" "kafkaclient")
for ou in "${OS_USERS[@]}"
do
  rm -Rf /etc/kerberos/users/$ou.keytab
  kadmin.local -q "delete_principal -force $ou@$REALM"
  kadmin.local -q "addprinc -randkey $ou@$REALM"
  ktmerge="change_password $ou@$REALM\n$KUSERS_PASSWORD\n$KUSERS_PASSWORD\n"
  echo -e "$ktmerge" | kadmin.local
  kadmin.local -q "xst -norandkey -k /etc/kerberos/users/$ou.keytab $ou@$REALM"
  chmod 444 /etc/kerberos/users/$ou.keytab
done

# Setup spnego.service.keytab
rm -Rf /etc/kerberos/keytabs/host.service.keytab
cp /etc/kerberos/keytabs/host.keytab /etc/kerberos/keytabs/host.service.keytab
chmod 444 /etc/kerberos/keytabs/host.service.keytab

rm -Rf /etc/kerberos/keytabs/HTTP.service.keytab
cp /etc/kerberos/keytabs/HTTP.keytab /etc/kerberos/keytabs/HTTP.service.keytab
chmod 444 /etc/kerberos/keytabs/HTTP.service.keytab

rm -Rf /etc/kerberos/keytabs/spnego.service.keytab
cp /etc/kerberos/keytabs/HTTP.keytab /etc/kerberos/keytabs/spnego.service.keytab
chmod 444 /etc/kerberos/keytabs/spnego.service.keytab

#
echo "Principles successfully generated."
echo ""