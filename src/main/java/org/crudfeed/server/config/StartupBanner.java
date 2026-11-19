package org.crudfeed.server.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static java.lang.IO.print;

@Component
public class StartupBanner implements CommandLineRunner {
    @Override
    public void run(String... args) {

        //Type of ASCII art: STANDARD
        print("""
                  ____                _ _____             _   ____                             ____         ___ \s
                 / ___|_ __ _   _  __| |  ___|__  ___  __| | / ___|  ___ _ ____   _____ _ __  |___ \\       / _ \\\s
                | |   | '__| | | |/ _` | |_ / _ \\/ _ \\/ _` | \\___ \\ / _ \\ '__\\ \\ / / _ \\ '__|   __) |     | | | |
                | |___| |  | |_| | (_| |  _|  __/  __/ (_| |  ___) |  __/ |   \\ V /  __/ |     / __/   _  | |_| |
                 \\____|_|   \\__,_|\\__,_|_|  \\___|\\___|\\__,_| |____/ \\___|_|    \\_/ \\___|_|    |_____| (_)  \\___/\s
                
                    """);
    }
}