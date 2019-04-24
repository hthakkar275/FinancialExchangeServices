docker stop financial-exchange-products
docker rm financial-exchange-products
docker rmi financial-exchange-products:latest
docker build -f dockerfile.products -t financial-exchange-products .
docker run -d -p 8080:8080 --name financial-exchange-products financial-exchange-products:latest

