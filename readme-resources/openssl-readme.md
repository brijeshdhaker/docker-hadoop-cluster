
#
# Generate Cert using SSL
#

# Setup Directory
```shell
    export BASE_PATH=/apps/security/ssl/
    export ROOT_CA_PATH=$BASE_PATH/root/ca
    mkdir -p ${ROOT_CA_PATH}
    # cd -p ${ROOT_CA_PATH}
    mkdir -p ${ROOT_CA_PATH}/{certs,crl,newcerts,private,public}
    chmod 700 ${ROOT_CA_PATH}/private
    touch ${ROOT_CA_PATH}/index.txt
    echo 1000 > ${ROOT_CA_PATH}/serial
    
    # 1. Create the root key
    openssl genrsa -aes256 -passout pass:sandbox -out ${ROOT_CA_PATH}/private/sandbox-root-ca.key.pem 4096
    chmod 400 ${ROOT_CA_PATH}/private/ca.key.pem
    
    # 3. Extract public key
    openssl rsa -pubout -passin pass:sandbox -in ${ROOT_CA_PATH}/private/sandbox-root-ca.key.pem -out ${ROOT_CA_PATH}/public/sandbox-root-ca-public.key.pem
    
    # 2. Create the root certificate
    openssl req -new -x509 -days 7300 -sha256 -extensions v3_ca \
      -config ${ROOT_CA_PATH}/openssl.cnf \
      -passin pass:sandbox \
      -key ${ROOT_CA_PATH}/private/sandbox-root-ca.key.pem \
      -out ${ROOT_CA_PATH}/certs/sandbox-root-ca.cert.pem
    
    #
    openssl x509 -noout -text -in ${ROOT_CA_PATH}/certs/sandbox-root-ca.cert.pem
    
    #
    # Create the intermediate pair : 
    # An intermediate certificate authority (CA) is an entity that can sign certificates 
    # on behalf of the root CA. The root CA signs the intermediate certificate, forming a chain of trust.
    #
    
    # 1. Prepare the directory
    export INTERMEDIATE_CA_PATH=$BASE_PATH/intermediate/ca
    mkdir -p ${INTERMEDIATE_CA_PATH}
    mkdir -p ${INTERMEDIATE_CA_PATH}/{certs,crl,csr,newcerts,private,public}
    chmod 700 ${INTERMEDIATE_CA_PATH}/private
    touch ${INTERMEDIATE_CA_PATH}/index.txt
    echo 1000 > ${INTERMEDIATE_CA_PATH}/serial
    
    # crlnumber is used to keep track of certificate revocation lists.
    echo 1000 > ${INTERMEDIATE_CA_PATH}/crlnumber
    
    # 2. Create the intermediate key
    openssl genrsa -aes256 -passout pass:sandbox -out ${INTERMEDIATE_CA_PATH}/private/sandbox-intermediate.key.pem 4096
    chmod 400 ${INTERMEDIATE_CA_PATH}/private/sandbox-intermediate.key.pem
    
    # 3. Extract public key
    openssl rsa -pubout -passin pass:sandbox -in ${INTERMEDIATE_CA_PATH}/private/sandbox-intermediate.key.pem -out ${INTERMEDIATE_CA_PATH}/public/sandbox-intermediate-public.key.pem
    
    # 4. Create the intermediate certificate Signing Request
    openssl req -config ${INTERMEDIATE_CA_PATH}/openssl.cnf -new -sha256 \
      -passin pass:sandbox \
      -key ${INTERMEDIATE_CA_PATH}/private/sandbox-intermediate.key.pem \
      -out ${INTERMEDIATE_CA_PATH}/csr/sandbox-intermediate.csr.pem
    
    # 5. Sign intermediate certificate with Root CA
    openssl ca -config ${ROOT_CA_PATH}/openssl.cnf -extensions v3_intermediate_ca \
      -days 3650 -notext -md sha256 \
      -passin pass:sandbox \
      -in ${INTERMEDIATE_CA_PATH}/csr/sandbox-intermediate.csr.pem \
      -out ${INTERMEDIATE_CA_PATH}/certs/sandbox-intermediate.cert.pem
      
      
    chmod 444 ${INTERMEDIATE_CA_PATH}/certs/sandbox-intermediate.cert.pem
   
    # 6. Check certificate details
    openssl x509 -noout -text \
      -in ${INTERMEDIATE_CA_PATH}/certs/sandbox-intermediate.cert.pem
    
    # 7. Verify the intermediate certificate against the root certificate
    openssl verify -CAfile ${ROOT_CA_PATH}/certs/sandbox-root-ca.cert.pem \
      ${INTERMEDIATE_CA_PATH}/certs/sandbox-intermediate.cert.pem
    
    # 8. Create the certificate chain file
    cat ${INTERMEDIATE_CA_PATH}/certs/sandbox-intermediate.cert.pem \
      ${ROOT_CA_PATH}/certs/sandbox-root-ca.cert.pem > ${INTERMEDIATE_CA_PATH}/certs/sandbox-ca-chain.cert.pem
      
      
    #
    # Sign server and client certificates
    #
    export BASE_PATH=./bd-setup-module/security
    export SERVER_CERT_PATH=$BASE_PATH/server
    mkdir -p ${SERVER_CERT_PATH}
    mkdir -p ${SERVER_CERT_PATH}/{certs,crl,newcerts,private,public}
    chmod 700 ${SERVER_CERT_PATH}/private
    
    # 1. Create a key
    openssl genrsa -aes256 \
      -out ${SERVER_CERT_PATH}/private/sandbox-server.key.pem 2048
    
    chmod 400 ${SERVER_CERT_PATH}/private/sandbox-server.key.pem
    
    # extract public key
    openssl rsa -pubout -passin pass:sandbox -in ${SERVER_CERT_PATH}/private/sandbox-server.key.pem -out ${SERVER_CERT_PATH}/public/sandbox-server-public.key.pem
        
    # 2. Create a certificate Signing Request
    openssl req -config ${INTERMEDIATE_CA_PATH}/openssl.cnf \
      -passin pass:sandbox \
      -key ${SERVER_CERT_PATH}/private/sandbox-server.key.pem \
      -new -sha256 -out ${SERVER_CERT_PATH}/csr/sandbox-server.csr.pem
    
    # 3. Sign server certificate with Intermediate Root CA
    openssl ca -config ${INTERMEDIATE_CA_PATH}/openssl.cnf \
      -extensions server_cert -days 375 -notext -md sha256 \
      -passin pass:sandbox \
      -in ${SERVER_CERT_PATH}/csr/sandbox-server.csr.pem \
      -out ${SERVER_CERT_PATH}/certs/sandbox-server.cert.pem
    
    chmod 444 ${SERVER_CERT_PATH}/certs/sandbox-server.cert.pem
          
    # Verify the certificate
    openssl x509 -noout -text \
      -in ${SERVER_CERT_PATH}/certs/sandbox-server.cert.pem
    
    #
    openssl verify -CAfile ${INTERMEDIATE_CA_PATH}/certs/sandbox-ca-chain.cert.pem \
      ${SERVER_CERT_PATH}/certs/sandbox-server.cert.pem
    
    #
    # Deployment on Server
    #
    
           
```


