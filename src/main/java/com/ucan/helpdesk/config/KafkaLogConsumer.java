package com.ucan.helpdesk.config;

import com.ucan.helpdesk.model.Log;
import com.ucan.helpdesk.repository.LogRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaLogConsumer {
    LogRepository logRepository;

    @KafkaListener(topics = "ticket-criado", groupId = "helpdesk-group")
    public void processLog(String mensagem) {
        Log log = new Log();
        log.setMensagem(mensagem);
        logRepository.save(log);
    }
}