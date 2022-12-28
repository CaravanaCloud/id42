#!/bin/bash
set -ex

mvn install
mkdir -p id42_bot/target/dist
cp id42_bot/target/*-runner.jar "id42_bot/target/dist"
mvn -f id42_cdk install
pushd id42_app && ./build.sh && popd

echo "build done."