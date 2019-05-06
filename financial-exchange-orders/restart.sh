docker stop financial-exchange-orders
docker rm financial-exchange-orders
docker rmi financial-exchange-orders:latest
docker build -f dockerfile.orders -t financial-exchange-orders .
docker run -d -e REMOTE_SERVICES_BASE_URL='http://192.168.2.21' -e REMOTE_SERVICES_USE_NONSTANDARD_PORT='true' -e SERVICE_PORT='8080' -e DATABASE_URL='jdbc:postgresql://192.168.2.21/finex-database' -e DATABASE_USERNAME='postgres' -e DATABASE_PASSWORD='password1' -p 8082:8080  --name financial-exchange-orders financial-exchange-orders:latest

