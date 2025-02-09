# **CarCar**

CarCar is a backend system for a car-sharing app, developed as a university project, to help families, friends, and companies manage shared vehicles.

It allows users to create groups, reserve cars, log trips, and submit maintenance or damage reports, with robust group-based permissions and administration.

---

## **Installation Instructions**

### **Step 1: Install PostgreSQL**

1. Download PostgreSQL from [https://www.postgresql.org/download/](https://www.postgresql.org/download/).
2. During installation:
   - Set the password for the `postgres` user (e.g., `admin` or any other password).
   - Choose port `5432` (if unavailable, note the port you select).

---

### **Step 2: Set Up PostgreSQL**

1. Open a command prompt (CMD) and type:
   ```
   psql -U postgres
   ```
2. Enter the password you set during installation.
3. Create a new database named `carcardb` by running:
   ```
   CREATE DATABASE carcardb;
   ```
4. Grant all privileges on the database to the `postgres` user:
   ```
   GRANT ALL PRIVILEGES ON DATABASE carcardb TO postgres;
   ```

---

### **Step 3: Clone the Project**

1. Clone the project repository:
   ```
   git clone https://github.com/Revilo1512/CarCarApp
   ```
2. Open the folder `SpringBootServer` as a project in **IntelliJ IDEA** or any other IDE.

---

### **Step 4: Configure the Backend**

1. Navigate to the file:  
   ```
   src/main/resources/application.properties
   ```
2. Update the following properties based on your PostgreSQL configuration:
   ```
   spring.datasource.url=jdbc:postgresql://localhost:YOURPORT/carcardb
   spring.datasource.username=YOURUSERNAME
   spring.datasource.password=YOURPASSWORD
   ```
   - Replace `YOURPORT`, `YOURUSERNAME`, and `YOURPASSWORD` with your PostgreSQL configuration.
   - If you followed the default setup (port: 5432, username: postgres, password: admin), no changes are needed.

---

### **Step 5: Start the Backend Application**

1. Open your IDE and run:
   ```
   CarcarBackendApplication
   ```
   This will start the backend server.

---

### **Step 6: Configure the Android Frontend**

1. Open the `AndroidApp` folder as a project in Android Studio.
2. Update API configurations in `com/example/carcarapplication/api_helpers/RetrofitClient.kt`:
   ```
   BASE_URL = "http://YOUR_IP_ADDRESS:8080/"
   ```
   Replace `YOUR_IP_ADDRESS` with the IP address of the backend server.
3. Modify network security in `res/xml/network_security_config.xml`:
   ```
   <domain includeSubdomains="true">YOUR_IP_ADDRESS</domain>
   ```
   Replace `YOUR_IP_ADDRESS` with the same IP address used in `BASE_URL`.
4. Set up a virtual device in Android Studio's Device Manager:
   - Choose a medium phone (e.g., Pixel 9 Pro).
5. (Optional) To run the app on a physical Android phone:
   - Connect your phone via USB.
6. Start the Frontend Application
- Run the app configuration from Android Studio on a virtual or physical device.

---

## Notes

- Ensure both the backend and frontend are running on the same network for proper connectivity.
- If you encounter issues, double-check the configurations and logs for errors.
