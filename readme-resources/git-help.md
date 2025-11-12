###
git config --global user.name "Brijesh K Dhaker"
git config --global user.email brijeshdhaker@gmail.com

### Setup Git Repository
git init
git add README.md
git commit -m "first commit"
git branch -M develop
git remote add origin git@github.com:brijeshdhaker/docker-hadoop-cluster.git
git push -u origin develop
                

### Push an existing repository from the command line

git remote add origin git@github.com:brijeshdhaker/docker-hadoop-cluster.git
git branch -M develop
git push -u origin develop

git remote add origin git@github.com:brijeshdhaker/docker-hadoop-cluster.git
git branch -M master
git push -u origin master

### Regular Commit
git add README.md
git commit -m "first commit"
git push -u origin develop

### deleted a file but didn’t commit
git checkout HEAD <filename>

### deleted a file and committed the deletion
git reset --hard HEAD~1

#
# Setup GPG2 Keys
#
```bash
#
sudo apt-get update && apt-get install gnupg2 -y

#
gpg --full-generate-key
gpg --default-new-key-algo rsa4096 --gen-key

#
gpg --list-secret-keys --keyid-format=long
gpg: checking the trustdb
gpg: marginals needed: 3  completes needed: 1  trust model: pgp
gpg: depth: 0  valid:   1  signed:   0  trust: 0-, 0q, 0n, 0m, 0f, 1u
/home/brijeshdhaker/.gnupg/pubring.kbx
--------------------------------------
sec   rsa2048/DA7F14F53DB10896 2025-11-07 [SC]
      F9EDD679E0778F8E99A8BA58DA7F14F53DB10896
uid                 [ultimate] Brijesh K. Dhaker (gpg key for git hub) <brijeshdhaker@gmail.com>
ssb   rsa2048/BC1BF7C91AFFCC0A 2025-11-07 [E]

#
gpg --armor --export DA7F14F53DB10896
# Prints the GPG key ID, in ASCII armor format


# Add the GPG key to your GitHub account. https://github.com/settings/keys

# Unset Other Setup
git config --global --unset gpg.format

git config --global user.signingkey DA7F14F53DB10896

#### Alternatively, you may want to use a subkey. In this example, the GPG subkey ID is BC1BF7C91AFFCC0A:

git config --global user.signingkey BC1BF7C91AFFCC0A

#### Optionally, to configure Git to sign all commits and tags by default, enter the following command:
git config --global commit.gpgsign true
git config --global tag.gpgSign true

#### To add your GPG key to your .bashrc startup file, run the following command:

[ -f ~/.bashrc ] && echo -e '\nexport GPG_TTY=$(tty)' >> ~/.bashrc

#
# Backup the public and secret keyrings and trust database
#
## Export all public keys
gpg -a --export > /apps/security/gpg/github_pub_keys.asc

## Export all encrypted private keys (which will also include corresponding public keys)
gpg -a --export-secret-keys > /apps/security/gpg/github_private_keys.asc

## Export gpg's trustdb to a text file
gpg --export-ownertrust > /apps/security/gpg/otrust.txt

# Restore the public and secret keyrings and trust database
gpg --import /apps/security/gpg/github_private_keys.asc
gpg --import /apps/security/gpg/github_pub_keys.asc
gpg -K
gpg -k
gpg --import-ownertrust /apps/security/gpg/otrust.txt

gpg --export DA7F14F53DB10896 > /apps/security/gpg/public.key
gpg --export-secret-key DA7F14F53DB10896 > /apps/security/gpg/private.key

gpg --import /apps/security/gpg/public.key
gpg --allow-secret-key-import /apps/security/gpg/private.key
gpg --import /apps/security/gpg/private.key

#
expect -c 'spawn gpg --edit-key {KEY} trust quit; send "5\ry\r"; expect eof'
#
gpg --list-key
gpg --list-secret-key

#
# Method -3
#
gpg -a --export brijeshdhaker@gmail.com > /apps/security/gpg/brijeshdhaker-public-gpg.key
gpg -a --export-secret-keys brijeshdhaker@gmail.com > /apps/security/gpg/brijeshdhaker-secret-gpg.key
gpg --export-ownertrust > /apps/security/gpg/brijeshdhaker-ownertrust-gpg.txt

# Import secret key (which contains the public key) and ownertrust
gpg --import /apps/security/gpg/brijeshdhaker-secret-gpg.key
gpg --import-ownertrust /apps/security/gpg/brijeshdhaker-ownertrust-gpg.txt

```

#
### Generate SSH Key
#
```bash
ssh-keygen -t rsa -b 4096 -C "brijeshdhaker@gmail.com"

### Add Key in Git Hub
https://github.com/settings/keys

### Telling Git about your SSH key
git config --global gpg.format ssh
git config --global user.signingkey ${HOME}/.ssh/id_rsa.pub
```