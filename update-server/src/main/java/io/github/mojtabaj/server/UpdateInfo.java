package io.github.mojtabaj.server;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UpdateInfo is a simple POJO (Plain Old Java Object) that holds information
 * about the client application update, such as version and URL.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateInfo {
    // The version of the client application
    private String version;
    // The URL to download the updated JAR file
    private String url;
}