To configure AWS CLI, type aws configure and specify the MinIO key information.

Access credentials shown in this example belong to https://minio:9000.
These credentials are open to public. Feel free to use this service for testing and development. Replace with your own MinIO keys in deployment.

#
# aws configure
#
AWS Access Key ID [None]: Q3AM3UQ867SPQQA43P2F
AWS Secret Access Key [None]: zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG
Default region name [None]: us-east-1
Default output format [None]: ENTER

#
# Additionally enable AWS Signature Version '4' for MinIO server.
#
aws configure set default.s3.signature_version s3v4

 Commands

#
# To list your buckets
#
aws --endpoint-url https://minio:9000 s3 ls
2016-03-27 02:06:30 deebucket
2016-03-28 21:53:49 guestbucket
2016-03-29 13:34:34 mbtest
2016-03-26 22:01:36 mybucket
2016-03-26 15:37:02 testbucket

#
# To list contents inside bucket
#
aws --endpoint-url https://minio:9000 s3 ls s3://word-count
2016-03-30 00:26:53      69297 argparse-1.2.1.tar.gz
2016-03-30 00:35:37      67250 simplejson-3.3.0.tar.gz

#
# To make a bucket
#
aws --endpoint-url https://minio:9000 s3 mb s3://sparkapp-bucket
make_bucket: s3://sparkapp-bucket/

#
# To add an object to a bucket
#
aws --endpoint-url https://minio:9000 s3 cp simplejson-3.3.0.tar.gz s3://mybucket
upload: ./simplejson-3.3.0.tar.gz to s3://mybucket/simplejson-3.3.0.tar.gz

#
# To delete an object from a bucket
#
aws --endpoint-url https://minio:9000 s3 rm s3://mybucket/argparse-1.2.1.tar.gz
delete: s3://mybucket/argparse-1.2.1.tar.gz

#
#To remove a bucket
#
 aws --endpoint-url https://minio:9000 s3 rb s3://mybucket
remove_bucket: s3://mybucket/