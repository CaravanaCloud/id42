FROM docker.io/nginx:alpine
COPY default.conf /etc/nginx/conf.d/default.conf
EXPOSE 8383
ENTRYPOINT ["nginx","-g","daemon off;"]
# docker build --no-cache -t id42-proxy .
# docker run --rm -it -p 8080:80 id42-proxy
# docker build --no-cache -t id42-proxy . && docker run --network="host" --rm -it -p 8080:80 id42-proxy