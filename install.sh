#!/bin/bash
set -ex

echo "build java modules"
MAVEN_ARGS="-B -DskipTests -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn"
MAVEN_ARGS="$MAVEN_ARGS install"
mvn -f id42_kernel $MAVEN_ARGS
mvn -f id42_core $MAVEN_ARGS
mvn -f id42_api $MAVEN_ARGS
mvn -f id42_bot $MAVEN_ARGS
mkdir -p id42_bot/target/dist
echo "copy bot runner to dist"
cp id42_bot/target/*-runner.jar "id42_bot/target/dist"
echo "build flutter module"
pushd id42_app && ./build.sh && popd
echo "build cdk module"
mvn -f id42_cdk install

echo "build done."