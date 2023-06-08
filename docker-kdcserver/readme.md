====================================================
==== Kerberos KDC and Kadmin ======================================================
===================================================================================
REALM: SANDBOX-BIGDATA.NET
KADMIN_PRINCIPAL_FULL: kadmin/admin@SANDBOX-BIGDATA.NET
KADMIN_PASSWORD: kadmin

===================================================================================
==== /etc/krb5.conf ===============================================================
===================================================================================
[libdefaults]
	default_realm = SANDBOX-BIGDATA.NET
[realms]
	SANDBOX-BIGDATA.NET = {
		kdc_ports = 88,750
		kadmind_port = 749
		kdc = kdcserver
		admin_server = kdcserver
	}

===================================================================================
==== /etc/krb5kdc/kdc.conf ========================================================
===================================================================================
[realms]
	SANDBOX-BIGDATA.NET = {
		acl_file = /etc/krb5kdc/kadm5.acl
		max_renewable_life = 7d 0h 0m 0s
		supported_enctypes = aes256-cts-hmac-sha1-96:normal
		default_principal_flags = +preauth
	}

===================================================================================
==== /etc/krb5kdc/kadm5.acl =======================================================
===================================================================================
kadmin/admin@SANDBOX-BIGDATA.NET *
noPermissions@SANDBOX-BIGDATA.NET X

===================================================================================
==== Creating realm ===============================================================
===================================================================================
This script should be run on the master KDC/admin server to initialize
a Kerberos realm.  It will ask you to type in a master key password.
This password will be used to generate a key that is stored in
/etc/krb5kdc/stash.  You should try to remember this password, but it
is much more important that it be a strong password than that it be
remembered.  However, if you lose the password and /etc/krb5kdc/stash,
you cannot decrypt your Kerberos database.
Loading random data
Initializing database '/var/lib/krb5kdc/principal' for realm 'SANDBOX-BIGDATA.NET',
master key name 'K/M@SANDBOX-BIGDATA.NET'
You will be prompted for the database Master Password.
It is important that you NOT FORGET this password.
Enter KDC database master key:
kdb5_util: Cannot open DB2 database '/var/lib/krb5kdc/principal': File exists while creating database '/var/lib/krb5kdc/principal'
Re-enter KDC database master key to verify:

===================================================================================
==== Creating default principals in the acl =======================================
===================================================================================
Adding kadmin/admin principal
Authenticating as principal root/admin@SANDBOX-BIGDATA.NET with password.
Principal "kadmin/admin@SANDBOX-BIGDATA.NET" deleted.
Make sure that you have removed this principal from all ACLs before reusing.

No policy specified for kadmin/admin@SANDBOX-BIGDATA.NET; defaulting to no policy
Authenticating as principal root/admin@SANDBOX-BIGDATA.NET with password.
Principal "kadmin/admin@SANDBOX-BIGDATA.NET" created.

Adding noPermissions principal
Authenticating as principal root/admin@SANDBOX-BIGDATA.NET with password.
Principal "noPermissions@SANDBOX-BIGDATA.NET" deleted.
Make sure that you have removed this principal from all ACLs before reusing.

No policy specified for noPermissions@SANDBOX-BIGDATA.NET; defaulting to no policy
Authenticating as principal root/admin@SANDBOX-BIGDATA.NET with password.
Principal "noPermissions@SANDBOX-BIGDATA.NET" created.

kadmind: starting...
===================================================================================
==== Kerberos KDC and Kadmin ======================================================
===================================================================================
REALM: SANDBOX-BIGDATA.NET
KADMIN_PRINCIPAL_FULL: kadmin/admin@SANDBOX-BIGDATA.NET
KADMIN_PASSWORD: kadmin

===================================================================================
==== /etc/krb5.conf ===============================================================
===================================================================================
[libdefaults]
	default_realm = SANDBOX-BIGDATA.NET
[realms]
	SANDBOX-BIGDATA.NET = {
		kdc_ports = 88,750
		kadmind_port = 749
		kdc = kdcserver
		admin_server = kdcserver
	}

===================================================================================
==== /etc/krb5kdc/kdc.conf ========================================================
===================================================================================
[realms]
	SANDBOX-BIGDATA.NET = {
		acl_file = /etc/krb5kdc/kadm5.acl
		max_renewable_life = 7d 0h 0m 0s
		supported_enctypes = aes256-cts-hmac-sha1-96:normal
		default_principal_flags = +preauth
	}

===================================================================================
==== /etc/krb5kdc/kadm5.acl =======================================================
===================================================================================
kadmin/admin@SANDBOX-BIGDATA.NET *
noPermissions@SANDBOX-BIGDATA.NET X

===================================================================================
==== Creating realm ===============================================================
===================================================================================
This script should be run on the master KDC/admin server to initialize
a Kerberos realm.  It will ask you to type in a master key password.
This password will be used to generate a key that is stored in
/etc/krb5kdc/stash.  You should try to remember this password, but it
is much more important that it be a strong password than that it be
remembered.  However, if you lose the password and /etc/krb5kdc/stash,
you cannot decrypt your Kerberos database.
Loading random data
Initializing database '/var/lib/krb5kdc/principal' for realm 'SANDBOX-BIGDATA.NET',
master key name 'K/M@SANDBOX-BIGDATA.NET'
You will be prompted for the database Master Password.
It is important that you NOT FORGET this password.
Enter KDC database master key:
kdb5_util: Cannot open DB2 database '/var/lib/krb5kdc/principal': File exists while creating database '/var/lib/krb5kdc/principal'
Re-enter KDC database master key to verify:

===================================================================================
==== Creating default principals in the acl =======================================
===================================================================================
Adding kadmin/admin principal
Authenticating as principal root/admin@SANDBOX-BIGDATA.NET with password.
Principal "kadmin/admin@SANDBOX-BIGDATA.NET" deleted.
Make sure that you have removed this principal from all ACLs before reusing.

No policy specified for kadmin/admin@SANDBOX-BIGDATA.NET; defaulting to no policy
Authenticating as principal root/admin@SANDBOX-BIGDATA.NET with password.
Principal "kadmin/admin@SANDBOX-BIGDATA.NET" created.

Adding noPermissions principal
Authenticating as principal root/admin@SANDBOX-BIGDATA.NET with password.
Principal "noPermissions@SANDBOX-BIGDATA.NET" deleted.
Make sure that you have removed this principal from all ACLs before reusing.

No policy specified for noPermissions@SANDBOX-BIGDATA.NET; defaulting to no policy
Authenticating as principal root/admin@SANDBOX-BIGDATA.NET with password.
Principal "noPermissions@SANDBOX-BIGDATA.NET" created.

kadmind: starting...
