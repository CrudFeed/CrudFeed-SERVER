package org.crudfeed.server.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static java.lang.IO.println;

@Component
public class StartupBanner implements CommandLineRunner {

    private static final String VERSION = "2.0";
    private static final String WEB_URL = "https://crudfeed.github.io/";
    private static final String RELEASE_URL = "https://github.com/CrudFeed/CrudFeed-SERVER/releases";

    @Override
    public void run(String... args) {


        //Type of ASCII art: STANDARD
        String asciiArt = """
                  ____                _ _____             _   ____                             ____         ___
                 / ___|_ __ _   _  __| |  ___|__  ___  __| | / ___|  ___ _ ____   _____ _ __  |___ \\       / _ \\
                | |   | '__| | | |/ _` | |_ / _ \\/ _ \\/ _` | \\___ \\ / _ \\ '__\\ \\ / / _ \\ '__|   __) |     | | | |
                | |___| |  | |_| | (_| |  _|  __/  __/ (_| |  ___) |  __/ |   \\ V /  __/ |     / __/   _  | |_| |
                 \\____|_|   \\__,_|\\__,_|_|  \\___|\\___|\\__,_| |____/ \\___|_|    \\_/ \\___|_|    |_____| (_)  \\___/
                """;

        println(asciiArt);
        println("Version:  " + VERSION);
        println("Website:  " + WEB_URL);
        println("Release:  " + RELEASE_URL);
        println();
    }
}