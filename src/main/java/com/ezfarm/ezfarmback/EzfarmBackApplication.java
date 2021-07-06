package com.ezfarm.ezfarmback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class EzfarmBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(EzfarmBackApplication.class, args);
    }

}
