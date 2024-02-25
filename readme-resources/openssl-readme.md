#
# Generate Cert using SSL
#

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
