CarCar

CarCar is a backend system for a car-sharing app, developed as a university project, to help families, friends, and companies manage shared vehicles. 

It allows users to create groups, reserve cars, log trips, and submit maintenance or damage reports, with robust group-based permissions and administration.

Installation Instructions:

Step 1: Install PostgreSQL

Download PostgreSQL from https://www.postgresql.org/download/.

During installation:

Set the password for the "postgres" user (e.g., "admin" or any other password).

Choose port 5432 (if unavailable, note the port you select).

Step 2: Set Up PostgreSQL

Open a command prompt (CMD) and type:

psql -U postgres

Enter the password you set during installation.

Create a new database named carcardb:

CREATE DATABASE carcardb;

Grant all privileges on the database to the postgres user:

GRANT ALL PRIVILEGES ON DATABASE carcardb TO postgres;

Step 3: Clone the Project

Clone the project repository:

git clone https://github.com/Revilo1512/CarCarApp

Open the folder SpringBootServer as a project in IntelliJ IDEA or any other IDE.

Step 4: Configure Backend

Navigate to src/main/resources/application.properties.

Update the following properties based on your PostgreSQL configuration:

spring.datasource.url=jdbc:postgresql://localhost:YOURPORT/carcardb
spring.datasource.username=YOURUSERNAME
spring.datasource.password=YOURPASSWORD

Replace YOURPORT, YOURUSERNAME, and YOURPASSWORD with your PostgreSQL configuration.

If you followed the default setup (port: 5432, username: postgres, password: admin), no changes are needed.

Step 5: Start the Backend Application

Run CarcarBackendApplication from your IDE to start the backend server.

Step 6: Frontend Setup

To be continiued







