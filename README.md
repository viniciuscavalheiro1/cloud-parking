# cloud-parking

## iniciar o container
docker run --name p-db-parking -p 5490:5432 -e POSTGRES_DB=parking -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=123 -d postgres:latest