FROM postgres:latest

ENV POSTGRES_DB=thebookcooper
ENV POSTGRES_USER=BCdev
ENV POSTGRES_PASSWORD=password

COPY ./init-scripts/ /docker-entrypoint-initdb.d/

# PSQL port
EXPOSE 5555

CMD ["postgres"]