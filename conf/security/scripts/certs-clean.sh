#!/bin/bash

# ./conf/security/scripts/certs-clean.sh

set -o nounset \
    -o errexit \
    -o verbose
#    -o xtrace

# Cleanup files
rm -f conf/security/ssl/*.crt
rm -f conf/security/ssl/*.csr
rm -f conf/security/ssl/*-creds
rm -f conf/security/ssl/*.jks
rm -f conf/security/ssl/*.srl
rm -f conf/security/ssl/*.key
rm -f conf/security/ssl/*.pem
rm -f conf/security/ssl/*.der
rm -f conf/security/ssl/*.p12

# Cleanup ext files
rm -f conf/security/ssl/*.extfile
