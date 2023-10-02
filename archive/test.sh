#!/bin/bash
set -ex

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
source $SCRIPT_DIR/build.sh

START=$(date)
mvn -f id42_core -fn test
mvn -f id42_bot -fn test

echo "Test run from $START to $(date)"
