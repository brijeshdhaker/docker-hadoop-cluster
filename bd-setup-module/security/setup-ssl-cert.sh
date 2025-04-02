#!/bin/bash
#
# @Auther : Brijesh K Dhaker
# @Usage: ./bd-setup-module/security/setup-ssl-cert.sh <base_path>
#
#
#

if [ $# -eq 0 ]
then

  echo '@Usage   : ./bd-setup-module/security/setup-ssl-cert.sh ./bd-setup-module/security <type> <hostname>'
  echo '@Example : ./bd-setup-module/security/setup-ssl-cert.sh ./bd-setup-module/security CA'
  echo '@Example : ./bd-setup-module/security/setup-ssl-cert.sh ./bd-setup-module/security Intermediate'
  echo '@Example : ./bd-setup-module/security/setup-ssl-cert.sh ./bd-setup-module/security Server kubernetes'
  echo '@Example : ./bd-setup-module/security/setup-ssl-cert.sh ./bd-setup-module/security Client kubernetes'
  exit 1

fi

#
if [ ! -d "$1" ]
then
  echo '$1 should be a directory.'
  exit 1
fi

CERT_TYPE=$2
if [ $CERT_TYPE == "" ]
then
    echo "Certificate Type can't be blank."
    exit 1
fi

export BASE_PATH=$1
export ROOT_CA_PATH=${BASE_PATH}/ca/root
export INTERMEDIATE_CA_PATH=${BASE_PATH}/ca/intermediate
export SERVER_CERT_PATH=${BASE_PATH}/server
export CLIENT_CERT_PATH=${BASE_PATH}/client

case "$CERT_TYPE" in
    #case 1
    "CA")
        echo "Generating Root CA Certificate"

        mkdir -p ${ROOT_CA_PATH}/{certs,crl,newcerts,private,public}
        chmod 700 ${ROOT_CA_PATH}/private
        touch ${ROOT_CA_PATH}/index.txt
        echo 1000 > ${ROOT_CA_PATH}/serial

        #
        # 1. Acting as a certificate authority (CA)
        # means dealing with cryptographic pairs of private keys and public certificates.
        #

        # 1. Create the root key
        # omit the -aes256 option to create a key without a password
        openssl genrsa -aes256 \
        -passout pass:sandbox \
        -out ${ROOT_CA_PATH}/private/root-ca.key 4096

        chmod 400 ${ROOT_CA_PATH}/private/root-ca.key

        # 2. Extract public key
        openssl rsa -pubout \
        -passin pass:sandbox \
        -in ${ROOT_CA_PATH}/private/root-ca.key \
        -out ${ROOT_CA_PATH}/public/root-ca-public.key

        # 3. Create the root certificate
        openssl req -new -x509 -days 7300 -sha256 -extensions v3_ca \
          -config ${ROOT_CA_PATH}/openssl.cnf \
          -passin pass:sandbox \
          -subj "/CN=Root CA/O=Sandbox/OU=Security/L=Pune/ST=MH/C=IN/emailAddress=security@sandbox.net" \
          -key ${ROOT_CA_PATH}/private/root-ca.key \
          -out ${ROOT_CA_PATH}/certs/root-ca.cert

        # 4. Check certificate details
        openssl x509 -noout -text -in ${ROOT_CA_PATH}/certs/root-ca.cert

      ;;
    #case 2
    "Intermediate")
        echo "Generating Intermediate CA Certificate"

        #
        # 2. Create the intermediate pair :
        # An intermediate certificate authority (CA) is an entity that can sign certificates
        # on behalf of the root CA. The root CA signs the intermediate certificate, forming a chain of trust.
        #

        # 1. Prepare the directory
        mkdir -p ${INTERMEDIATE_CA_PATH}/{certs,crl,csr,newcerts,private,public}
        chmod 700 ${INTERMEDIATE_CA_PATH}/private
        touch ${INTERMEDIATE_CA_PATH}/index.txt
        echo 1000 > ${INTERMEDIATE_CA_PATH}/serial

        # crlnumber is used to keep track of certificate revocation lists.
        echo 1000 > ${INTERMEDIATE_CA_PATH}/crlnumber

        # 2. Create the intermediate key
        openssl genrsa -aes256 -passout pass:sandbox \
          -out ${INTERMEDIATE_CA_PATH}/private/intermediate-ca.key 4096

        chmod 400 ${INTERMEDIATE_CA_PATH}/private/intermediate-ca.key

        # 3. Extract public key
        openssl rsa -pubout \
          -passin pass:sandbox \
          -in ${INTERMEDIATE_CA_PATH}/private/intermediate-ca.key \
          -out ${INTERMEDIATE_CA_PATH}/public/intermediate-ca-public.key

        # 4. Create the intermediate certificate Signing Request
        openssl req -new -sha256 \
          -config ${INTERMEDIATE_CA_PATH}/openssl.cnf \
          -subj "/CN=Intermediate CA/O=Sandbox/OU=Security/L=Pune/ST=MH/C=IN/emailAddress=security@sandbox.net" \
          -passin pass:sandbox \
          -key ${INTERMEDIATE_CA_PATH}/private/intermediate-ca.key \
          -out ${INTERMEDIATE_CA_PATH}/csr/intermediate-ca.csr

        # 5. Sign intermediate certificate with Root CA
        openssl ca -days 3650 -notext -md sha256 \
          -config ${ROOT_CA_PATH}/openssl.cnf \
          -extensions v3_intermediate_ca \
          -passin pass:sandbox \
          -in ${INTERMEDIATE_CA_PATH}/csr/intermediate-ca.csr \
          -out ${INTERMEDIATE_CA_PATH}/certs/intermediate-ca.crt


        chmod 444 ${INTERMEDIATE_CA_PATH}/certs/intermediate-ca.crt

        # 6. Check certificate details
        openssl x509 -noout -text \
          -in ${INTERMEDIATE_CA_PATH}/certs/intermediate-ca.crt

        # 7. Verify the intermediate certificate against the root certificate
        openssl verify -CAfile ${ROOT_CA_PATH}/certs/root-ca.cert \
          ${INTERMEDIATE_CA_PATH}/certs/intermediate-ca.crt

        # 8. Create the certificate chain file
        cat ${INTERMEDIATE_CA_PATH}/certs/intermediate-ca.crt \
          ${ROOT_CA_PATH}/certs/root-ca.cert > ${INTERMEDIATE_CA_PATH}/certs/ca-chain.crt


      ;;
    #case 3
    "Server")
        echo "Generating Server Certificate"

        #
        # 3. Sign server and client certificates
        #
        mkdir -p ${SERVER_CERT_PATH}/{certs,crl,csr,newcerts,private,public}
        chmod 700 ${SERVER_CERT_PATH}/private

        SERVER_NAME=$3
        if [ $SERVER_NAME == "" ]
        then
            SERVER_NAME=$(hostname -f)
        fi

        # 1. Create a key
        # Omit the -aes256 option to create a key without a password
        openssl genrsa \
          -out ${SERVER_CERT_PATH}/private/${SERVER_NAME}.key 2048

        chmod 400 ${SERVER_CERT_PATH}/private/${SERVER_NAME}.key


        # Extract Server public key
        openssl rsa -pubout \
          -in ${SERVER_CERT_PATH}/private/${SERVER_NAME}.key \
          -out ${SERVER_CERT_PATH}/public/${SERVER_NAME}-public.key

        # 2. Create a Server Certificate Signing Request
        openssl req -config ${INTERMEDIATE_CA_PATH}/openssl.cnf \
          -new -sha256 \
          -subj "/CN=${SERVER_NAME}/O=Sandbox/OU=Servers/L=Pune/ST=MH/C=IN/emailAddress=security@sandbox.net" \
          -addext "subjectAltName = DNS:localhost,DNS:${SERVER_NAME},DNS:${SERVER_NAME}.sandbox.net,DNS:*.sandbox.net,DNS:*.example.com,IP:127.0.0.1,IP:192.168.9.128, IP:192.168.30.128" \
          -key ${SERVER_CERT_PATH}/private/${SERVER_NAME}.key \
          -out ${SERVER_CERT_PATH}/csr/${SERVER_NAME}.csr

        # 3. Verify Certificate Signing Request
        openssl req -text -noout -verify -in ${SERVER_CERT_PATH}/csr/${SERVER_NAME}.csr

        # 4. Creating Server Extension Conf file
        if [ ! -f "${SERVER_CERT_PATH}/${SERVER_NAME}-extfile.cnf" ]
        then
          rm -f ${SERVER_CERT_PATH}/${SERVER_NAME}-extfile.cnf
        fi

        sed_expression=$(eval echo "s/#SERVER_NAME#/${SERVER_NAME}/g")
        sed -E ${sed_expression} ${SERVER_CERT_PATH}/server-extfile.cnf > ${SERVER_CERT_PATH}/${SERVER_NAME}-extfile.cnf

        # 5. Sign server certificate with Intermediate CA
        openssl x509 \
          -req -sha256 -days 365 \
          -passin pass:sandbox \
          -CAcreateserial \
          -CA ${INTERMEDIATE_CA_PATH}/certs/intermediate-ca.crt \
          -CAkey ${INTERMEDIATE_CA_PATH}/private/intermediate-ca.key \
          -in ${SERVER_CERT_PATH}/csr/${SERVER_NAME}.csr \
          -out ${SERVER_CERT_PATH}/certs/${SERVER_NAME}.cert \
          -extensions v3_req \
          -extfile ${SERVER_CERT_PATH}/${SERVER_NAME}-extfile.cnf

        #
        chmod 444 ${SERVER_CERT_PATH}/certs/${SERVER_NAME}.cert

        # 6. Verify the certificate
        openssl x509 -noout -text \
          -in ${SERVER_CERT_PATH}/certs/${SERVER_NAME}.cert

        # 7. Verify the certificate against Signing CA
        openssl verify -CAfile ${INTERMEDIATE_CA_PATH}/certs/ca-chain.crt \
          ${SERVER_CERT_PATH}/certs/${SERVER_NAME}.cert


      ;;
    #case 4
    "Client")
      echo "Generating Client Certificate"

      mkdir -p ${CLIENT_CERT_PATH}/{certs,crl,csr,newcerts,private,public}

      CLIENT_NAME=$3
      if [ $CLIENT_NAME == "" ]
      then
          CLIENT_NAME=$(hostname -f)
      fi

      # 1. Create a key
      openssl genrsa \
      -out ${CLIENT_CERT_PATH}/private/${CLIENT_NAME}-client.key 2048

      # 2. Extract Client public key
      openssl rsa \
        -pubout -in ${CLIENT_CERT_PATH}/private/${CLIENT_NAME}-client.key \
        -out ${CLIENT_CERT_PATH}/public/${CLIENT_NAME}-client-public.key

      # 3. Create a certificate Signing Request
      openssl req \
        -subj '/CN=Sandbox Client' -new \
        -key ${CLIENT_CERT_PATH}/private/${CLIENT_NAME}-client.key \
        -out ${CLIENT_CERT_PATH}/csr/${CLIENT_NAME}-client.csr

      # echo extendedKeyUsage = clientAuth > ${CLIENT_CERT_PATH}/client-extfile.cnf

      # Generate the signed client certificate
      openssl x509 -req -days 365 -sha256 \
        -passin pass:sandbox \
        -CAcreateserial \
        -CA ${INTERMEDIATE_CA_PATH}/certs/intermediate.cert \
        -CAkey ${INTERMEDIATE_CA_PATH}/private/intermediate.key \
        -in ${CLIENT_CERT_PATH}/csr/${CLIENT_NAME}-client.csr \
        -out ${CLIENT_CERT_PATH}/certs/${CLIENT_NAME}-client.cert \
        -extfile ${CLIENT_CERT_PATH}/client-extfile.cnf

      # Verify the certificate
      openssl x509 -noout -text \
        -in ${CLIENT_CERT_PATH}/certs/${CLIENT_NAME}-client.cert

      # Verify the certificate against Signing CA
      openssl verify \
        -CAfile ${INTERMEDIATE_CA_PATH}/certs/ca-chain.crt \
        ${CLIENT_CERT_PATH}/certs/${CLIENT_NAME}-client.cert

      ;;
esac



