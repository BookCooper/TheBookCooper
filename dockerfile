FROM postgres:latest
FROM maven:3.9.6-eclipse-temurin-21 AS build
ADD ./ECE366_Project
WORKDIR /ECE366_Project
RUN mvn -e package


ENV POSTGRES_DB=thebookcooper
ENV POSTGRES_USER=BCdev
ENV POSTGRES_PASSWORD=password

FROM eclipse-temurin:latest
COPY ./init-scripts/ /docker-entrypoint-initdb.d/   
COPY --from=build /ECE366_Project/target 
ENTRYPOINT ["java", "-jar", "ECE366_Project-0.0.1-SNAPSHOT.jar"]

# PSQL port
EXPOSE 5555
