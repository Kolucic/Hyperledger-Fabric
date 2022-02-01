#!/bin/bash
if [ ! -d "mysql" ]; then
  mkdir mysql
fi
export UID=${UID}
export GID=${GID}
docker-compose up -d
