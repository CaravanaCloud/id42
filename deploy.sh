#!/bin/bash
set -ex

BOT_BUCKET="id42botappstack-botbucketab77be32-g3h4qcn5bbgp"
aws s3 cp id42_bot/target/dist/* s3://$BOT_BUCKET/
pushd id42_cdk
cdk deploy --require-approval=never Id42BotEnvironmentStack # --all
popd

echo "id42 deploy completed"