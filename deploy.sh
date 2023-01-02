#!/bin/bash
set -ex

BOT_BUCKET="id42botappstack-botbucketab77be32-1uq2ndywa2a8v"
aws s3 cp id42_bot/target/dist/* s3://$BOT_BUCKET/
pushd id42_cdk
cdk deploy --require-approval=never --all
popd

echo "id42 deploy completed"