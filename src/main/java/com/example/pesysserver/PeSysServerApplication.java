package com.example.pesysserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.example.pesysserver.mapper")
public class PeSysServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PeSysServerApplication.class, args);
    }

}