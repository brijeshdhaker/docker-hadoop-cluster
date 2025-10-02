# Run with sudo password
echo Accoo7@k47 | sudo -S -k microk8s start
echo Accoo7@k47 | sudo -S -v
sudo whoami

sudo apt purge nodejs

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
netstat -na | grep 9080

# Passphrase-less SSH
$ ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa_hadoop
$ cat ~/.ssh/id_rsa_hadoop.pub >> ~/.ssh/authorized_keys
$ chmod 0600 ~/.ssh/authorized_keys

###
ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsacat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keyschmod 0600 ~/.ssh/authorized_keysssh localhost

yarn logs -applicationId your_application_id > your_application_id.log 2>&1

#
## Find Hidden Files Using Find Command
#
find /tmp/* -mtime +7 -type f -size +1G
find /tmp/* -mtime +7 -type f -exec rm {} \;

```shell
find . -name ".*" -type f 
for i in $(find . -name ".*" -type f)
do
  #echo "output: $i"
  rm -Rf $i
done

```

update-alternatives --config java

cat <<EOF > test.txt
Hello
EOF

cat > test.txt <<EOF
Hello
EOF

python -m http.server 8888

pkgs=""
while IFS= read -r pkg; do
# Append the package name to pkgs
pkgs="$pkgs $pkg"
done < "./bd-setup-module/security/hosts.txt"
echo $pkgs

for pkg in $(cat ./bd-setup-module/security/hosts.txt); do
echo $pkg
done

#
# SMBA Client
```shell
smbclient -L //thinkpad.sandbox.net --user='brijeshdhaker@gmail.com' --password='Windows2024$' --workgroup='THINKPAD-WIN-11'
```

#
#
```shell
today=$(date +'%s')
now=`date +"%Y-%m-%dT%H:%M:%S"`
YYYYMMDD=`date +"%Y%m%d"`
```
#
# Split Strings
#
```shell
A="$(echo 'one_two_three_four_five' | cut -d'_' -f2)"
B="$(echo 'one_two_three_four_five' | cut -d'_' -f4)"

C=$(awk -F_ '{print $2}' <<< 'one_two_three_four_five')
D=$(awk -F_ '{print $4}' <<< 'one_two_three_four_five')

```

#
# break string with token
#
```shell
s_path="/ib/core/data/csv:dfilter_44
s_dir_path=${s_path%%:*}
s_filter=${s_path#*:}
```

#
#
#
```shell
nc -v -n -z -w 1 192.168.65.1 22
netstat -na | grep 7180

#

last | grep -i reboot
ps -ef | grep -i java

#
ls -l | awk '$1 !~ /^d/ {print $9}'
ls -l | awk '$1 ~ /^d/ {print $9}'
awk '/pattern/{ print $0 }' file
awk '{for(i=1;i<=NF;i++){ if($i=="yyy"){print $i} } }' file
```