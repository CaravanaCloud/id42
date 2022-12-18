#!/bin/bash
set -ex

mvn -f id42_core verify
mvn -f id42_api verify
mvn -f id42_cdk verify
pusd id42_app && ./build.sh && popd

echo "build done."