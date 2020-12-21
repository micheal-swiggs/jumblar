package com.jumblar.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class JumblarApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(JumblarApplication.class, args);
    }
}
