#
## Minio S3 Use Cases 
![s3-compatibility.svg](images%2Fs3-compatibility.svg)

#
```shell
/usr/bin/mc config host add minio http://minio.sandbox.net:9010 admin password

echo "Creating Credentials for 'minio/warehouse' bucket "
export AWS_S3_ENDPOINT=http://minio.sandbox.net:9010
export AWS_ACCESS_KEY=ffaJ6a2MOj8mZ5lI3P6h
export AWS_ACCESS_SECRET=9u8TCmTtg9VyCVzgfDl6LvgcDd84DaM4h43bg1Bs
export AWS_REGION=us-east-1

 
/usr/bin/mc rm -r --force minio/warehouse;

/usr/bin/mc mb minio/warehouse;

/usr/bin/mc policy set public minio/warehouse;

/usr/bin/mc alias set warehouse "${AWS_S3_ENDPOINT}" "${AWS_ACCESS_KEY}" "${AWS_ACCESS_SECRET}" --api S3v4

/usr/bin/mc admin info warehouse

/usr/bin/mc cp users.parquet s3/warehouse/users-data/

/usr/bin/mc du warehouse

```


To configure AWS CLI, type `aws configure` and specify the MinIO key information.
Access credentials shown in this example belong to http://minio.sandbox.net:9010.
These credentials are open to public. Feel free to use this service for testing and development. Replace with your own MinIO keys in deployment.

## aws configure
```shell
sudo yum install awscli
aws configure
AWS Access Key ID [None]: ffaJ6a2MOj8mZ5lI3P6h
AWS Secret Access Key [None]: 9u8TCmTtg9VyCVzgfDl6LvgcDd84DaM4h43bg1Bs
Default region name [None]: us-east-1
Default output format [None]: ENTER
```

## Additionally enable AWS Signature Version '4' for MinIO server.
```shell
aws configure set default.s3.signature_version s3v4
```

## To list your buckets
```shell
aws --endpoint-url http://minio.sandbox.net:9010 s3 ls
2016-03-27 02:06:30 deebucket
2016-03-28 21:53:49 guestbucket
2016-03-29 13:34:34 mbtest
2016-03-26 22:01:36 mybucket
2016-03-26 15:37:02 testbucket
```

## To list contents inside bucket
```shell
aws --endpoint-url http://minio.sandbox.net:9010 s3 ls s3://warehouse
2024-03-02 21:59:29     396896 airlines.csv
2024-03-02 21:59:52    1127225 airports.csv
```

## To make a bucket
```shell
aws --endpoint-url http://minio.sandbox.net:9010 s3 mb s3://warehouse
make_bucket: s3://warehouse/
```

## To add an object to a bucket
```shell
aws --endpoint-url http://minio.sandbox.net:9010 s3 cp /apps/hostpath/datasets/airlines.csv s3://warehouse
upload: ../../../../apps/hostpath/datasets/airlines.csv to s3://warehouse/airlines.csv

aws --endpoint-url http://minio.sandbox.net:9010 s3 cp /apps/hostpath/datasets/airports.csv s3://warehouse
upload: ../../../../apps/hostpath/datasets/airports.csv to s3://warehouse/airports.csv

```

## To delete an object from a bucket
```shell
aws --endpoint-url http://minio.sandbox.net:9010 s3 rm s3://warehouse/airports.csv
delete: s3://warehouse/airports.csv
```

## To remove a bucket
```shell
aws --endpoint-url http://minio.sandbox.net:9010 s3 rb s3://warehouse
remove_bucket: s3://warehouse/
```
