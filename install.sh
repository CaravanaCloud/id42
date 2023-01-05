#!/bin/bash
set -ex

echo "build java modules"
mvn -f id42_core install
mvn -f id42_api install
mvn -f id42_bot install
mkdir -p id42_bot/target/dist
echo "copy bot runner to dist"
cp id42_bot/target/*-runner.jar "id42_bot/target/dist"
echo "build flutter module"
pushd id42_app && ./build.sh && popd
echo "build cdk module"
mvn -f id42_cdk install

echo "build done."