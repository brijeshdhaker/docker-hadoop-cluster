#!/bin/bash

echo "==================================================================================="
echo "==== Kerberos KDC and Kadmin ======================================================"
echo "==================================================================================="
KADMIN_PRINCIPAL_FULL=$KADMIN_PRINCIPAL@$REALM

echo "REALM: $REALM"
echo "KADMIN_PRINCIPAL_FULL: $KADMIN_PRINCIPAL_FULL"
echo "KADMIN_PASSWORD: $KADMIN_PASSWORD"
echo ""

echo "==================================================================================="
echo "==== /etc/krb5.conf ==============================================================="
echo "==================================================================================="
KDC_KADMIN_SERVER=$(hostname -f)
tee /etc/krb5.conf <<EOF
[libdefaults]
	default_realm = $REALM
[realms]
	$REALM = {
		kdc_ports = 88,750
		kadmind_port = 749
		kdc = $KDC_KADMIN_SERVER
		admin_server = $KDC_KADMIN_SERVER
	}
EOF
echo ""

echo "==================================================================================="
echo "==== /etc/krb5kdc/kdc.conf ========================================================"
echo "==================================================================================="
tee /etc/krb5kdc/kdc.conf <<EOF
[kdcdefaults]
    kdc_ports = 88

[realms]
	$REALM = {
		acl_file = /etc/krb5kdc/kadm5.acl
		kadmind_port = 749
		max_life = 12h 0m 0s
		max_renewable_life = 7d 0h 0m 0s
		master_key_type = aes256-cts-hmac-sha1-96
		supported_enctypes = aes256-cts-hmac-sha1-96:normal aes128-cts-hmac-sha1-96:normal
		default_principal_flags = +preauth
	}
EOF
echo ""

echo "==================================================================================="
echo "==== /etc/krb5kdc/kadm5.acl ======================================================="
echo "==================================================================================="
tee /etc/krb5kdc/kadm5.acl <<EOF
$KADMIN_PRINCIPAL_FULL *
noPermissions@$REALM X
EOF
echo ""

echo "==================================================================================="
echo "==== Creating realm ==============================================================="
echo "==================================================================================="
MASTER_PASSWORD=$(tr -cd '[:alnum:]' < /dev/urandom | fold -w30 | head -n1)
# This command also starts the krb5-kdc and krb5-admin-server services
krb5_newrealm <<EOF
$MASTER_PASSWORD
$MASTER_PASSWORD
EOF
echo ""

