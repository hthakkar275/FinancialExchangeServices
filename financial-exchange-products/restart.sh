docker stop financial-exchange-products
docker rm financial-exchange-products
docker rmi financial-exchange-products:latest
docker build -f dockerfile.products -t financial-exchange-products .
docker run -d -e SERVICE_PORT='8080' -e DATABASE_URL='jdbc:postgresql://192.168.2.21/finex-database' -e DATABASE_USERNAME='postgres' -e DATABASE_PASSWORD='password1' -p 8080:8080  --name financial-exchange-products financial-exchange-products:latest

