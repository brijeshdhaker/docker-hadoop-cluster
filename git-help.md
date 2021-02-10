### Generate SSH Key
ssh-keygen -t rsa -b 4096 -C "brijeshdhaker@gmail.com"

### Add Key in Git Hub

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

# deleted a file but didn’t commit
git checkout HEAD <filename>

# deleted a file and committed the deletion
git reset --hard HEAD~1