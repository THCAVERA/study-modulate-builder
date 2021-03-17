package com.br.modulate.common.service;

import com.br.modulate.common.model.Pessoa;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class PessoaService {


    public Pessoa getPessoa(){
        return Pessoa.builder()
                .nome("Sostenes")
                .build();
    }
}
