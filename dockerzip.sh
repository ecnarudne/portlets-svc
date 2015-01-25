#!/bin/sh
activator universal:package-zip-tarball
zip --junk-paths target/docker.zip aws/Dockerfile aws/Dockerrun.aws.json target/universal/portlets-svc-1.0-SNAPSHOT.tgz
