FROM openjdk:11
VOLUME /tmp
EXPOSE 8085
ADD ./target/ms-bank-account-0.0.1-SNAPSHOT.jar ms-bank-account.jar
ENTRYPOINT ["java","-jar","/ms-bank-account.jar"]