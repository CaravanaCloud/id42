#!/bin/bash
set -ex

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
ENV_PROJECT=$(basename ${ENV_PROJECT:-$DIR})
ENV_USER=${ENV_USER:-$USER}

SSM_PATH="/$ENV_PROJECT/$ENV_USER"
SSM_PARAMS=$(aws ssm get-parameters-by-path --path $SSM_PATH --query "Parameters[*].[Name,Value]" --output text)

#TODO: Iterate on SSMPARAMS and export them 
echo $SSM_PARAMS

#aws ssm get-parameters --names "/id42/jufaerma/PERSONAL_API_KEY"

echo Loading parameter for user: $ENV_USER project: $ENV_PROJECT
