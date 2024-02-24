[brijeshdhaker@thinkpad docker-hadoop]$ docker swarm init --advertise-addr 127.0.0.1
Swarm initialized: current node (ylafxhtqnwdgq4dil7059l6er) is now a manager.

To add a worker to this swarm, run the following command:

    docker swarm join --token SWMTKN-1-36ho0jiduf2hf1fhruwdlve2x9rlfep2tqrys52b259a5p6yqy-6d8q2jgux31clvb6wt3zihow8 127.0.0.1:2377

To add a manager to this swarm, run 'docker swarm join-token manager' and follow the instructions.


# create an overlay network
docker network create --driver overlay swarm-net

docker network create --driver overlay hbase
