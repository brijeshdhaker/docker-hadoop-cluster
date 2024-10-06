
$ curl -X GET 'http://localhost:8080/transactions'

$ curl -X 'POST' 'http://localhost:8080/transactions' \
-H 'Content-Type: application/json' \
-d 'false'