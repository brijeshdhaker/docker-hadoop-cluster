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