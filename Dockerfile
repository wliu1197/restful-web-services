
#FROM maven:3.8.1-openjdk-17-slim AS build
#WORKDIR /home/app
#COPY . /home/app
#RUN mvn -f /home/app/pom.xml clean install -DskipTests
# Note: above auto build everytime creates maven <none><none> dangling images uses a lot of memeory 584MB per image
# need to cleaning up dangling images by this : docker rmi $(docker images -f "dangling=true" -q)
# I will manually build .jar file for now imporve it later

FROM openjdk:17-jdk-slim
# instead of mvn build from local and copy to spring boot image we created maven image above 
# maven image will build the restful-web-services-0.0.1-SNAPSHOT.jar for us 
# we can just copy from maven image
# COPY --from=build /home/app/target/*.jar springboot.jar
# Identified memeroy issue with maven auto build above will improve it later now let's manual build jar files first
ADD /target/restful-web-services-0.0.1-SNAPSHOT.jar /home/app/springboot.jar
EXPOSE 8081
CMD exec java -jar /home/app/springboot.jar