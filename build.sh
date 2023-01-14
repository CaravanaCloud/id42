#!/bin/bash
set -ex
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

echo "build java modules"
MAVEN_ARGS="-B -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn"
MAVEN_ARGS="$MAVEN_ARGS -DskipTests" #-Dmaven.javadoc.skip=true -Dcheckstyle.skip=true -Dspotbugs.skip=true -Dfindbugs.skip=true -Dpmd.skip=true -Djacoco.skip=true"
MAVEN_ARGS="$MAVEN_ARGS install"
mvn -f id42_kernel $MAVEN_ARGS
mvn -f id42_core $MAVEN_ARGS
mvn -f id42_api $MAVEN_ARGS
mvn -f id42_bot $MAVEN_ARGS
mkdir -p "$DIR/id42_bot/target/dist"
echo "copy bot runner to dist"
cp id42_bot/target/*-runner.jar "$DIR/id42_bot/target/dist"
echo "build flutter module"
# pushd id42_app && ./build.sh && popd
echo "build cdk module"
mvn -f id42_cdk $MAVEN_ARGS
echo "build done."