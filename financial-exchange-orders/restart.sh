docker stop financial-exchange-orders
docker rm financial-exchange-orders
docker rmi financial-exchange-orders:latest
docker build -f dockerfile.orders -t financial-exchange-orders .
docker run -d -p 8082:8080 --name financial-exchange-orders financial-exchange-orders:latest

