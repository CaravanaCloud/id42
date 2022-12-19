#!/bin/bash
set -ex

mvn -f id42_core install
mvn -f id42_api install
mvn -f id42_cdk install
pushd id42_app && ./build.sh && popd

echo "build done."