# Generate the certification authority (CA) key and certificate (Root CA)
```shell

openssl req -new \
-x509 \
-days 7300 \
-keyout /apps/security/ssl/sandbox-root-ca.key \
-out /apps/security/ssl/sandbox-root-ca.crt \
-subj '/CN=Sandbox-Root-CA/OU=Sandbox/O=Sandbox/L=Pune/ST=MH/C=IN' \
-passin pass:sandbox \
-passout pass:sandbox

```

# creating a new key and certificate that is valid for 365 days, uses the rsa:2048 encryption,
openssl req -new -nodes \
-days 365 \
-newkey rsa:2048 \
-keyout /apps/security/ssl/server.key \
-out /apps/security/ssl/server.csr \
-config /apps/security/ssl/server.cnf


#
# Sign the host certificate with the certificate authority (CA)
#
openssl x509 -req \
-days 3650 \
-in /apps/security/ssl/server.csr \
-CA /apps/security/ssl/sandbox-root-ca.crt \
-CAkey /apps/security/ssl/sandbox-root-ca.key \
-CAcreateserial \
-out /apps/security/ssl/server.crt \
-extfile /apps/security/ssl/server.cnf \
-extensions v3_req


#
# Verify HTTPS 
#
```shell

openssl s_client -connect localhost:19093 -tls1_3 -showcerts

# Kafka Broker
openssl s_client -connect kafkabroker.sandbox.net:19093 -tls1_3 -showcerts

# Zookeeper
openssl s_client -connect zookeeper.sandbox.net:28080 -tls1_3 -showcerts \ 
      -CAfile /etc/zookeeper/secrets/sandbox_ca.crt \
      -cert /etc/zookeeper/secrets/clients.certificate.pem \ 
      -key /etc/zookeeper/secrets/clients.key

# Schema Registry
openssl s_client -connect schemaregistry.sandbox.net:8081 -tls1_3 -showcerts \
      -cert conf/kafka/secrets/clients.certificate.pem \
      -key conf/kafka/secrets/clients.key

```

<<comment
 # Schema Registry Test :
comment

:'
echo"This doesnt echo"
echo"Even this doesnt"
'
