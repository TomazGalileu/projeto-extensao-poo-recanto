package com.ProjetoExtensao.Projeto.servicos;

import com.ProjetoExtensao.Projeto.models.Consulta;
import com.ProjetoExtensao.Projeto.models.EventoSentinela;
import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.repositorios.ConsultaRepositorio;
import com.ProjetoExtensao.Projeto.repositorios.EventoSentinelaRepositorio;
import com.ProjetoExtensao.Projeto.repositorios.PacienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class RelatorioService {

    @Autowired
    private PacienteRepositorio pacienteRepositorio;

    @Autowired
    private ConsultaRepositorio consultaRepositorio;

    @Autowired
    private EventoSentinelaRepositorio eventoSentinelaRepositorio;

    private final DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public String gerarRelatorioPorCpf(String cpfDigitado) {
        Optional<Paciente> pacienteOptional = buscarPacientePorCpf(cpfDigitado);

        if (pacienteOptional.isEmpty()) {
            return "Paciente nao encontrado para o CPF informado.";
        }

        Paciente paciente = pacienteOptional.get();

        List<Consulta> consultas = consultaRepositorio.findAllByPaciente(paciente);
        List<EventoSentinela> eventos = eventoSentinelaRepositorio.findByPacienteOrderByDataEventoDesc(paciente);

        StringBuilder relatorio = new StringBuilder();

        relatorio.append("RELATORIO DA IDOSA\n");
        relatorio.append("====================================\n\n");

        relatorio.append("DADOS PESSOAIS\n");
        relatorio.append("------------------------------------\n");
        relatorio.append("Nome: ").append(paciente.getNomeCompleto()).append("\n");
        relatorio.append("CPF: ").append(paciente.getCpf()).append("\n");
        relatorio.append("Data de nascimento: ").append(formatarData(paciente.getDataNascimento())).append("\n");
        relatorio.append("Idade: ").append(calcularIdade(paciente.getDataNascimento())).append(" anos\n");
        relatorio.append("Nome da mae: ").append(valorOuVazio(paciente.getNomeMae())).append("\n");
        relatorio.append("Cartao SUS: ").append(paciente.getCartaoSUS()).append("\n");
        relatorio.append("Data de entrada: ").append(formatarData(paciente.getDataEntrada())).append("\n");
        relatorio.append("Status: ").append(Boolean.TRUE.equals(paciente.getAtivo()) ? "Ativa" : "Inativa").append("\n\n");

        relatorio.append("CONSULTAS\n");
        relatorio.append("------------------------------------\n");

        if (consultas.isEmpty()) {
            relatorio.append("Nenhuma consulta encontrada.\n");
        } else {
            for (Consulta consulta : consultas) {
                relatorio.append("Data: ").append(formatarData(consulta.getData())).append("\n");
                relatorio.append("Hora: ").append(consulta.getHora()).append("\n");
                relatorio.append("Tipo: ").append(consulta.getTipoConsulta()).append("\n");
                relatorio.append("Motivo: ").append(valorOuVazio(consulta.getMotivoConsulta())).append("\n");
                relatorio.append("Diagnostico: ").append(valorOuVazio(consulta.getDiagnostico())).append("\n");
                relatorio.append("Anotacoes: ").append(valorOuVazio(consulta.getAnotacoesMedico())).append("\n");
                relatorio.append("------------------------------------\n");
            }
        }

        relatorio.append("\nEVENTOS SENTINELAS\n");
        relatorio.append("------------------------------------\n");

        if (eventos.isEmpty()) {
            relatorio.append("Nenhum evento sentinela encontrado.\n");
        } else {
            for (EventoSentinela evento : eventos) {
                relatorio.append("Data: ").append(formatarData(evento.getDataEvento())).append("\n");
                relatorio.append("Evento: ").append(evento.getEventosOcorridos()).append("\n");
                relatorio.append("Descricao: ").append(valorOuVazio(evento.getDescricao())).append("\n");
                relatorio.append("------------------------------------\n");
            }
        }

        return relatorio.toString();
    }

    private Optional<Paciente> buscarPacientePorCpf(String cpfDigitado) {
        String cpfLimpo = limparCpf(cpfDigitado);

        return pacienteRepositorio.findAll()
                .stream()
                .filter(paciente -> limparCpf(paciente.getCpf()).equals(cpfLimpo))
                .findFirst();
    }

    private String limparCpf(String cpf) {
        if (cpf == null) {
            return "";
        }

        return cpf.replaceAll("\\D", "");
    }

    private String formatarData(LocalDate data) {
        if (data == null) {
            return "Nao informado";
        }

        return data.format(formatadorData);
    }

    private int calcularIdade(LocalDate dataNascimento) {
        if (dataNascimento == null) {
            return 0;
        }

        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    private String valorOuVazio(String valor) {
        if (valor == null || valor.isBlank()) {
            return "Nao informado";
        }

        return valor;
    }
}