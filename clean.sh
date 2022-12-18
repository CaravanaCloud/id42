#!/bin/bash
set -ex

mvn -f id42_core clean
mvn -f id42_api clean
mvn -f id42_cdk clean
pusd id42_app && flutter clean && popd

echo "clean done."