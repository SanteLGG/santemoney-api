package com.santemoneyapi.controller;

import com.santemoneyapi.evento.RecursoCriadoEvent;
import com.santemoneyapi.model.Categoria;
import com.santemoneyapi.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping
    public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
        Categoria categoriaSalva = categoriaRepository.save(categoria);

        publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getCodigo()));

        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
    }

    @GetMapping("/{codigo}") //DESAFIO DE RETORNAR NOTNULL
    public ResponseEntity<Categoria> buscarPeloCodigo(@PathVariable Long codigo) {
        if (categoriaRepository != null) {
            return ResponseEntity.of(categoriaRepository.findById(codigo));
        }
        return ResponseEntity.notFound().build();
    }

}