ARG GITPOD_TAG=2023-01-16-03-31-28
ARG JAVA_TAG=11.0.17-amzn
FROM gitpod/workspace-full-vnc:${GITPOD_TAG}

RUN bash -c ". /home/gitpod/.sdkman/bin/sdkman-init.sh && sdk install java ${JAVA_TAG} && sdk default java ${JAVA_TAG} && sdk install quarkus"
RUN bash -c "curl 'https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip' -o 'awscliv2.zip' && unzip awscliv2.zip && sudo ./aws/install"
RUN bash -c "npm install -g aws-cdk"
