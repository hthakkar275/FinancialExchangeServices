docker stop financial-exchange-orderbooks
docker rm financial-exchange-orderbooks
docker rmi financial-exchange-orderbooks:latest
docker build -f dockerfile.orderbooks -t financial-exchange-orderbooks .
docker run -d -e REMOTE_SERVICES_BASE_URL='http://192.168.2.21' -e SERVICE_PORT='8080' -p 8083:8080  --name financial-exchange-orderbooks financial-exchange-orderbooks:latest

