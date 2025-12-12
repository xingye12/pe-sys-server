package com.example.pesysserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.pesysserver.mapper")
public class PeSysServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PeSysServerApplication.class, args);
    }

}