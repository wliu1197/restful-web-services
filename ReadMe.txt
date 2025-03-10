set up docker mysql image and runs in docker container
1 docker images
2 docker pull mysql
3 
docker run --name mysql-social-media -p 3306:3306 -e MYSQL_ROOT_PASSWORD=dummypassword -e MYSQL_DATABASE=social-media-database -e MYSQL_USER=social-media-user -e MYSQL_PASSWORD=dummypassword -d mysql:latest

4 docker into mysql server
docker exec -it mysql-social-media bash
mysql -u root -p social-media-database
password:dummypassword

SHOW databases;
USE social-media-database;
SHOW tables;
DESCRIBE student;

show databases; # lists all databases
use information_schema; # connect to the mysql schema
show tables;


Once mysql is in docker container


1 in application.properties set up sql connection to running mysql in docker
	spring.datasource.url=jdbc:mysql://mysql-social-media:3306/social-media-database
2 to create new .jar in target folder without test as local won't be able to connect to mysql-social-media
	mvn install -DskipTests

3 we can run Dockerfile to build docker image for our rest api
docker build -t wenspringboot/rest-web-services:v1 .

MacBook-Pro:service-5gn-cpanel wenhaoliu$ docker images
REPOSITORY                                  TAG       IMAGE ID       CREATED          SIZE
wenspringboot/rest-web-services             v1        3f8d4ea9a15c   11 minutes ago   468MB
mysql                                       latest    10db11fef9ce   3 weeks ago      602MB

4 run rest api in docker container link with mysql container
MacBook-Pro:restful-web-services wenhaoliu$ docker container ps
CONTAINER ID   IMAGE          COMMAND                  CREATED         STATUS         PORTS                               NAMES
ab4efee2291e   mysql:latest   "docker-entrypoint.s…"   7 seconds ago   Up 6 seconds   0.0.0.0:3306->3306/tcp, 33060/tcp   mysql-social-media

docker run -d -t --name rest-web-services -p 8081:8081 --link mysql-social-media wenspringboot/rest-web-services:v1




---------------------------
docker-compose.yml way of set up both mysql and rest api in docker containers
1 create docker-compose.yml file save folder level as Dockerfile
version: "3.7"
services:
 mysql-social-media:
  image: mysql:latest
  restart: always
  container_name: mysql-social-media
  ports:
   - 3306:3306
  networks:
   - springapimysql-net  
  environment:
   MYSQL_DATABASE: social-media-database
   MYSQL_USER: social-media-user
   MYSQL_PASSWORD: dummypassword
   MYSQL_ROOT_PASSWORD: dummypassword
   
 rest-web-services:
  image: rest-web-services:v1
  build: .
  restart: always
  container_name: rest-web-services
  ports:
   - 8081:8081
  networks:
   - springapimysql-net
  environment:
   - spring.datasource.url=jdbc:mysql://mysql-social-media:3306/social-media-database
   - spring.datasource.username=root
   - spring.datasource.password=dummypasswor
  depends_on:
   - mysql-social-media
  volumes:
   - .m2:/root/.m2
   - type: bind
     source: /Users/wenhaoliu/Desktop/interview preparing/SpringbootUdemy/restful-web-services/target/restful-web-services-0.0.1-SNAPSHOT.jar
     target: /home/app/springboot.jar

networks:
  springapimysql-net:
  
version: Version of Docker Compose file format.
services: My application has two services: app (Spring Boot) and mysqldb (MySQL database image).
container_name: name of container we run images on
build: Configuration options that are applied at build time that we defined in the Dockerfile with relative path
image: Official Docker image from docker hub or build our own docker image from Dockerfile and give it a name and tag version
volumes: Named volumes that keeps our data alive after restart.
network: The two services should be belong to one network.
depends_on: Dependency order, mysqldb is started before app

1 manually build the new jar file 
mvn clean install -DskipTests

2 use build --no-cache first if we don't set up type: bind (mount) source and target 
as image already exist it won't rebuild image and copy new .jar to container
docker-compose build --no-cache

if we have use  above type: bind (mount) source and target in docker-compose.yaml 
it will copy .jar to container when we run container so we can skip docker-compose build --no-cache 
because existing image should be fine for us
docker-compose up

image pull from docker hub e.g: mysql:latest
image created from our Dockerfile e.g: rest-web-services :v1
MacBook-Pro:restful-web-services wenhaoliu$ docker images
REPOSITORY                                  TAG       IMAGE ID       CREATED         SIZE
rest-web-services                           v1        0e85554208f6   4 minutes ago   468MB
mysql                                       latest    10db11fef9ce   3 weeks ago     602MB

MacBook-Pro:restful-web-services wenhaoliu$ docker container ps
CONTAINER ID   IMAGE                                    COMMAND                  CREATED          STATUS          PORTS                               NAMES
367af25cd5fd   restful-web-services-rest-web-services   "/bin/sh -c 'exec ja…"   19 seconds ago   Up 4 seconds    0.0.0.0:8081->8081/tcp              rest-web-services
571f713b2060   mysql:latest                             "docker-entrypoint.s…"   19 seconds ago   Up 18 seconds   0.0.0.0:3306->3306/tcp, 33060/tcp   mysql-social-media

3 docker-compose down

Note: if any changes made in Dockerfile or used mvn image as build to build jar file 
it will require to build docker-compose build --no-cache first then run up
docker-compose build --no-cache
docker-compose up 


Clean up <none><none> dangling images that uses a lot of memory 
docker rmi $(docker images -f "dangling=true" -q)
