#!/bin/bash
set -ex

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
ENV_PROJECT=$(basename ${ENV_PROJECT:-$DIR})
ENV_USER=${ENV_USER:-$USER}

SSM_PATH="/$ENV_PROJECT/$ENV_USER"
SSM_PARAMS=$(aws ssm get-parameters-by-path --path $SSM_PATH --query "Parameters"  | jq -c -r '.[]')

echo Loading parameter for user: $ENV_USER project: $ENV_PROJECT


echo "" > .env
#TODO: Iterate on SSMPARAMS and export them 
for SSM_PARAM in ${SSM_PARAMS[@]}; do
    echo "====" 
    PARAM_KEY=$(echo $SSM_PARAM | jq -r '.Name' | cut -d '/' -f 4)
    PARAM_VAL=$(echo $SSM_PARAM | jq -r '.Value')
    echo $PARAM_KEY=$PARAM_VAL | tee -a .env
    echo "===="
    # whatever you are trying to do ...
done
# aws ssm get-parameters --names "/id42/jufaerma/PERSONAL_API_KEY"

cat .env

