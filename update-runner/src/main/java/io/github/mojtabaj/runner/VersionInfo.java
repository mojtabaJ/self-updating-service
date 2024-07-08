package io.github.mojtabaj.runner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VersionInfo {
    private String version;
    private String jar;
    private String url;

    public void update(VersionInfo v) {
        this.version = v.getVersion();
        this.jar = v.getJar();
        this.url = v.getUrl();
    }
}