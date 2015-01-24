portlets-svc
============

Portlets service and related jobs


# To create Docker zip for AWS

cd <project-home:portlets-svc>
./aws.sh
# Upload the zip target/docker.zip to beanstalk-docker


# To run on local Docker

cd target
unzip -o docker.zip -d portlets-svc
cd portlets-svc
sudo docker build --force-rm --rm --tag="portlets-svc:1.0-SNAPSHOT" .
sudo docker run --name "portlets-svc-1.0-SNAPSHOT" --interactive --rm=true --tty --publish-all portlets-svc:1.0-SNAPSHOT

# check mapped localhost port by
sudo docker port portlets-svc-1.0-SNAPSHOT 9000
