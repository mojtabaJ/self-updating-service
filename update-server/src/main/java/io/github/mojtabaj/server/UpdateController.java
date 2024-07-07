package io.github.mojtabaj.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateController {

    @GetMapping("/update")
    public UpdateInfo getUpdateInfo() {
        return new UpdateInfo("1.1.0", "http://yourserver.com/yourapp-1.1.0.jar");
    }

    static class UpdateInfo {
        private String version;
        private String url;

        public UpdateInfo(String version, String url) {
            this.version = version;
            this.url = url;
        }

        public String getVersion() {
            return version;
        }

        public String getUrl() {
            return url;
        }
    }
}