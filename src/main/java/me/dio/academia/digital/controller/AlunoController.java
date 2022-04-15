package me.dio.academia.digital.controller;

import me.dio.academia.digital.entity.Aluno;
import me.dio.academia.digital.entity.AvaliacaoFisica;
import me.dio.academia.digital.entity.form.AlunoForm;
import me.dio.academia.digital.service.impl.AlunoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

@RestController
@RequestMapping("/api/v1/alunos")
public class AlunoController {

  @Autowired
  private AlunoServiceImpl service;

  @PostMapping
  @ResponseStatus( HttpStatus.CREATED )
  public Aluno create(@Valid @RequestBody AlunoForm form) {
    return service.create(form);
  }

  @GetMapping("/avaliacoes/{id}")
  public List<AvaliacaoFisica> getAllAvaliacaoFisicaId(@PathVariable Long id) {
    return service.getAllAvaliacaoFisicaId(id);
  }

  @GetMapping
  public List<Aluno> getAll(){
    return service.getAll();
  }

  @GetMapping("/nascimento")
  public List<Aluno> getByDataDeNascimento(@RequestParam(value = "dataDeNascimento", required = true)
                                    String dataDeNascimento){
    return service.getAll(dataDeNascimento);
  }
}
