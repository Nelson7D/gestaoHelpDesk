package com.ucan.helpdesk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaLogProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendLog(String topico, String mensagem) {
        kafkaTemplate.send(topico, mensagem);
    }
}
