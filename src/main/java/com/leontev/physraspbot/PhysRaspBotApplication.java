package com.leontev.physraspbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

// @EnableKafka
@SpringBootApplication
public class PhysRaspBotApplication {
    public static void main(String[] args) {SpringApplication.run(PhysRaspBotApplication.class, args);}}
