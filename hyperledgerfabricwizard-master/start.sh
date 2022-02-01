#!/bin/bash
# This scripts starts the network defined in the test-network.json
export PATH=$PWD/bin:$PATH
bash prereq.sh # Downloads binaries
bash $PWD/orgs/artifacts.sh # Starts CAs and creates artifacts per org
bash $PWD/orgs/configtx.sh # Creates genesis block and channels txs
# Starts the containers
docker-compose -f $PWD/orgs/org1.example.com/entities/peer0/docker-compose.yaml up -d
docker-compose -f $PWD/orgs/org2.example.com/entities/peer0/docker-compose.yaml up -d
docker-compose -f $PWD/orgs/ordererOrg.example.com/entities/orderer/docker-compose.yaml up -d
sleep 3s
# Links the started containers so that they can communicate
docker network create test-network
docker network connect test-network orderer.ordererOrg.example.com
docker network connect test-network peer0.org1.example.com
docker network connect test-network peer0.org2.example.com
bash $PWD/orgs/mychannel/create.sh # Creates the channel
bash $PWD/orgs/mychannel/join.sh # Joins peers to the channel
