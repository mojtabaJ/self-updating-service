package io.github.mojtabaj.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    /**
     * Handles HTTP GET requests to the root endpoint ("/").
     *
     * @return a String response with a greeting message including the version of the client app.
     */
    @GetMapping("/")
    public String hello() {
        return "Hello from Client App version 1.0.0!";
    }
}