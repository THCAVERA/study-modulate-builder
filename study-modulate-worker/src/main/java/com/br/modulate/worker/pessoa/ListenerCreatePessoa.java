package com.br.modulate.worker.pessoa;

import com.br.modulate.common.model.Pessoa;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class ListenerCreatePessoa {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @JmsListener(destination = "${sqs.createPessoa}")
    public void listen(String json) throws Exception{
        try{
        log.info("start listen create-pessoa-queue");
            Pessoa pessoa = objectMapper.readValue(json, Pessoa.class);
            log.info("pessoa  {}",pessoa);
        }catch (IOException | RuntimeException e) {
            final String msg = String.format("Error to process send selfie queue - {%s}", json);
            log.error(msg, e);
            throw e;
        }
    }
}
