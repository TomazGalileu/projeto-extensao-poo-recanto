package com.ProjetoExtensao.Projeto.view;

import com.ProjetoExtensao.Projeto.infra.Cores;
import com.ProjetoExtensao.Projeto.infra.DateTimeFormatter;
import com.ProjetoExtensao.Projeto.infra.PanelsFactory;
import com.ProjetoExtensao.Projeto.models.Vacina;
import com.ProjetoExtensao.Projeto.models.Consulta;
import com.ProjetoExtensao.Projeto.models.Exame;
import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.models.Prescricao;
import com.ProjetoExtensao.Projeto.servicos.VacinaService;
import com.ProjetoExtensao.Projeto.servicos.ConsultaService;
import com.ProjetoExtensao.Projeto.servicos.PacienteService;
import com.ProjetoExtensao.Projeto.servicos.ProntuarioMedicoService;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;

@org.springframework.stereotype.Component
@NoArgsConstructor
public class TelaProntuarioPaciente extends JFrame {

    @Autowired
    private PanelsFactory panelsFactory;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private ProntuarioMedicoService prontuarioMedicoService;

    @Autowired
    private VacinaService vacinaService;

    private Long pacienteIdAtual;

    private JLabel lblNome;
    private JLabel lblCpf;
    private JLabel lblDataNascimento;
    private JLabel lblNomeMae;
    private JLabel lblCartaoSus;
    private JLabel lblDataEntrada;
    private JLabel lblStatus;

    private JTextArea areaResumo;

    private DefaultTableModel modeloTabelaConsultas;
    private DefaultTableModel modeloTabelaPrescricoes;
    private DefaultTableModel modeloTabelaExames;
    private DefaultTableModel modeloTabelaVacinas;
    private DefaultListModel<String> modeloInternacoes;

    private final java.time.format.DateTimeFormatter formatadorEntrada =
            java.time.format.DateTimeFormatter
                    .ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

    @PostConstruct
    private void initUI() {
        setTitle("Prontuário da Paciente");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(Cores.COR_FUNDO_CLARO);
        painelPrincipal.add(panelsFactory.getHeaderPanel(), BorderLayout.NORTH);
        painelPrincipal.add(panelsFactory.getFooterPanel(), BorderLayout.SOUTH);

        JScrollPane scrollPrincipal = new JScrollPane(criarPainelCentral());
        scrollPrincipal.setBorder(null);
        scrollPrincipal.getVerticalScrollBar().setUnitIncrement(16);

        painelPrincipal.add(scrollPrincipal, BorderLayout.CENTER);

        setContentPane(painelPrincipal);
    }

    private JPanel criarPainelCentral() {
        JPanel painelCentral = new JPanel();
        painelCentral.setLayout(new BoxLayout(painelCentral, BoxLayout.Y_AXIS));
        painelCentral.setBorder(new EmptyBorder(30, 50, 30, 50));
        painelCentral.setBackground(Cores.COR_FUNDO_CLARO);

        JLabel titulo = new JLabel("Prontuário Médico");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(Cores.COR_LETRA_PAINEL);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        painelCentral.add(titulo);
        painelCentral.add(Box.createVerticalStrut(20));
        painelCentral.add(criarPainelDadosPaciente());
        painelCentral.add(Box.createVerticalStrut(15));
        painelCentral.add(criarPainelResumo());
        painelCentral.add(Box.createVerticalStrut(15));
        painelCentral.add(criarAbasProntuario());

        return painelCentral;
    }

