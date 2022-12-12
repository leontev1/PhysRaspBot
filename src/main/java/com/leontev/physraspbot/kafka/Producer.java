package com.leontev.physraspbot.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/*@Service
public class Producer {

    private final String TOPIC = "users";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String username) {
        kafkaTemplate.send(TOPIC, username);
    }
}*/
