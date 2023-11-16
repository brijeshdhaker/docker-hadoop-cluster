E.g. to consume all messages from mytopic partition 2 and then exit:

## Aruments Details 
* -t  for topic name
* -p  for partition
* -o  for offset
* 


$ kafkacat -b mybroker -t mytopic -p 2 -o beginning -e


Or consume the last 3000 messages and then exit:
$ kafkacat -b mybroker -t mytopic -p 2 -o -3000 -e