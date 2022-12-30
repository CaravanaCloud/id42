#!/bin/bash
set -ex

echo "build java modules"
mvn install
mkdir -p id42_bot/target/dist
echo "build flutter module"
pushd id42_app && ./build.sh && popd
echo "build java modules"
mvn -f id42_cdk install
echo "copy bot runner to s3"
cp id42_bot/target/*-runner.jar "id42_bot/target/dist"

echo "build done."