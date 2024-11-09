FROM openjdk:17-jdk-slim
ADD /target/restful-web-services-0.0.1-SNAPSHOT.jar springboot.jar
EXPOSE 8081
CMD exec java -jar springboot.jar