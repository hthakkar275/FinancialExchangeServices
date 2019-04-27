docker stop financial-exchange-participants
docker rm financial-exchange-participants
docker rmi financial-exchange-participants:latest
docker build -f dockerfile.participants -t financial-exchange-participants .
docker run -d -e SERVICE_PORT='8080' -e DATABASE_URL='jdbc:postgresql://192.168.2.21/finex-database' -e DATABASE_USERNAME='postgres' -e DATABASE_PASSWORD='password1' -p 8081:8080  --name financial-exchange-participants financial-exchange-participants:latest

