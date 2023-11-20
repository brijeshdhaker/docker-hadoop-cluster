#!/bin/bash

set -o nounset \
    -o errexit \
    -o verbose
#    -o xtrace

# Cleanup files
rm -f conf/kafka/secrets/*.crt
rm -f conf/kafka/secrets/*.csr
rm -f conf/kafka/secrets/*_creds
rm -f conf/kafka/secrets/*.jks
rm -f conf/kafka/secrets/*.srl
rm -f conf/kafka/secrets/*.key
rm -f conf/kafka/secrets/*.pem
rm -f conf/kafka/secrets/*.der
rm -f conf/kafka/secrets/*.p12

# Cleanup ext files
rm -f conf/kafka/secrets/*.extfile
