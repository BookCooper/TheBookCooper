--CREATE DATABASE thebookcooper;
--GRANT ALL PRIVILEGES ON DATABASE thebookcooper TO BCdev;
--GRANT ALL PRIVILEGES ON DATABASE thebookcooper TO postgres;
CREATE ROLE BCdev WITH LOGIN PASSWORD 'password';
--CREATE DATABASE thebookcooper;
GRANT ALL PRIVILEGES ON DATABASE thebookcooper TO BCdev;