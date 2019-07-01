docker stop finex-orderbooks
docker rm finex-orderbooks
docker rmi hthakkar/finex-orderbooks:latest
docker build -f dockerfile.orderbooks -t hthakkar/finex-orderbooks .
docker run -d -e REMOTE_SERVICES_BASE_URL='http://10.0.0.13' -e REMOTE_SERVICES_USE_NONSTANDARD_PORT='true' -e SERVICE_PORT='8080' -p 8083:8080  --name finex-orderbooks hthakkar/finex-orderbooks:latest

