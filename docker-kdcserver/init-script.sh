#!/bin/bash

echo "==================================================================================="
echo "==== Kerberos KDC and Kadmin ======================================================"
echo "==================================================================================="
ROOT_ADMIN_PRINCIPAL=$ROOT_PRINCIPAL@$REALM
KADMIN_PRINCIPAL_FULL=$KADMIN_PRINCIPAL@$REALM

echo ""
echo "REALM: $REALM"
echo "ROOT_ADMIN_PRINCIPAL: $ROOT_ADMIN_PRINCIPAL"
echo "KADMIN_PRINCIPAL_FULL: $KADMIN_PRINCIPAL_FULL"
echo "KADMIN_PASSWORD: $KADMIN_PASSWORD"
echo "KUSERS_PASSWORD: $KUSERS_PASSWORD"
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
		# master_key_type = $MASTER_KEY_TYPE
		supported_enctypes = aes256-cts:normal aes128-cts:normal des3-hmac-sha1:normal arcfour-hmac:normal camellia256-cts:normal camellia128-cts:normal
		default_principal_flags = +renewable, +forwardable
	}
EOF
echo ""

echo "==================================================================================="
echo "==== /etc/krb5kdc/kadm5.acl ======================================================="
echo "==================================================================================="
tee /etc/krb5kdc/kadm5.acl <<EOF
$ROOT_ADMIN_PRINCIPAL  *
$KADMIN_PRINCIPAL_FULL *
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

  echo "Adding $ROOT_ADMIN_PRINCIPAL principal"
  kadmin.local -q "delete_principal -force $ROOT_ADMIN_PRINCIPAL"
  kadmin.local -q "addprinc -pw $KADMIN_PASSWORD $ROOT_ADMIN_PRINCIPAL"
  kadmin.local -q "xst -norandkey -k /etc/kerberos/admin/root.keytab $ROOT_ADMIN_PRINCIPAL"
  echo ""

  echo "Adding $KADMIN_PRINCIPAL principal"
  kadmin.local -q "delete_principal -force $KADMIN_PRINCIPAL_FULL"
  kadmin.local -q "addprinc -pw $KADMIN_PASSWORD $KADMIN_PRINCIPAL_FULL"
  kadmin.local -q "xst -norandkey -k /etc/kerberos/admin/kadmin.keytab $KADMIN_PRINCIPAL_FULL"
  echo ""

  echo "Adding noPermissions principal"
  kadmin.local -q "delete_principal -force noPermissions@$REALM"
  kadmin.local -q "addprinc -pw $KADMIN_PASSWORD noPermissions@$REALM"
  kadmin.local -q "xst -norandkey -k /etc/kerberos/admin/noPermissions.keytab noPermissions@$REALM"
  echo ""

  echo "Admin principles successfully generated."
  echo ""
else
  echo ""
  echo "Admin principles already generated."
  echo ""
fi

krb5kdc
kadmind -nofork