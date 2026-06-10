package com.ProjetoExtensao.Projeto.servicos;

import com.ProjetoExtensao.Projeto.models.Consulta;
import com.ProjetoExtensao.Projeto.models.EventoSentinela;
import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.repositorios.ConsultaRepositorio;
import com.ProjetoExtensao.Projeto.repositorios.EventoSentinelaRepositorio;
import com.ProjetoExtensao.Projeto.repositorios.PacienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ProjetoExtensao.Projeto.utils.EventosOcorridos;


import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.time.YearMonth;
import java.time.Month;
import java.util.LinkedHashMap;
import java.util.Map;


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
        List<EventoSentinela> eventos =
                eventoSentinelaRepositorio.findByPacienteOrderByDataEventoDesc(paciente);

        StringBuilder relatorio = new StringBuilder();

        relatorio.append("RELATORIO DA IDOSA\n");
        relatorio.append("====================================\n\n");

        adicionarDadosPessoais(relatorio, paciente);
        adicionarConsultas(relatorio, consultas, "CONSULTAS", "Nenhuma consulta encontrada.");
        adicionarEventosSentinelas(
                relatorio,
                eventos,
                "EVENTOS SENTINELAS",
                "Nenhum evento sentinela encontrado."
        );

        return relatorio.toString();
    }

    public String gerarRelatorioPorCpfEPeriodo(
        String cpfDigitado,
        LocalDate dataInicio,
        LocalDate dataFim
    ) {
        Optional<Paciente> pacienteOptional = buscarPacientePorCpf(cpfDigitado);

        if (pacienteOptional.isEmpty()) {
            return "Paciente nao encontrado para o CPF informado.";
        }

        if (dataInicio == null || dataFim == null) {
            return "Informe a data inicial e a data final.";
        }

        if (dataInicio.isAfter(dataFim)) {
            return "A data inicial nao pode ser maior que a data final.";
        }

        Paciente paciente = pacienteOptional.get();

        List<Consulta> consultas = consultaRepositorio.findAllByPacienteAndDataBetween(
                paciente,
                dataInicio,
                dataFim
        );

        List<EventoSentinela> eventos =
                eventoSentinelaRepositorio.findByPacienteAndDataEventoBetweenOrderByDataEventoDesc(
                        paciente,
                        dataInicio,
                        dataFim
                );

        StringBuilder relatorio = new StringBuilder();

        relatorio.append("RELATORIO DA IDOSA POR PERIODO\n");
        relatorio.append("====================================\n");
        relatorio.append("Periodo: ")
                .append(formatarData(dataInicio))
                .append(" ate ")
                .append(formatarData(dataFim))
                .append("\n\n");

        adicionarDadosPessoais(relatorio, paciente);

        adicionarConsultas(
                relatorio,
                consultas,
                "CONSULTAS NO PERIODO",
                "Nenhuma consulta encontrada no periodo informado."
        );

        adicionarEventosSentinelas(
                relatorio,
                eventos,
                "EVENTOS SENTINELAS NO PERIODO",
                "Nenhum evento sentinela encontrado no periodo informado."
        );

        adicionarResumoPeriodo(relatorio, consultas, eventos);

        return relatorio.toString();
    }

    public String gerarIndicadorEventosSentinelasPorPeriodo(
        LocalDate dataInicio,
        LocalDate dataFim
    ) {
        if (dataInicio == null || dataFim == null) {
            return "Informe a data inicial e a data final.";
        }

        if (dataInicio.isAfter(dataFim)) {
            return "A data inicial nao pode ser maior que a data final.";
        }

        List<Paciente> pacientesAtivas = pacienteRepositorio.findByAtivo(true);

        if (pacientesAtivas.isEmpty()) {
            return "Nao existem pacientes ativas cadastradas.";
        }

        List<EventoSentinela> eventosNoPeriodo =
                eventoSentinelaRepositorio.findByDataEventoBetween(
                    dataInicio,
                    dataFim
                );

        Set<Long> idsPacientesComEvento = new HashSet<>();

        for (EventoSentinela evento : eventosNoPeriodo) {
            Paciente paciente = evento.getPaciente();

            if (paciente != null && Boolean.TRUE.equals(paciente.getAtivo())) {
                idsPacientesComEvento.add(paciente.getId());
            }
        }

        int totalPacientesAtivas = pacientesAtivas.size();
        int totalPacientesComEvento = idsPacientesComEvento.size();

        double percentual =
                (totalPacientesComEvento * 100.0) / totalPacientesAtivas;

        return String.format(
            Locale.forLanguageTag("pt-BR"),
            """
            INDICADOR DE EVENTOS SENTINELAS
            ====================================
            Periodo: %s ate %s

            Total de pacientes ativas: %d
            Pacientes com ao menos um evento sentinela: %d
            Percentual de pacientes com evento sentinela: %.2f%%
            """,
            formatarData(dataInicio),
            formatarData(dataFim),
            totalPacientesAtivas,
            totalPacientesComEvento,
            percentual
        );
    }

    public String gerarIndicadorPorTipoEvento(
        EventosOcorridos tipoEvento,
        LocalDate dataInicio,
        LocalDate dataFim
    ) {
        if (tipoEvento == null) {
            return "Selecione um tipo de evento.";
        }

        if (dataInicio == null || dataFim == null) {
            return "Informe a data inicial e a data final.";
        }

        if (dataInicio.isAfter(dataFim)) {
            return "A data inicial nao pode ser maior que a data final.";
        }

        List<Paciente> pacientesAtivas = pacienteRepositorio.findByAtivo(true);

        if (pacientesAtivas.isEmpty()) {
            return "Nao existem pacientes ativas cadastradas.";
        }

        List<EventoSentinela> eventosDoTipo =
                eventoSentinelaRepositorio.findByEventosOcorridosAndDataEventoBetween(
                    tipoEvento,
                    dataInicio,
                    dataFim
                );

        Set<Long> idsPacientesComEvento = new HashSet<>();

        for (EventoSentinela evento : eventosDoTipo) {
            Paciente paciente = evento.getPaciente();

            if (paciente != null && Boolean.TRUE.equals(paciente.getAtivo())) {
                idsPacientesComEvento.add(paciente.getId());
            }
        }

        int totalPacientesAtivas = pacientesAtivas.size();
        int totalPacientesComEvento = idsPacientesComEvento.size();

        double percentual =
            (totalPacientesComEvento * 100.0) / totalPacientesAtivas;

        return String.format(
            Locale.forLanguageTag("pt-BR"),
            """
            INDICADOR POR TIPO DE EVENTO
            ==================================
            Evento: %s
            Periodo: %s ate %s

            Total de pacientes ativas: %d
            Pacientes com o evento selecionado: %d
            Percentual de pacientes com o evento selecionado: %.2f%%
            """,
            tipoEvento,
            formatarData(dataInicio),
            formatarData(dataFim),
            totalPacientesAtivas,
            totalPacientesComEvento,
            percentual
        );
    }

    public String gerarRelatorioInstitucionalMensal(int ano, int mes) {
        if (mes < 1 || mes > 12) {
            return "Informe um mes valido entre 1 e 12.";
        }

        YearMonth anoMes = YearMonth.of(ano, mes);

        LocalDate dataInicio = anoMes.atDay(1);
        LocalDate dataFim = anoMes.atEndOfMonth();

        List<Paciente> pacientesAtivas = pacienteRepositorio.findByAtivo(true);

        if (pacientesAtivas.isEmpty()) {
            return "Nao existem pacientes ativas cadastradas.";
        }

        int totalPacientesAtivas = pacientesAtivas.size();

        Map<String, EventosOcorridos> indicadores = new LinkedHashMap<>();

        indicadores.put("Taxa de mortalidade", EventosOcorridos.OBITO);
        indicadores.put("Doenca diarreica aguda", EventosOcorridos.DIARREIA);
        indicadores.put("Escabiose", EventosOcorridos.ESCABIOSE);
        indicadores.put("Desidratacao", EventosOcorridos.DESIDRATACAO);
        indicadores.put("Ulcera por pressao", EventosOcorridos.ULCERA_POR_PRESSAO);
        indicadores.put("Desnutricao", EventosOcorridos.DESNUTRICAO);

        StringBuilder relatorio = new StringBuilder();

        relatorio.append("RELATORIO INSTITUCIONAL MENSAL\n");
        relatorio.append("============================================================\n");
        relatorio.append("Periodo: ")
            .append(formatarData(dataInicio))
            .append(" ate ")
            .append(formatarData(dataFim))
            .append("\n");

        relatorio.append("Pacientes ativas consideradas: ")
            .append(totalPacientesAtivas)
            .append("\n");

        relatorio.append("Observacao: calculo baseado nas pacientes atualmente ativas.\n");
        relatorio.append("Ainda nao considera historico mensal de permanencia.\n\n");

        relatorio.append(String.format(
            "%-32s | %-5s | %-10s%n",
            "Indicador",
            "Casos",
            "Taxa"
        ));

        relatorio.append("------------------------------------------------------------\n");

        for (Map.Entry<String, EventosOcorridos> indicador : indicadores.entrySet()) {
            List<EventoSentinela> eventos =
                 eventoSentinelaRepositorio.findByEventosOcorridosAndDataEventoBetween(
                    indicador.getValue(),
                    dataInicio,
                    dataFim
                );

            int quantidadePacientesComEvento =
                contarPacientesAtivasDistintas(eventos);

            double percentual =
                (quantidadePacientesComEvento * 100.0) / totalPacientesAtivas;

            relatorio.append(String.format(
                Locale.forLanguageTag("pt-BR"),
                "%-32s | %-5d | %6.2f%%%n",
                indicador.getKey(),
                quantidadePacientesComEvento,
                percentual
            ));
        }
        return relatorio.toString();
    }

    public String gerarRelatorioInstitucionalAnual(int ano) {
        if (ano < 1900 || ano > 2100) {
            return "Informe um ano valido entre 1900 e 2100.";
        }

        List<Paciente> pacientesAtivas = pacienteRepositorio.findByAtivo(true);

        if (pacientesAtivas.isEmpty()) {
            return "Nao existem pacientes ativas cadastradas.";
        }

        int totalPacientesAtivas = pacientesAtivas.size();

        StringBuilder relatorio = new StringBuilder();

        relatorio.append("CONSOLIDADO ANUAL DE INDICADORES\n");
        relatorio.append("====================================================================================================\n");
        relatorio.append("Ano: ").append(ano).append("\n");
        relatorio.append("Pacientes ativas consideradas: ")
            .append(totalPacientesAtivas)
            .append("\n");
        relatorio.append("Observacao: calculo baseado nas pacientes atualmente ativas.\n");
        relatorio.append("Ainda nao considera historico mensal de permanencia.\n\n");

        relatorio.append(String.format(
            "%-10s | %-11s | %-9s | %-9s | %-12s | %-8s | %-11s%n",
            "Mes",
            "Mortalidade",
            "Diarreia",
            "Escabiose",
            "Desidratacao",
            "Ulcera",
            "Desnutricao"
        ));

        relatorio.append("----------------------------------------------------------------------------------------------------\n");

        for (int mes = 1; mes <= 12; mes++) {
            YearMonth anoMes = YearMonth.of(ano, mes);

            LocalDate dataInicio = anoMes.atDay(1);
            LocalDate dataFim = anoMes.atEndOfMonth();

            double mortalidade = calcularPercentualPorTipoNoPeriodo(
                EventosOcorridos.OBITO,
                dataInicio,
                dataFim,
                totalPacientesAtivas
            );

            double diarreia = calcularPercentualPorTipoNoPeriodo(
                EventosOcorridos.DIARREIA,
                dataInicio,
                dataFim,
                totalPacientesAtivas
            );

            double escabiose = calcularPercentualPorTipoNoPeriodo(
                EventosOcorridos.ESCABIOSE,
                dataInicio,
                dataFim,
                totalPacientesAtivas
            );

            double desidratacao = calcularPercentualPorTipoNoPeriodo(
                EventosOcorridos.DESIDRATACAO,
                dataInicio,
                dataFim,
                totalPacientesAtivas
            );

            double ulcera = calcularPercentualPorTipoNoPeriodo(
                EventosOcorridos.ULCERA_POR_PRESSAO,
                dataInicio,
                dataFim,
                totalPacientesAtivas
            );

            double desnutricao = calcularPercentualPorTipoNoPeriodo(
                EventosOcorridos.DESNUTRICAO,
                dataInicio,
                dataFim,
                totalPacientesAtivas
            );

            relatorio.append(String.format(
                Locale.forLanguageTag("pt-BR"),
                "%-10s | %10.2f%% | %8.2f%% | %8.2f%% | %11.2f%% | %7.2f%% | %10.2f%%%n",
                obterNomeMes(mes),
                mortalidade,
                diarreia,
                escabiose,
                desidratacao,
                ulcera,
                desnutricao
            ));
        }
        return relatorio.toString();
    }

    private double calcularPercentualPorTipoNoPeriodo(
        EventosOcorridos tipoEvento,
        LocalDate dataInicio,
        LocalDate dataFim,
        int totalPacientesAtivas
    ) {
        List<EventoSentinela> eventos =
            eventoSentinelaRepositorio.findByEventosOcorridosAndDataEventoBetween(
                    tipoEvento,
                    dataInicio,
                    dataFim
            );

        int totalPacientesComEvento =
            contarPacientesAtivasDistintas(eventos);

        return (totalPacientesComEvento * 100.0) / totalPacientesAtivas;
    }

    private String obterNomeMes(int mes) {
        return switch (mes) {
            case 1 -> "Janeiro";
            case 2 -> "Fevereiro";
            case 3 -> "Marco";
            case 4 -> "Abril";
            case 5 -> "Maio";
            case 6 -> "Junho";
            case 7 -> "Julho";
            case 8 -> "Agosto";
            case 9 -> "Setembro";
            case 10 -> "Outubro";
            case 11 -> "Novembro";
            case 12 -> "Dezembro";
            default -> "Invalido";
        };
    }

    private int contarPacientesAtivasDistintas(List<EventoSentinela> eventos) {
        Set<Long> idsPacientes = new HashSet<>();

        for (EventoSentinela evento : eventos) {
            Paciente paciente = evento.getPaciente();

            if (paciente != null && Boolean.TRUE.equals(paciente.getAtivo())) {
                idsPacientes.add(paciente.getId());
            }
        }
        return idsPacientes.size();
    }

    private void adicionarDadosPessoais(StringBuilder relatorio, Paciente paciente) {
        relatorio.append("DADOS PESSOAIS\n");
        relatorio.append("------------------------------------\n");
        relatorio.append("Nome: ").append(paciente.getNomeCompleto()).append("\n");
        relatorio.append("CPF: ").append(paciente.getCpf()).append("\n");
        relatorio.append("Data de nascimento: ")
            .append(formatarData(paciente.getDataNascimento()))
            .append("\n");
        relatorio.append("Idade: ")
            .append(calcularIdade(paciente.getDataNascimento()))
            .append(" anos\n");
        relatorio.append("Nome da mae: ")
            .append(valorOuVazio(paciente.getNomeMae()))
            .append("\n");
        relatorio.append("Cartao SUS: ")
            .append(paciente.getCartaoSUS())
            .append("\n");
        relatorio.append("Data de entrada: ")
            .append(formatarData(paciente.getDataEntrada()))
            .append("\n");
        relatorio.append("Status: ")
            .append(Boolean.TRUE.equals(paciente.getAtivo()) ? "Ativa" : "Inativa")
            .append("\n\n");
    }

    private void adicionarConsultas(
        StringBuilder relatorio,
        List<Consulta> consultas,
        String titulo,
        String mensagemSemResultados
    ) {
        relatorio.append(titulo).append("\n");
        relatorio.append("------------------------------------\n");

        if (consultas.isEmpty()) {
            relatorio.append(mensagemSemResultados).append("\n");
            return;
        }

        for (Consulta consulta : consultas) {
            relatorio.append("Data: ")
                .append(formatarData(consulta.getData()))
                .append("\n");
            relatorio.append("Hora: ")
                .append(consulta.getHora())
                .append("\n");
            relatorio.append("Tipo: ")
                .append(consulta.getTipoConsulta())
                .append("\n");
            relatorio.append("Motivo: ")
                .append(valorOuVazio(consulta.getMotivoConsulta()))
                .append("\n");
            relatorio.append("Diagnostico: ")
                .append(valorOuVazio(consulta.getDiagnostico()))
                .append("\n");
            relatorio.append("Anotacoes: ")
                .append(valorOuVazio(consulta.getAnotacoesMedico()))
                .append("\n");
            relatorio.append("------------------------------------\n");
        }
    }

    private void adicionarEventosSentinelas(
        StringBuilder relatorio,
        List<EventoSentinela> eventos,
        String titulo,
        String mensagemSemResultados
    ) {
        relatorio.append("\n").append(titulo).append("\n");
        relatorio.append("------------------------------------\n");

        if (eventos.isEmpty()) {
            relatorio.append(mensagemSemResultados).append("\n");
            return;
        }

        for (EventoSentinela evento : eventos) {
            relatorio.append("Data: ")
                .append(formatarData(evento.getDataEvento()))
                .append("\n");
            relatorio.append("Evento: ")
                .append(evento.getEventosOcorridos())
                .append("\n");
            relatorio.append("Descricao: ")
                .append(valorOuVazio(evento.getDescricao()))
                .append("\n");
            relatorio.append("------------------------------------\n");
        }
    }

    private void adicionarResumoPeriodo(
        StringBuilder relatorio,
        List<Consulta> consultas,
        List<EventoSentinela> eventos
    ) {
        relatorio.append("\nRESUMO DO PERIODO\n");
        relatorio.append("------------------------------------\n");
        relatorio.append("Total de consultas: ")
            .append(consultas.size())
            .append("\n");
        relatorio.append("Total de eventos sentinelas: ")
            .append(eventos.size())
            .append("\n");
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