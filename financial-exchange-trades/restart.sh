docker stop financial-exchange-trades
docker rm financial-exchange-trades
docker rmi financial-exchange-trades:latest
docker build -f dockerfile.trades -t financial-exchange-trades .
docker run -d -e REMOTE_SERVICES_BASE_URL='http://192.168.2.21' -e SERVICE_PORT='8080' -e DATABASE_URL='jdbc:postgresql://192.168.2.21/finex-database' -e DATABASE_USERNAME='postgres' -e DATABASE_PASSWORD='password1' -p 8084:8080  --name financial-exchange-trades financial-exchange-trades:latest

