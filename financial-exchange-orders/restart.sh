docker stop finex-orders
docker rm finex-orders
docker rmi hthakkar/finex-orders:latest
docker build -f dockerfile.orders -t hthakkar/finex-orders .
docker run -d -e REMOTE_SERVICES_BASE_URL='http://10.0.0.13' -e REMOTE_SERVICES_USE_NONSTANDARD_PORT='true' -e SERVICE_PORT='8080' -e DATABASE_URL='jdbc:postgresql://10.0.0.13/finex-database' -e DATABASE_USERNAME='postgres' -e DATABASE_PASSWORD='password1' -p 8082:8080  --name finex-orders hthakkar/finex-orders:latest

