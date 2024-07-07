package io.github.mojtabaj.runner;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
class UpdateRunner {

    private static final String CURRENT_VERSION = "1.0.0";
    private static final String UPDATE_URL = "http://yourserver.com/update";
    private static final String JAR_NAME = "client-app.jar";

    @Scheduled(fixedRate = 60000) // Check for updates every minute
    public void checkForUpdates() {
        try {
            String latestVersion = getLatestVersion();
            if (latestVersion != null && !latestVersion.equals(CURRENT_VERSION)) {
                downloadNewVersion(latestVersion);
                replaceOldVersion();
                runClientApp();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getLatestVersion() throws IOException {
        URL url = new URL(UPDATE_URL);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            String json = content.toString();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> updateInfo = mapper.readValue(json, Map.class);
            return updateInfo.get("version");
        }
    }

    private void downloadNewVersion(String version) throws IOException {
        String jarUrl = String.format("http://yourserver.com/yourapp-%s.jar", version);
        try (InputStream in = new URL(jarUrl).openStream()) {
            Files.copy(in, Paths.get(JAR_NAME), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void replaceOldVersion() {
        // Implement logic to stop the old application if running
        // Optionally, rename the downloaded JAR to the working JAR name if needed
    }

    private void runClientApp() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("java", "-jar", JAR_NAME);
        pb.inheritIO();
        pb.start();
    }
}