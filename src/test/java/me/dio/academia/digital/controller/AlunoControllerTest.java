package me.dio.academia.digital.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.dio.academia.digital.entity.Aluno;
import me.dio.academia.digital.entity.AvaliacaoFisica;
import me.dio.academia.digital.entity.form.AlunoForm;
import me.dio.academia.digital.service.impl.AlunoServiceImpl;
import me.dio.academia.digital.util.AlunoUtil;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@TestPropertySource(properties = { "spring.config.location=classpath:application-test.yml" })
@TestMethodOrder(MethodOrderer.DisplayName.class)
@WebMvcTest(AlunoController.class)
public class AlunoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlunoServiceImpl alunoService;

    @Autowired
    private ObjectMapper objectMapper;

    Aluno aluno = AlunoUtil.createFakeAluno();
    AlunoForm alunoForm = AlunoUtil.createFakeAlunoForm();
    List<Aluno> listOfAlunos = Arrays.asList(aluno);

    @Test
    @DisplayName("01 - Dado objeto aluno, Quando criar aluno, Então deve retornar aluno salvo")
    public void givenAlunoForm_whenCreateAluno_thenReturnSavedAluno() throws Exception{
        // given - precondition or setup
        given(alunoService.create(any(AlunoForm.class))).willReturn(aluno);

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/api/v1/alunos")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(alunoForm))
                .contentType(MediaType.APPLICATION_JSON));

        // then - verify the result or output using assert statements
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome",
                        is(aluno.getNome())))
                .andExpect(jsonPath("$.cpf",
                        is(aluno.getCpf())))
                .andExpect(jsonPath("$.bairro",
                        is(aluno.getBairro())))
                .andExpect(jsonPath("$.dataDeNascimento",
                        is(is(LocalDate.of(2010, 10, 01).toString()))));

    }

    @Test
    @DisplayName("02 - Dado id de aluno, Quando buscar todas avaliações, Então deve retornar lista de avaliaçãoes")
    public void givenAlunoId_whenGetAllAvaliacoesFisicas_thenReturnAvaliacoesFisicasList() throws Exception{
        // given - precondition or setup
        List<AvaliacaoFisica> listOfAvaliacoesFisicas = aluno.getAvaliacoes();
        given(alunoService.getAllAvaliacaoFisicaId(aluno.getId())).willReturn(listOfAvaliacoesFisicas);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/v1/alunos/avaliacoes/{id}", aluno.getId()));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfAvaliacoesFisicas.size())));

    }

    @Test
    @DisplayName("03 - Dado lista de alunos, Quando buscar todos os alunos, Então deve retornar lista de alunos")
    public void givenListOfAlunos_whenGetAllAlunos_thenReturnAlunosList() throws Exception{
        // given - precondition or setup
        given(alunoService.getAll()).willReturn(listOfAlunos);

        // when -  action or the behaviour that we are going test

        ResultActions response = mockMvc.perform(get("/api/v1/alunos"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfAlunos.size())));

    }

    @Test
    @DisplayName("04 - Dado data de nascimento e lista de alunos, Quando buscar todos os alunos por data de nascimento, Então deve retornar lista de alunos")
    public void givenDataDeNascimentoAndListOfAlunos_whenGetAllAlunosWithDataDeNascimento_thenReturnAlunosList() throws Exception{
        // given - precondition or setup
        String dataDeNascimento = "01/10/2010";
        given(alunoService.getAll(dataDeNascimento)).willReturn(listOfAlunos);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/v1/alunos/nascimento").param("dataDeNascimento", dataDeNascimento));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfAlunos.size())));

    }
}