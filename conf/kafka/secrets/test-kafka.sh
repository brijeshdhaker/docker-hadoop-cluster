
#
openssl s_client -connect localhost:19093 -tls1_3 -showcerts

openssl s_client -connect kafkabroker.sandbox.net:9093 -tls1_3 -showcerts