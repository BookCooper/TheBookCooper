FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY . /ECE366_Project
WORKDIR /ECE366_Project
RUN mvn -e package


ENV POSTGRES_DB=thebookcooper
ENV POSTGRES_USER=BCdev
ENV POSTGRES_PASSWORD=password

FROM eclipse-temurin:latest
COPY ./init-scripts/ /docker-entrypoint-initdb.d/   
COPY --from=build /ECE366_Project/target /app/target
ENTRYPOINT ["java", "-jar", "/app/target/ECE366_Project-1.0-SNAPSHOT.jar"]

# PSQL port
EXPOSE 5555
