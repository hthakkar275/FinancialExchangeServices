docker stop finex-participants
docker rm finex-participants
docker rmi hthakkar/finex-participants:latest
docker build -f dockerfile.participants -t hthakkar/finex-participants .
docker run -d -e SERVICE_PORT='8080' -e DATABASE_URL='jdbc:postgresql://10.0.0.13/finex-database' -e DATABASE_USERNAME='postgres' -e DATABASE_PASSWORD='password1' -p 8081:8080  --name finex-participants hthakkar/finex-participants:latest 

