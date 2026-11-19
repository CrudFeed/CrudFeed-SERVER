package org.crudfeed.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.crudfeed")
public class CrudFeedServer {

    public static void main(String[] args) {
        SpringApplication.run(CrudFeedServer.class, args);
    }
}