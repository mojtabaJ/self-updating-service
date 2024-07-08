Self Updating Service
=====================

Overview
--------

The Self Updating Service repository contains three Spring Boot projects that demonstrate a self-updating mechanism for a client application. These projects are designed to showcase how a central update server can manage and distribute updates to client applications.

### Projects:

1.  **update-server**: This project serves as the update server. It hosts the latest version information and JAR files that clients can download to update themselves.

2.  **client-app**: The client application that needs to be updated. It checks with the update server periodically to see if a new version is available.

3.  **update-runner**: This project acts as a runner or updater agent. It periodically checks the update server for new versions, downloads them, replaces the old client JAR with the new one, and restarts the client application.


Project Structure
-----------------

Each project is structured as follows:

### update-server

*   **Purpose**: Hosts update information and JAR files.
*   **Endpoints**: Provides version information and download links to client applications.
*   **Default Port**: 8081

### client-app

*   **Purpose**: A sample client application that needs to stay updated.
*   **Functionality**: Checks for updates from the update-server.
*   **Default Port**: 8082

### update-runner

*   **Purpose**: Monitors for updates and manages the client application lifecycle.
*   **Functionality**: Checks for updates from the update-server, downloads new versions of client-app, replaces the old version, and restarts the client application.
*   **Default Port**: 8083

Build and Run Instructions
--------------------------

### Build the Projects

Navigate into each project directory and build them using Maven:

```bash
cd ../update-server
mvn clean install

cd ../client-app
mvn clean install

cd ../update-runner
mvn clean install
```

### Start the Applications

Start each Spring Boot application in separate terminal windows:

```bash
java -jar update-server/target/update-server.jar
java -jar client-app/target/client-app.jar
java -jar update-runner/target/update-runner.jar
```

### Access the Client Application

*   Open a web browser and navigate to [http://localhost:8083/](http://localhost:8083/) to see the client application's greeting message.

### Trigger Update

To trigger an update manually, make a GET request to [http://localhost:8082/update](http://localhost:8082/update) using tools like cURL or a web browser. This will check for updates, download, and run the new version of `client-app`.

* * *

