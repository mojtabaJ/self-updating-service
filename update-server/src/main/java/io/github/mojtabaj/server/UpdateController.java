package io.github.mojtabaj.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UpdateController is a Spring Boot REST controller that provides an endpoint to retrieve
 * update information for the client application.
 */
@RestController
public class UpdateController {

    /**
     * Handles HTTP GET requests to the /update endpoint.
     *
     * @return an UpdateInfo object containing the version of the client application and
     *         the URL to download the updated JAR file.
     */
    @GetMapping("/update")
    public UpdateInfo getUpdateInfo() {
        // Return the update information for the client application
        return new UpdateInfo("1.0.0", "http://example-host.com/self-updating-service-client-1.0.0.jar");
    }
}