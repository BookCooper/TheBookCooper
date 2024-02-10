FROM postgres:latest
ADD ./ECE366_Project
WORKDIR /ECE366_Project
RUN mvn -e package


ENV POSTGRES_DB=thebookcooper
ENV POSTGRES_USER=BCdev
ENV POSTGRES_PASSWORD=password

FROM eclipse-temurin:latest
COPY ./init-scripts/ /docker-entrypoint-initdb.d/   
COPY --from=build /ECE366_Project/target 

# PSQL port
EXPOSE 5555
