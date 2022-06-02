FROM openjdk:11-jre-slim-buster
MAINTAINER darewod
add target/*.jar MatrixBackend.jar
ENTRYPOINT ["java","-jar","MatrixBackend.jar"]