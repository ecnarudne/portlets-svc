#!/bin/sh
activator universal:package-zip-tarball
zip --junk-paths target/docker.zip aws/Dockerfile target/universal/portlets-svc-1.0-SNAPSHOT.tgz
