FROM java:8
VOLUME /tmp
ADD target/SmartShoppingServices-0.0.1-SNAPSHOT.jar /SmartShoppingServices.jar
COPY crypto/ /crypto
COPY util/ /util
COPY chaincode/ /chaincode
RUN bash -c 'touch /SmartShoppingServices.jar'
EXPOSE 8080
ENTRYPOINT ["java","-jar","/SmartShoppingServices.jar"]
