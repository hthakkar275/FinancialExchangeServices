docker stop financial-exchange-participants
docker rm financial-exchange-participants
docker rmi financial-exchange-participants:latest
docker build -f dockerfile.participants -t financial-exchange-participants .
docker run -d -p 80:8080 --name financial-exchange-participants financial-exchange-participants:latest

