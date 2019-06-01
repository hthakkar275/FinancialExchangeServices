docker stop finex-products
docker rm finex-products
docker rmi hthakkar/finex-products:latest
docker build -f dockerfile.products -t hthakkar/finex-products .
docker run -d -e SERVICE_PORT='8080' -e DATABASE_URL='jdbc:postgresql://192.168.2.21/finex-database' -e DATABASE_USERNAME='postgres' -e DATABASE_PASSWORD='password1' -p 8080:8080  --name finex-products hthakkar/finex-products:latest