    private JPanel criarPainelDadosPaciente() {
        JPanel painelDados = new JPanel(new GridLayout(4, 2, 20, 10));
        painelDados.setBackground(Color.WHITE);
        painelDados.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(20, 20, 20, 20)
        ));
        painelDados.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        painelDados.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblNome = criarCampoInfo(painelDados, "Nome completo");
        lblCpf = criarCampoInfo(painelDados, "CPF");
        lblDataNascimento = criarCampoInfo(painelDados, "Data de nascimento");
        lblNomeMae = criarCampoInfo(painelDados, "Nome da mãe");
        lblCartaoSus = criarCampoInfo(painelDados, "Cartão SUS");
        lblDataEntrada = criarCampoInfo(painelDados, "Data de entrada");
        lblStatus = criarCampoInfo(painelDados, "Status");

        return painelDados;
    }

    private JLabel criarCampoInfo(JPanel painelDestino, String titulo) {
        JPanel painelCampo = new JPanel(new BorderLayout(5, 5));
        painelCampo.setBackground(Color.WHITE);

        JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 13));
        labelTitulo.setForeground(Cores.COR_RODAPE);

        JLabel labelValor = new JLabel("-");
        labelValor.setFont(new Font("Arial", Font.PLAIN, 14));
        labelValor.setForeground(Cores.COR_LETRA_PAINEL);

        painelCampo.add(labelTitulo, BorderLayout.NORTH);
        painelCampo.add(labelValor, BorderLayout.CENTER);
        painelDestino.add(painelCampo);

        return labelValor;
    }

    private JPanel criarPainelResumo() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createTitledBorder("Resumo do prontuário"));
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        painel.setAlignmentX(Component.LEFT_ALIGNMENT);

        areaResumo = new JTextArea(6, 80);
        areaResumo.setEditable(false);
        areaResumo.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaResumo.setBackground(Color.WHITE);

        painel.add(new JScrollPane(areaResumo), BorderLayout.CENTER);

        return painel;
    }

    private JTabbedPane criarAbasProntuario() {
        JTabbedPane abas = new JTabbedPane();
        abas.setAlignmentX(Component.LEFT_ALIGNMENT);
        abas.setPreferredSize(new Dimension(1000, 450));
        abas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));

        abas.addTab("Consultas", criarPainelHistoricoConsultas());
        abas.addTab("Prescrições", criarPainelPrescricoes());
        abas.addTab("Exames", criarPainelExames());
        abas.addTab("Internações", criarPainelInternacoes());
        abas.addTab("Vacinas", criarPainelVacinas());
        return abas;
    }

    private JPanel criarPainelHistoricoConsultas() {
        JPanel painel = criarPainelTabela();

        String[] colunas = {
                "Data",
                "Hora",
                "Tipo",
                "Profissional",
                "Motivo",
                "Diagnóstico",
                "Anotações"
        };

        modeloTabelaConsultas = criarModeloNaoEditavel(colunas);

        JTable tabela = new JTable(modeloTabelaConsultas);
        tabela.setRowHeight(25);

        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarPainelPrescricoes() {
        JPanel painel = criarPainelTabela();

        String[] colunas = {
                "ID",
                "Medicamento",
                "Dosagem",
                "Frequência",
                "Data inicial",
                "Data final",
                "Status"
        };

        modeloTabelaPrescricoes = criarModeloNaoEditavel(colunas);

        JTable tabela = new JTable(modeloTabelaPrescricoes);
        tabela.setRowHeight(25);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnAdicionar = new JButton("Adicionar prescrição");
        btnAdicionar.addActionListener(e -> adicionarPrescricao());

        JButton btnEncerrar = new JButton("Encerrar prescrição");
        btnEncerrar.addActionListener(
                e -> encerrarPrescricaoSelecionada(tabela)
        );

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnEncerrar);

        painel.add(painelBotoes, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarPainelExames() {
        JPanel painel = criarPainelTabela();

        String[] colunas = {
                "ID",
                "Exame",
                "Data da solicitação",
                "Resultado",
                "Data do resultado"
        };

        modeloTabelaExames = criarModeloNaoEditavel(colunas);

        JTable tabela = new JTable(modeloTabelaExames);
        tabela.setRowHeight(25);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnAdicionar = new JButton("Solicitar exame");
        btnAdicionar.addActionListener(e -> adicionarExame());

        JButton btnResultado = new JButton("Registrar resultado");
        btnResultado.addActionListener(
                e -> registrarResultadoExameSelecionado(tabela)
        );

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnResultado);

        painel.add(painelBotoes, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarPainelInternacoes() {
        JPanel painel = criarPainelTabela();

        modeloInternacoes = new DefaultListModel<>();

        JList<String> lista = new JList<>(modeloInternacoes);

        JButton btnAdicionar = new JButton("Adicionar internação");
        btnAdicionar.addActionListener(e -> adicionarInternacao());

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBotoes.add(btnAdicionar);

        painel.add(painelBotoes, BorderLayout.NORTH);
        painel.add(new JScrollPane(lista), BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarPainelVacinas() {
        JPanel painel = criarPainelTabela();

        String[] colunas = {
                "ID",
                "Vacina",
                "Data de aplicação"
        };

        modeloTabelaVacinas = criarModeloNaoEditavel(colunas);

        JTable tabela = new JTable(modeloTabelaVacinas);
        tabela.setRowHeight(25);

        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarPainelTabela() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBackground(Color.WHITE);
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));

        return painel;
    }

    private DefaultTableModel criarModeloNaoEditavel(String[] colunas) {
        return new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public void carregarProntuario(Long pacienteId) {
        try {
            pacienteIdAtual = pacienteId;

            Paciente paciente =
                    pacienteService.findPacienteById(pacienteId);

            preencherDadosPaciente(paciente);

            preencherHistoricoConsultas(
                    consultaService.findHistoricoConsultasByPaciente(paciente)
            );

            atualizarDadosProntuario();

        } catch (RuntimeException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erro ao carregar prontuário",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void preencherDadosPaciente(Paciente paciente) {
        lblNome.setText(valorOuTraco(paciente.getNomeCompleto()));
        lblCpf.setText(valorOuTraco(paciente.getCpf()));

        lblDataNascimento.setText(
                paciente.getDataNascimento() == null
                        ? "-"
                        : paciente.getDataNascimento()
                        .format(DateTimeFormatter.DATE_TIME_FORMATTER)
        );

        lblNomeMae.setText(valorOuTraco(paciente.getNomeMae()));
        lblCartaoSus.setText(valorOuTraco(paciente.getCartaoSUS()));

        lblDataEntrada.setText(
                paciente.getDataEntrada() == null
                        ? "-"
                        : paciente.getDataEntrada()
                        .format(DateTimeFormatter.DATE_TIME_FORMATTER)
        );

        lblStatus.setText(
                Boolean.TRUE.equals(paciente.getAtivo())
                        ? "Ativo"
                        : "Inativo"
        );
    }

    private void preencherHistoricoConsultas(List<Consulta> consultas) {
        modeloTabelaConsultas.setRowCount(0);

        for (Consulta consulta : consultas) {
            modeloTabelaConsultas.addRow(new Object[]{
                    consulta.getData() == null
                            ? "-"
                            : consulta.getData()
                            .format(DateTimeFormatter.DATE_TIME_FORMATTER),

                    consulta.getHora() == null
                            ? "-"
                            : consulta.getHora().toString(),

                    consulta.getTipoConsulta() == null
                            ? "-"
                            : consulta.getTipoConsulta().toString(),

                    consulta.getResponsavelSaude() == null
                            ? "-"
                            : consulta.getResponsavelSaude()
                            .getNomeCompleto(),

                    valorOuTraco(consulta.getMotivoConsulta()),
                    valorOuTraco(consulta.getDiagnostico()),
                    valorOuTraco(consulta.getAnotacoesMedico())
            });
        }
    }

    private void atualizarDadosProntuario() {
        if (pacienteIdAtual == null) {
            return;
        }

        preencherPrescricoes(
                prontuarioMedicoService.listarPrescricoes(pacienteIdAtual)
        );

        preencherExames(
                prontuarioMedicoService.listarExames(pacienteIdAtual)
        );

        preencherInternacoes(
                prontuarioMedicoService.listarInternacoes(pacienteIdAtual)
        );

        preencherVacinas(
                vacinaService.buscarPorPaciente(pacienteIdAtual)
        );

        areaResumo.setText(
                prontuarioMedicoService.gerarResumoHistorico(pacienteIdAtual)
        );

        areaResumo.setCaretPosition(0);
    }

    private void preencherPrescricoes(List<Prescricao> prescricoes) {
        modeloTabelaPrescricoes.setRowCount(0);

        for (Prescricao prescricao : prescricoes) {
            modeloTabelaPrescricoes.addRow(new Object[]{
                    prescricao.getId(),
                    prescricao.getMedicamento(),
                    prescricao.getDosagem(),
                    prescricao.getFrequencia(),

                    prescricao.getDataInicio() == null
                            ? "-"
                            : prescricao.getDataInicio()
                            .format(DateTimeFormatter.DATE_TIME_FORMATTER),

                    prescricao.getDataFim() == null
                            ? "-"
                            : prescricao.getDataFim()
                            .format(DateTimeFormatter.DATE_TIME_FORMATTER),

                    Boolean.TRUE.equals(prescricao.getAtiva())
                            ? "Ativa"
                            : "Encerrada"
            });
        }
    }

    private void preencherExames(List<Exame> exames) {
        modeloTabelaExames.setRowCount(0);

        for (Exame exame : exames) {
            modeloTabelaExames.addRow(new Object[]{
                    exame.getId(),
                    exame.getNome(),

                    exame.getDataSolicitacao() == null
                            ? "-"
                            : exame.getDataSolicitacao()
                            .format(DateTimeFormatter.DATE_TIME_FORMATTER),

                    valorOuTraco(exame.getResultado()),

                    exame.getDataResultado() == null
                            ? "-"
                            : exame.getDataResultado()
                            .format(DateTimeFormatter.DATE_TIME_FORMATTER)
            });
        }
    }

    private void preencherInternacoes(List<String> internacoes) {
        modeloInternacoes.clear();

        for (String internacao : internacoes) {
            modeloInternacoes.addElement(internacao);
        }
    }

    private void preencherVacinas(List<Vacina> vacinas) {
        modeloTabelaVacinas.setRowCount(0);

        for (Vacina vacina : vacinas) {
            modeloTabelaVacinas.addRow(new Object[]{
                    vacina.getId(),
                    vacina.getNome(),

                    vacina.getDataAplicacao() == null
                            ? "-"
                            : vacina.getDataAplicacao()
                            .format(DateTimeFormatter.DATE_TIME_FORMATTER)
            });
        }
    }

    private void adicionarPrescricao() {
        if (pacienteIdAtual == null) {
            return;
        }

        JTextField medicamento = new JTextField();
        JTextField dosagem = new JTextField();
        JTextField frequencia = new JTextField();
        JTextField dataInicio = new JTextField();
        JTextField dataFim = new JTextField();

        Object[] campos = {
                "Medicamento:", medicamento,
                "Dosagem:", dosagem,
                "Frequência:", frequencia,
                "Data inicial (dd/MM/yyyy):", dataInicio,
                "Data final opcional (dd/MM/yyyy):", dataFim
        };

        int resultado = JOptionPane.showConfirmDialog(
                this,
                campos,
                "Adicionar prescrição",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (resultado != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            validarCampoObrigatorio(medicamento.getText(), "Medicamento");
            validarCampoObrigatorio(dosagem.getText(), "Dosagem");
            validarCampoObrigatorio(frequencia.getText(), "Frequência");

            LocalDate inicio = converterData(dataInicio.getText());
            LocalDate fim = converterDataOpcional(dataFim.getText());

            if (fim != null && fim.isBefore(inicio)) {
                throw new RuntimeException(
                        "A data final não pode ser anterior à data inicial."
                );
            }

            prontuarioMedicoService.adicionarPrescricao(
                    pacienteIdAtual,
                    medicamento.getText().trim(),
                    dosagem.getText().trim(),
                    frequencia.getText().trim(),
                    inicio,
                    fim
            );

            atualizarDadosProntuario();

        } catch (RuntimeException exception) {
            mostrarErro(exception.getMessage());
        }
    }

    private void encerrarPrescricaoSelecionada(JTable tabela) {
        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            mostrarErro("Selecione uma prescrição.");
            return;
        }

        String data = JOptionPane.showInputDialog(
                this,
                "Data de encerramento (dd/MM/yyyy):"
        );

        if (data == null) {
            return;
        }

        try {
            Long prescricaoId = Long.parseLong(
                    modeloTabelaPrescricoes
                            .getValueAt(linha, 0)
                            .toString()
            );

            prontuarioMedicoService.encerrarPrescricao(
                    prescricaoId,
                    converterData(data)
            );

            atualizarDadosProntuario();

        } catch (RuntimeException exception) {
            mostrarErro(exception.getMessage());
        }
    }

    private void adicionarExame() {
        if (pacienteIdAtual == null) {
            return;
        }

        JTextField nome = new JTextField();
        JTextField dataSolicitacao = new JTextField();

        Object[] campos = {
                "Nome do exame:", nome,
                "Data da solicitação (dd/MM/yyyy):", dataSolicitacao
        };

        int resultado = JOptionPane.showConfirmDialog(
                this,
                campos,
                "Solicitar exame",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (resultado != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            validarCampoObrigatorio(nome.getText(), "Nome do exame");

            prontuarioMedicoService.adicionarExame(
                    pacienteIdAtual,
                    nome.getText().trim(),
                    converterData(dataSolicitacao.getText())
            );

            atualizarDadosProntuario();

        } catch (RuntimeException exception) {
            mostrarErro(exception.getMessage());
        }
    }

    private void registrarResultadoExameSelecionado(JTable tabela) {
        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            mostrarErro("Selecione um exame.");
            return;
        }

        JTextArea resultadoExame = new JTextArea(5, 30);
        JTextField dataResultado = new JTextField();

        Object[] campos = {
                "Resultado:", new JScrollPane(resultadoExame),
                "Data do resultado (dd/MM/yyyy):", dataResultado
        };

        int resultado = JOptionPane.showConfirmDialog(
                this,
                campos,
                "Registrar resultado do exame",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (resultado != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            validarCampoObrigatorio(
                    resultadoExame.getText(),
                    "Resultado"
            );

            Long exameId = Long.parseLong(
                    modeloTabelaExames
                            .getValueAt(linha, 0)
                            .toString()
            );

            prontuarioMedicoService.registrarResultadoExame(
                    exameId,
                    resultadoExame.getText().trim(),
                    converterData(dataResultado.getText())
            );

            atualizarDadosProntuario();

        } catch (RuntimeException exception) {
            mostrarErro(exception.getMessage());
        }
    }

    private void adicionarInternacao() {
        if (pacienteIdAtual == null) {
            return;
        }

        JTextArea descricao = new JTextArea(5, 30);

        int resultado = JOptionPane.showConfirmDialog(
                this,
                new JScrollPane(descricao),
                "Descrição da internação",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (resultado != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            validarCampoObrigatorio(
                    descricao.getText(),
                    "Descrição da internação"
            );

            prontuarioMedicoService.adicionarInternacao(
                    pacienteIdAtual,
                    descricao.getText().trim()
            );

            atualizarDadosProntuario();

        } catch (RuntimeException exception) {
            mostrarErro(exception.getMessage());
        }
    }

    private LocalDate converterData(String texto) {
        try {
            return LocalDate.parse(texto.trim(), formatadorEntrada);
        } catch (
                DateTimeParseException
                | NullPointerException exception
        ) {
            throw new RuntimeException(
                    "Informe uma data válida no formato dd/MM/yyyy."
            );
        }
    }

    private LocalDate converterDataOpcional(String texto) {
        if (texto == null || texto.isBlank()) {
            return null;
        }

        return converterData(texto);
    }

    private void validarCampoObrigatorio(
            String valor,
            String nomeCampo
    ) {
        if (valor == null || valor.isBlank()) {
            throw new RuntimeException(
                    nomeCampo + " é obrigatório."
            );
        }
    }

    private void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(
                this,
                mensagem,
                "Erro",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private String valorOuTraco(String valor) {
        return valor == null || valor.isBlank()
                ? "-"
                : valor;
    }
}