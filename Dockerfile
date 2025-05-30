FROM maven:3.8.4-openjdk-17-slim

WORKDIR /backend
COPY ./server .

# RUN mvn test
# RUN mvn verify
RUN mvn clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/server-0.0.1-SNAPSHOT.jar", "fully.qualified.package.Application"]

