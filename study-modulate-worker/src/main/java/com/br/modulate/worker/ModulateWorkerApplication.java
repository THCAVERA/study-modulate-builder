package com.br.modulate.worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.br.modulate")
public class ModulateWorkerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ModulateWorkerApplication.class, args);
    }
}
