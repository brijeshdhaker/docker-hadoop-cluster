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
  kadmin.local -q "addprinc -pw $KADMIN_PASSWORD $KADMIN_PRINCIPAL_FULL"
  echo ""

  echo "Adding noPermissions principal"
  kadmin.local -q "delete_principal -force noPermissions@$REALM"
  kadmin.local -q "addprinc -pw $KADMIN_PASSWORD noPermissions@$REALM"
  echo ""

  echo "Adding OS User Principal"
  echo ""
  OS_USERS=('root' 'hdfs' 'yarn' 'mepred' 'hive' 'spark' 'brijeshdhaker')
  for ou in "${OS_USERS[@]}"
  do
    echo "Add kerberos principal $ou start ."
    kadmin.local -q "delete_principal -force $ou@SANDBOX-BIGDATA.NET"
    kadmin.local -q "addprinc -pw $ou $ou@SANDBOX-BIGDATA.NET"
    echo "Add kerberos principal for user $ou complete."
  done

  # Set service instances
  #for var in one two three; do echo "$var"; done
  PRINCIPALS=('nn' 'dn' 'sn' 'rm' 'nm' 'jhs' 'yts' 'gw' 'host' 'HTTP')
  SANDBOX_NODES=('namenode' 'datanode' 'datanode' 'resourcemanager' 'nodemanager' 'historyserver' 'timelineserver' 'gateway')
  for pr in "${PRINCIPALS[@]}"
  do
    echo "Add kerberos principal $pr start ."
    echo ""
    for node in "${SANDBOX_NODES[@]}"
    do
        kadmin.local -q "delete_principal -force $pr/$node@SANDBOX-BIGDATA.NET"
        kadmin.local -q "addprinc -pw $pr $pr/$node@SANDBOX-BIGDATA.NET"
        echo "$pr/$node@SANDBOX-BIGDATA.NET principles added."

        kadmin.local ktadd -k "/etc/security/keytabs/$pr.service.$node.keytab" "$pr/$node@SANDBOX-BIGDATA.NET"

    done
    echo "Add kerberos principal for $pr complete"
  done

  for pr in "${PRINCIPALS[@]}"
  do
    mergekeytabs=""
    for node in "${SANDBOX_NODES[@]}"
    do
        mergekeytabs+="rkt /etc/security/keytabs/$pr.service.$node.keytab\n"
    done
    mergekeytabs+="wkt /etc/security/keytabs/$pr.service.keytab\nquit"
    echo ""
    echo "$mergekeytabs"
    echo -e "$mergekeytabs" | ktutil
  done

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