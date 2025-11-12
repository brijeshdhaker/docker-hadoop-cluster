
### -X for Action
```bash 
$ curl -X GET 'http://localhost:8080/transactions'
```

### --json
```bash
$ curl -X 'POST' 'http://localhost:8080/transactions' \
--data-binary [arg]
-H 'Content-Type: application/json' \
-H 'Accept: application/json' \
-d 'false'
```
###
```bash 
$ curl --resolve "hello-world.example:80:192.168.65.128" -i http://hello-world.example
```

### -I returns only the HTTPS headers
```bash
curl --request GET 'https://api.nasa.gov/planetary/apod?api_key=<myapikey>&date=2020-01-01' -I
```

### -o stores the output in a file
```bash
curl --request GET 'https://api.nasa.gov/planetary/apod?api_key=$NASA_API_KEY&date=2020-01-01' --output curloutput
```

### -v is the verbose option
```bash
curl --request GET 'https://api.nasa.gov/planetary/apod?api_key=$NASA_API_KEY&date=2020-01-01' -v
```

### -T Uploading Files
```bash
curl -T uploadfile.txt ftp://example.com/upload/
```

### -U Handling Authentication
```bash
curl -u username:password https://example.com/api
```

### -k, --insecure
```bash
curl \
--cacert ./bd-k8s-module/istio/certs/ca-cert-chain.crt \
--cert ./bd-k8s-module/istio/certs/client.sandbox.net.crt \
--key ./bd-k8s-module/istio/certs/client.sandbox.net.key \
https://example.com/api
```

### Postman API Key
PMAK-68f4eb00b698cb0001908b04-73f1bbd2826a6917647feb1214b6c523a9