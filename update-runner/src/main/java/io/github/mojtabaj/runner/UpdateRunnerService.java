package io.github.mojtabaj.runner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Service class responsible for managing the self-updating mechanism of the client application.
 */
@Service
public class UpdateRunnerService {

    // Configuration constants
    private static final String UPDATE_SERVER_URL = "http://127.0.0.1:8081/update";
    private static final String JAR_FILE_NAME = "client_app.jar";
    private static final String VERSION_FILE = "version.txt";
    private static final VersionInfo VERSION_INFO = new VersionInfo("0.0.0", JAR_FILE_NAME, "");

    private Process appProcess = null;
    /**
     * Constructor that initializes the UpdateRunnerService.
     * It loads the version information from a file on startup and starts the initial client application.
     */
    public UpdateRunnerService() {
        try {
            try {
                startClientApp(VERSION_INFO); // Start the initial client-app.jar when UpdateRunner starts
            }catch (FileNotFoundException e){
                checkForUpdates();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to periodically check for updates.
     * If a new version is available, it replaces the old version and updates the version file.
     */
    public void checkForUpdates() {
        try {
            VersionInfo updateInfo = getLatestVersion();
            if (!updateInfo.getVersion().equals(VERSION_INFO.getVersion())) {
                replaceOldVersion(updateInfo);
                startClientApp(updateInfo); // Start the updated client-app
                saveVersionToFile(updateInfo); // Update version in file
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Saves the version information to a file.
     *
     * @param version The VersionInfo object containing version details to save.
     * @throws IOException If an I/O error occurs.
     */
    private void saveVersionToFile(VersionInfo version) throws IOException {
        VERSION_INFO.update(version);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(VERSION_FILE))) {
            writer.write(version.getVersion());
            writer.newLine();
            writer.write(version.getJar());
            writer.newLine();
            writer.write(version.getUrl());
        }
    }

    /**
     * Retrieves the latest version information from an update server.
     *
     * @return The VersionInfo object containing the latest version details.
     * @throws IOException If an I/O error occurs.
     */
    private VersionInfo getLatestVersion() throws IOException {
        // Replace with your update URL
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new URL(UPDATE_SERVER_URL).openStream()))) {
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            String json = content.toString();
            VersionInfo res = parseUpdateInfoJson(json);
            res.setJar(res.getVersion().replace(".","_") + "_" + JAR_FILE_NAME);
            return res;
        }
    }

    /**
     * Parses the JSON string into a VersionInfo object.
     *
     * @param json The JSON string representing version information.
     * @return The VersionInfo object parsed from the JSON string.
     */
    private VersionInfo parseUpdateInfoJson(String json) {
        VersionInfo updateInfo = null;
        try {
            updateInfo = new ObjectMapper().readValue(json, VersionInfo.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        // Implement your custom parsing logic from JSON string to UpdateInfo object
        return updateInfo;
    }

    /**
     * Replaces the old version of the client application with the new version.
     * Downloads the new client-app.jar from the specified URL and stops the old client application.
     *
     * @param updateInfo The VersionInfo object containing information about the update.
     * @throws IOException            If an I/O error occurs.
     * @throws InterruptedException   If the thread is interrupted while waiting for a process to complete.
     */
    private void replaceOldVersion(VersionInfo updateInfo) throws IOException, InterruptedException {
        // Download the new client-app.jar from the specified URL
        String downloadUrl = updateInfo.getUrl(); // Assuming versionInfo has getUrl() method
        if (downloadUrl != null && !downloadUrl.isEmpty()) {
            try (InputStream in = new URL(downloadUrl).openStream()) {
                Files.copy(in, Paths.get(updateInfo.getJar()), StandardCopyOption.REPLACE_EXISTING);
            }
        } else {
            throw new IllegalArgumentException("Download URL is null or empty.");
        }
    }

    /**
     * Starts the client application using the JAR file specified in the VersionInfo object.
     * Throws FileNotFoundException if the JAR file does not exist.
     *
     * @param version The VersionInfo object containing information about the client application.
     * @throws IOException If an I/O error occurs.
     */
    private void startClientApp(VersionInfo version) throws IOException {
        Path jarPath = Paths.get(version.getJar());
        // Check if the JAR file exists
        if (Files.exists(jarPath)) {
            synchronized (this) {
                if (appProcess != null) {
                    appProcess.destroy();
                    appProcess = null;
                }
                ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", version.getJar());
                appProcess = processBuilder.start();
            }
        } else {
            throw new FileNotFoundException("JAR file not found: " + jarPath.toString());
        }
    }


    //</editor-fold>


}
