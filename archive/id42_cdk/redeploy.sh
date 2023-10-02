#!/bin/bash
set -ex

echo "Redeploying id42_bot..."
cdk destroy --require-approval=never Id42BotEnvironmentStack
cdk deploy --require-approval=never Id42BotEnvironmentStack
echo "Redeployed id42_bot."
