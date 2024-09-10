
<<comment
# Schema Registry Test :
comment

:'
echo"This doesnt echo"
echo"Even this doesnt"
'

> file redirects stdout to file
1> file redirects stdout to file
2> file redirects stderr to file
&> file redirects stdout and stderr to file
> file 2>&1 redirects stdout and stderr to file
/dev/null is the null device it takes any input you want and throws it away. It can be used to suppress any output.
Note that > file 2>&1 is an older syntax which still works, &> file is neater, but would not have worked on older systems.

0 means stdin
1 means stdout(useful output)
2 means stderr(error message output)



declare -A services=(
["apache2"]="/etc/init.d/apache2 status"
["mysql"]="/etc/init.d/mysql status"
["sshd"]="/etc/init.d/ssh status"
)


IFS=";" read -r -a arr <<< 'domain.de;de;https'
site="${arr[0]}"
lang="${arr[1]}"
prot="${arr[2]}"
echo "site : ${site}"
echo "lang : ${lang}"
echo "prot : ${prot}"
echo

## TCP Port Scan
nc -z -v kdcserver.sandbox.net 749

## UDP Port Scan
netcat -u kdcserver.sandbox.net 88

# Passphrase-less SSH
$ ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa_hadoop
$ cat ~/.ssh/id_rsa_hadoop.pub >> ~/.ssh/authorized_keys
$ chmod 0600 ~/.ssh/authorized_keys

###
ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsacat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keyschmod 0600 ~/.ssh/authorized_keysssh localhost

yarn logs -applicationId your_application_id > your_application_id.log 2>&1

## Find Hidden Files Using Find Command
```shell
find . -name ".*" -type f 
for i in $(find . -name ".*" -type f)
do
  #echo "output: $i"
  rm -Rf $i
done

```
