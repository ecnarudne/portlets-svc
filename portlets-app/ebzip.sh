#!/bin/sh
rm -f target/eb_docker.zip
./activator docker:stage
cd target/docker
cp ../../aws/Dockerrun.aws.json .
zip -r ../eb_docker.zip .
