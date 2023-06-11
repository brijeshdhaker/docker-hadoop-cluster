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

[logging]
  default = FILE:/var/log/krb5libs.log
  kdc = FILE:/var/log/krb5kdc.log
  admin_server = FILE:/var/log/kadmind.log

[libdefaults]
  default_realm = $REALM
  dns_lookup_realm = false
  dns_lookup_kdc = false
  ticket_lifetime = 24h
  renew_lifetime = 7d
  forwardable = true
# udp_preference_limit = 1  # when TCP only should be used.

# uncomment the following if AD cross realm auth is ONLY providing DES encrypted tickets
# allow-weak-crypto = true

[realms]
	$REALM = {
		kdc_ports = 88,750
		kadmind_port = 749
		kdc = $KDC_KADMIN_SERVER
		admin_server = $KDC_KADMIN_SERVER
	}
[domain_realm]
  .sandbox.net = $REALM
  sandbox.net = $REALM

EOF
echo ""

echo "==================================================================================="
echo "==== /etc/krb5kdc/kdc.conf ========================================================"
echo "==================================================================================="
tee /etc/krb5kdc/kdc.conf <<EOF
[logging]
    kdc = FILE:/var/log/krb5kdc.log
    admin_server = FILE:/var/log/kadmin.log

[kdcdefaults]
    kdc_ports = 88
    kdc_tcp_ports = 88

[realms]
	$REALM = {
		acl_file = /etc/krb5kdc/kadm5.acl
		dict_file = /usr/share/dict/words
		kadmind_port = 749
		max_life = 12h 0m 0s
		max_renewable_life = 7d 0h 0m 0s
		# master_key_type = aes256-cts
		# supported_enctypes =  camellia256-cts:normal camellia128-cts:normal
		supported_enctypes = aes256-cts:normal aes128-cts:normal arcfour-hmac:normal
		default_principal_flags = +renewable, +forwardable
	}
EOF
echo ""

echo "==================================================================================="
echo "==== /etc/krb5kdc/kadm5.acl ======================================================="
echo "==================================================================================="
tee /etc/krb5kdc/kadm5.acl <<EOF
$KADMIN_PRINCIPAL_FULL *
*/admin@$REALM *
noPermissions@$REALM X
EOF
echo ""

echo "==================================================================================="
echo "==== Creating realm ==============================================================="
echo "==================================================================================="
MASTER_PASSWORD=$(tr -cd '[:alnum:]' < /dev/urandom | fold -w30 | head -n1)
echo "Database Master Password : $MASTER_PASSWORD"
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

  # delete existing keytab files
  rm -Rf /etc/kerberos/keytabs/*.keytab

  echo "Adding OS User Principal"
  echo ""
  OS_USERS=("root" "brijeshdhaker" "hdfs" "yarn" "mapred" "hive" "spark")
  for ou in "${OS_USERS[@]}"
  do
  # kadmin.local -w kadmin -q "delete_principal -force brijeshdhaker@SANDBOX.NET"
    kadmin.local -w $KADMIN_PASSWORD -q "delete_principal -force $ou@$REALM"

  # kadmin.local -w kadmin -q "addprinc -pw brijeshdhaker brijeshdhaker@SANDBOX.NET"
    kadmin.local -w $KADMIN_PASSWORD -q "addprinc -pw $KUSERS_PASSWORD $ou@$REALM"

  # kadmin.local ktadd -k "/etc/kerberos/keytabs/brijeshdhaker.keytab" brijeshdhaker@SANDBOX.NET
    kadmin.local ktadd -k "/etc/kerberos/keytabs/$ou.keytab" "$ou@$REALM"
  done

  echo "Adding service principal for all hosts."
  echo ""
  #for var in one two three; do echo "$var"; done
  PRINCIPALS=("hdfs" "yarn" "mapred" "hive" "metastore" "host" "HTTP")
  SANDBOX_NODES=(
    "namenode.sandbox.net"
    "datanode.sandbox.net"
    "secondary.sandbox.net"
    "resourcemanager.sandbox.net"
    "nodemanager.sandbox.net"
    "historyserver.sandbox.net"
    "timelineserver.sandbox.net"
    "metastore.sandbox.net"
    "hiveserver.sandbox.net"
    "gateway.sandbox.net"
    "thinkpad.sandbox.net"
    "hostmaster.sandbox.net"
    "dockerhost.sandbox.net"
  )
  for pr in "${PRINCIPALS[@]}"
  do
    echo ""
    for node in "${SANDBOX_NODES[@]}"
    do
        kadmin.local -q "delete_principal -force $pr/$node@$REALM"
        kadmin.local -q "addprinc -pw $KUSERS_PASSWORD $pr/$node@$REALM"
        kadmin.local ktadd -k "/etc/kerberos/keytabs/$pr.service.$node.keytab" "$pr/$node@$REALM"
    done
  done

  # Create unmerged keytab
  for pr in "${PRINCIPALS[@]}"
  do
    ktunmerged=""
    for node in "${SANDBOX_NODES[@]}"
    do
        ktunmerged+="rkt /etc/kerberos/keytabs/$pr.service.$node.keytab\n"
    done
    ktunmerged+="wkt /etc/kerberos/keytabs/$pr-unmerged.keytab\n"
    ktunmerged+="quit"
    echo ""
    echo -e "$ktunmerged" | ktutil
  done

  # Create merged keytab
  UNMPRINC=("hdfs" "yarn" "mapred" "hive" "metastore")
  for upr in "${UNMPRINC[@]}"
  do
    ktmerge=""
    ktmerge+="rkt /etc/kerberos/keytabs/$upr-unmerged.keytab\n"
    ktmerge+="rkt /etc/kerberos/keytabs/host-unmerged.keytab\n"
    ktmerge+="wkt /etc/kerberos/keytabs/$upr.service.keytab\n"
    ktmerge+="quit"
    echo ""
    echo -e "$ktmerge" | ktutil
  done

  # Setup spnego.service.keytab
  cp /etc/kerberos/keytabs/host-unmerged.keytab /etc/kerberos/keytabs/host.service.keytab
  cp /etc/kerberos/keytabs/HTTP-unmerged.keytab /etc/kerberos/keytabs/HTTP.service.keytab
  cp /etc/kerberos/keytabs/HTTP-unmerged.keytab /etc/kerberos/keytabs/spnego.service.keytab
  chmod -Rf 777 /etc/kerberos/keytabs/*

  #
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