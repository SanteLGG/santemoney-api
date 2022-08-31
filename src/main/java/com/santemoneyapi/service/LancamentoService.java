package com.santemoneyapi.service;

import com.santemoneyapi.model.Lancamento;
import com.santemoneyapi.model.Pessoa;
import com.santemoneyapi.repository.LancamentoRepository;
import com.santemoneyapi.repository.PessoaRepository;
import com.santemoneyapi.service.exception.PessoaInexistenteOuInativaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LancamentoService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private LancamentoRepository lancamentoRepository;
    public Lancamento salvar (Lancamento lancamento){
        Optional<Pessoa> pessoa = pessoaRepository.findById(lancamento.getPessoa().getCodigo());
        if (pessoa.isEmpty() || pessoa.get().isInativo() ){
            throw new PessoaInexistenteOuInativaException();
        }

        return lancamentoRepository.save(lancamento);
    }

}