echo "==================================================================================="
echo "==== Creating default principals in the acl ======================================="
echo "==================================================================================="
if [ ! -f /var/lib/krb5kdc/.already_setup ]; then

  echo "Adding $KADMIN_PRINCIPAL principal"
  kadmin.local -q "delete_principal -force $KADMIN_PRINCIPAL_FULL"
  echo ""
  kadmin.local -q "addprinc -pw $KADMIN_PASSWORD $KADMIN_PRINCIPAL_FULL"
  echo ""

  echo "Adding noPermissions principal"
  kadmin.local -q "delete_principal -force noPermissions@$REALM"
  echo ""
  kadmin.local -q "addprinc -pw $KADMIN_PASSWORD noPermissions@$REALM"
  echo ""

  echo "Adding User Admin Principal"
  echo ""
  kadmin.local -q "delete_principal -force root/admin@SANDBOX-BIGDATA.NET"
  kadmin.local -q "delete_principal -force brijeshdhaker/admin@SANDBOX-BIGDATA.NET"
  echo ""
  kadmin.local -q "addprinc -pw root root/admin@SANDBOX-BIGDATA.NET"
  kadmin.local -q "addprinc -pw brijeshdhaker brijeshdhaker/admin@SANDBOX-BIGDATA.NET"

  echo "Adding Service Principals"
  echo ""
  kadmin.local -q "delete_principal -force nn/namenode@SANDBOX-BIGDATA.NET"
  kadmin.local -q "delete_principal -force dn/datanode@SANDBOX-BIGDATA.NET"
  kadmin.local -q "delete_principal -force sn/secondaynamenode@SANDBOX-BIGDATA.NET"
  kadmin.local -q "delete_principal -force rm/resourcemanager@SANDBOX-BIGDATA.NET"
  kadmin.local -q "delete_principal -force nm/nodemanager@SANDBOX-BIGDATA.NET"
  kadmin.local -q "delete_principal -force jhs/historyserver@SANDBOX-BIGDATA.NET"
  kadmin.local -q "delete_principal -force yts/timelineserver@SANDBOX-BIGDATA.NET"
  kadmin.local -q "delete_principal -force gw/gateway@SANDBOX-BIGDATA.NET"

  kadmin.local -q "delete_principal -force host/namenode@SANDBOX-BIGDATA.NET"
  kadmin.local -q "delete_principal -force host/datanode@SANDBOX-BIGDATA.NET"
  kadmin.local -q "delete_principal -force host/secondaynamenode@SANDBOX-BIGDATA.NET"
  kadmin.local -q "delete_principal -force host/resourcemanager@SANDBOX-BIGDATA.NET"
  kadmin.local -q "delete_principal -force host/nodemanager@SANDBOX-BIGDATA.NET"
  kadmin.local -q "delete_principal -force host/historyserver@SANDBOX-BIGDATA.NET"
  kadmin.local -q "delete_principal -force host/timelineserver@SANDBOX-BIGDATA.NET"
  kadmin.local -q "delete_principal -force host/gateway@SANDBOX-BIGDATA.NET"

  echo ""

  kadmin.local -q "addprinc -pw nn nn/namenode@SANDBOX-BIGDATA.NET"
  kadmin.local -q "addprinc -pw dn dn/datanode@SANDBOX-BIGDATA.NET"
  kadmin.local -q "addprinc -pw sn sn/secondaynamenode@SANDBOX-BIGDATA.NET"
  kadmin.local -q "addprinc -pw rm rm/resourcemanager@SANDBOX-BIGDATA.NET"
  kadmin.local -q "addprinc -pw nm nm/nodemanager@SANDBOX-BIGDATA.NET"
  kadmin.local -q "addprinc -pw jhs jhs/historyserver@SANDBOX-BIGDATA.NET"
  kadmin.local -q "addprinc -pw yts yts/timelineserver@SANDBOX-BIGDATA.NET"
  kadmin.local -q "addprinc -pw gw gw/gateway@SANDBOX-BIGDATA.NET"

  kadmin.local -q "addprinc -pw nn host/namenode@SANDBOX-BIGDATA.NET"
  kadmin.local -q "addprinc -pw dn host/datanode@SANDBOX-BIGDATA.NET"
  kadmin.local -q "addprinc -pw sn host/secondaynamenode@SANDBOX-BIGDATA.NET"
  kadmin.local -q "addprinc -pw rm host/resourcemanager@SANDBOX-BIGDATA.NET"
  kadmin.local -q "addprinc -pw nm host/nodemanager@SANDBOX-BIGDATA.NET"
  kadmin.local -q "addprinc -pw jhs host/historyserver@SANDBOX-BIGDATA.NET"
  kadmin.local -q "addprinc -pw yts host/timelineserver@SANDBOX-BIGDATA.NET"
  kadmin.local -q "addprinc -pw gw host/gateway@SANDBOX-BIGDATA.NET"

  kadmin.local -q "addprinc -pw hive hive/hive-server@SANDBOX-BIGDATA.NET"
  kadmin.local -q "addprinc -pw spark spark/spark@SANDBOX-BIGDATA.NET"
  kadmin.local -q "addprinc -pw yarn yarn/spark@SANDBOX-BIGDATA.NET"

  echo ""
  touch /var/lib/krb5kdc/.already_setup
  echo "Principles successfully generated."
  echo ""
else
  echo ""
  echo "Principles already generated."
  echo ""
fi

krb5kdc
kadmind -nofork