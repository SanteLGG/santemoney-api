package com.santemoneyapi.controller;

import com.santemoneyapi.repository.filter.LancamentoFilter;
import com.santemoneyapi.evento.RecursoCriadoEvent;
import com.santemoneyapi.exceptionhandler.SantemoneyExceptionHandler;
import com.santemoneyapi.model.Lancamento;
import com.santemoneyapi.repository.LancamentoRepository;
import com.santemoneyapi.service.LancamentoService;
import com.santemoneyapi.service.exception.PessoaInexistenteOuInativaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoController {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private LancamentoService lancamentoService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping
    public List<Lancamento> pesquisar(LancamentoFilter lancamentoFilter){
        return lancamentoRepository.filtrar(lancamentoFilter);
    }

    @PostMapping//colocar no banco de dados novos lancamentos
    public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response){
        Lancamento lancamentoSalvo = lancamentoService.salvar(lancamento);

        publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));

        return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
    }

    @GetMapping("/{codigo}") //buscar no banco de dados os lancamentos
    public ResponseEntity<Lancamento> buscarPeloCodigo(@PathVariable Long codigo) {
        if (lancamentoRepository != null) {
            return ResponseEntity.of(lancamentoRepository.findById(codigo));
        }
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({PessoaInexistenteOuInativaException.class})
    public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex){

        String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
        String mernsagemDesenvolvedor = ex.toString();
        List<SantemoneyExceptionHandler.Erro> erros = Arrays.asList(new SantemoneyExceptionHandler.Erro(mensagemUsuario, mernsagemDesenvolvedor));

        return ResponseEntity.badRequest().body(erros);
    }
}
