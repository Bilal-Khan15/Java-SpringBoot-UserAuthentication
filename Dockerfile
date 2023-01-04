FROM openjdk:8
LABEL maintainer="com.callsign"
EXPOSE 8080
ADD target/springboot-userauthentication.jar springboot-userauthentication.jar
ENTRYPOINT ["java","-jar","/springboot-userauthentication.jar"]