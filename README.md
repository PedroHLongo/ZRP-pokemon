# Running the application

- To run de application, first you'll need to have docker installed on your machine
- Then you'll need to crete the app image by running the following commands on the project's source folder
```
mvn clean package
```
```
docker build -t pokemon_api .
```
- Then you'll need to run the image created in a docker container following the command
```
docker run -p 8080:8080 pokemon_api
```

# Running sonarqube

- To run the sonarqube and check de coverage + mutations, you'll also need to have docker installed on your machine
- Then you'll need to run the following command on your terminal inside the project's source folder to start the docker-compose
```
docker-compose -f infra/sonar/Docker-compose.yml up
```

- You'll have to wave some time until the compose is up and running
- Then you'll head to localhost:9000 and login with the credentials: 
  - login: admin
  - password: admin
- Then you'll have to change de password to a new one
- After that, you can run the following command in a new terminal, changing the login and password, to run the sonarqube with mutation tests:
```
mvn clean --batch-mode verify org.pitest:pitest-maven:mutationCoverage sonar:sonar '-Dsonar.host.url=http://localhost:9000' '-Dsonar.login={login}' '-Dsonar.password={password}!'
```