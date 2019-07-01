docker stop finex-trades
docker rm finex-trades
docker rmi hthakkar/finex-trades:latest
docker build -f dockerfile.trades -t hthakkar/finex-trades .
docker run -d -e REMOTE_SERVICES_BASE_URL='http://10.0.0.13' -e SERVICE_PORT='8080' -e DATABASE_URL='jdbc:postgresql://10.0.0.13/finex-database' -e DATABASE_USERNAME='postgres' -e DATABASE_PASSWORD='password1' -p 8084:8080  --name finex-trades hthakkar/finex-trades:latest

