package com.br.modulate.api.controller;

import com.br.modulate.common.config.QueueProperties;
import com.br.modulate.common.model.Pessoa;
import com.br.modulate.common.service.AmazonSqsService;
import com.br.modulate.common.service.PessoaService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pessoa")
@RequiredArgsConstructor
@Slf4j
public class PessoaController {

    private final AmazonSqsService amazonSqsService;

   private final PessoaService pessoaService;

   private final QueueProperties queueProperties;


    @PostMapping()
    public void getPessoa(){
        try{
            Pessoa pessoa = pessoaService.getPessoa();
            amazonSqsService.sendMessage(queueProperties.getCreatePessoa(),pessoa);
            log.info("sending to create-pessoa-queue - {}",pessoa );
        }catch (Exception e){
            log.error("error to send pessoa to send-pessoa-queue");
        }
    }
}
