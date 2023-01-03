
To Compile/package
mvn clean package

To install locally
mvn clean install

To run
java -jar target/data-mesh-visitors-0.0.1-SNAPSHOT.jar

To Dockerize
docker build --tag=data-mesh-visitors:latest .


docker run -p 8080:8080 data-mesh-visitors:latest

# Related Blog Posts

* [Using Kafka with Spring Boot](https://reflectoring.io/spring-boot-kafka/)
