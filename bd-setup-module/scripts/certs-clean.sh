#!/bin/bash

set -o nounset \
    -o errexit \
    -o verbose
#    -o xtrace

# Cleanup files
rm -f conf/secrets/ssl/*.crt
rm -f conf/secrets/ssl/*.csr
rm -f conf/secrets/ssl/*_creds
rm -f conf/secrets/ssl/*.jks
rm -f conf/secrets/ssl/*.srl
rm -f conf/secrets/ssl/*.key
rm -f conf/secrets/ssl/*.pem
rm -f conf/secrets/ssl/*.der
rm -f conf/secrets/ssl/*.p12

# Cleanup ext files
rm -f conf/secrets/ssl/*.extfile
