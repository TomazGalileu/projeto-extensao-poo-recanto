package com.ProjetoExtensao.Projeto.servicos;

import com.ProjetoExtensao.Projeto.view.*;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class NavigationService {

    @Lazy
    @Autowired
    private TelaLogin telaLogin;

    @Lazy
    @Autowired
    private TelaGeral telaGeral;

    @Lazy
    @Autowired
    private TelaPacientes telaPacientes;

    @Lazy
    @Autowired
    private TelaCadastroPacientes telaCadastroPacientes;

    @Lazy
    @Autowired
    private TelaConsultas consulta;

    @Lazy
    @Autowired
    private TelaAgendamentoConsulta telaAgendamentoConsulta;

    @Lazy
    @Autowired
    private TelaEventosSentinelas telaEventosSentinelas;

    @Lazy
    @Autowired
    private TelaRelatorios telaRelatorios;

    @Lazy
    @Autowired
    private TelaVacinas telaVacinas;

    @Lazy
    @Autowired
    private TelaIndicadores telaIndicadores;

    @Lazy
    @Autowired
    private TelaCadastroVacina telaCadastroVacina;

    @Lazy
    @Autowired
    private TelaRelatorioIndividual telaRelatorioIndividual;

    @Lazy
    @Autowired
    private TelaProntuarioPaciente telaProntuarioPaciente;

    public void abrirTelaLogin() {
        telaLogin.setVisible(true);
    }

    public void abrirTelaGeral() {
        telaGeral.setVisible(true);
    }

    public void abrirTelaPacientes() {
        telaPacientes.setVisible(true);
    }

    public void abrirTelaCadastroPacientes() {
        telaCadastroPacientes.limparCamposAoAbrir();
        telaCadastroPacientes.setVisible(true);
    }

    public void abrirTelaConsultas() {
        consulta.setVisible(true);
    }

    public void abrirTelaAgendamentoConsultas() {
        telaAgendamentoConsulta.setVisible(true);
    }

    public void abrirTelaEdicaoPaciente(Long pacienteId) {
        telaCadastroPacientes.carregarPacienteParaEdicao(pacienteId);
        telaCadastroPacientes.setVisible(true);
    }

    public void abrirTelaEventosSentinelas() {
        telaEventosSentinelas.limparCampos();
        telaEventosSentinelas.setVisible(true);
    }

    public void abrirTelaRelatorios() {
        telaRelatorios.setVisible(true);
    }

    public void abrirTelaVacinas() {
        telaVacinas.setVisible(true);
    }

    public void abrirTelaIndicadores() {
        telaIndicadores.setVisible(true);
    }

    public void abrirTelaCadastroVacina() {
        telaCadastroVacina.limparCampos();
        telaCadastroVacina.setVisible(true);
    }

    public void abrirTelaRelatorioIndividual() {
        telaRelatorioIndividual.limparCampos();
        telaRelatorioIndividual.setVisible(true);
    }

    public void abrirTelaProntuarioPaciente(Long pacienteId) {
        telaProntuarioPaciente.carregarProntuario(pacienteId);
        telaProntuarioPaciente.setVisible(true);
    }
}