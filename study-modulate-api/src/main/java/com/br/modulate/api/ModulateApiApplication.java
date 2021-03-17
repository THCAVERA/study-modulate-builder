package com.br.modulate.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableRetry
@EnableAsync
@ComponentScan("com.br.modulate")
public class ModulateApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ModulateApiApplication.class, args);
    }

}
