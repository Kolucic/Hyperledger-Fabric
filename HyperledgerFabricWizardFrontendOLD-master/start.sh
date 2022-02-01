#!/bin/bash
# This script starts the network defined in the test-network.json
# Place this script inside the test-network folder before running it
export PATH=$PWD/bin:$PATH
echo "#################################################################"
echo "#                  Downloading prerequisites                    #"
echo "#################################################################"
bash prereq.sh > output.txt # Downloads binaries
echo "#################################################################"
echo "#                   Creating certificates                       #"
echo "#################################################################"
bash $PWD/orgs/artifacts.sh > output.txt # Starts CAs and creates artifacts per org
echo "#################################################################"
echo "#            Creating genesis block and channels.tx             #"
echo "#################################################################"
bash $PWD/orgs/configtx.sh > output.txt # Creates genesis block and channels txs
# Starts the containers
echo "#################################################################"
echo "#                Starting peers and orderers                    #"
echo "#################################################################"
docker-compose -f $PWD/orgs/org1.example.com/entities/peer0/docker-compose.yaml up -d 
docker-compose -f $PWD/orgs/org2.example.com/entities/peer0/docker-compose.yaml up -d
docker-compose -f $PWD/orgs/ordererOrg.example.com/entities/orderer/docker-compose.yaml up -d
sleep 3s
# Links the started containers so that they can communicate
docker network create test-network
docker network connect test-network orderer.ordererOrg.example.com
docker network connect test-network peer0.org1.example.com
docker network connect test-network peer0.org2.example.com
echo "#################################################################"
echo "#                       Creating channel                        #"
echo "#################################################################"
bash $PWD/orgs/mychannel/create.sh > output.txt  # Creates the channel
echo "#################################################################"
echo "#                     Joining orgs to channel                   #"
echo "#################################################################"
bash $PWD/orgs/mychannel/join.sh > output.txt # Joins peers to the channel
