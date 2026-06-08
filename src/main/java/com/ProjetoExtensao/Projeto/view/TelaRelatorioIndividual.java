package com.ProjetoExtensao.Projeto.view;

import com.ProjetoExtensao.Projeto.infra.Cores;
import com.ProjetoExtensao.Projeto.models.EventoSentinela;
import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.models.Vacina;
import com.ProjetoExtensao.Projeto.servicos.EventoSentinelaService;
import com.ProjetoExtensao.Projeto.servicos.PacienteService;
import com.ProjetoExtensao.Projeto.servicos.RelatorioPdfService;
import com.ProjetoExtensao.Projeto.servicos.VacinaService;
import com.ProjetoExtensao.Projeto.utils.CPFUtils;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.io.File;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Component
@NoArgsConstructor
public class TelaRelatorioIndividual extends JFrame {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private VacinaService vacinaService;

    @Autowired
    private EventoSentinelaService eventoSentinelaService;

    @Autowired
    private RelatorioPdfService relatorioPdfService;

    private JFormattedTextField txtCpf;
    private JFormattedTextField txtDataInicial;
    private JFormattedTextField txtDataFinal;

    private JLabel lblNome;
    private JLabel lblDataNascimento;
    private JLabel lblCartaoSus;
    private JLabel lblDataEntrada;

    private DefaultTableModel modeloVacinas;
    private DefaultTableModel modeloEventos;

    private Paciente pacienteAtual;

    private LocalDate dataInicialAtual;
    private LocalDate dataFinalAtual;

    private List<Vacina> vacinasAtuais;
    private List<EventoSentinela> eventosAtuais;

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @PostConstruct
    public void initUI() {
        setTitle("Relatório Individual por Período");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel painelPrincipal =
                new JPanel(new BorderLayout(0, 20));

        painelPrincipal.setBackground(
                Cores.COR_FUNDO_CLARO
        );

        painelPrincipal.setBorder(
                new EmptyBorder(20, 30, 20, 30)
        );

        JLabel titulo =
                new JLabel("Relatório Individual por Período");

        titulo.setFont(
                new Font("Arial", Font.BOLD, 30)
        );

        titulo.setForeground(
                Cores.COR_LETRA_PAINEL
        );

        painelPrincipal.add(
                titulo,
                BorderLayout.NORTH
        );

        JPanel painelConteudo = new JPanel();

        painelConteudo.setLayout(
                new BoxLayout(
                        painelConteudo,
                        BoxLayout.Y_AXIS
                )
        );

        painelConteudo.setBackground(
                Cores.COR_FUNDO_CLARO
        );

        painelConteudo.add(
                criarPainelPesquisa()
        );

        painelConteudo.add(
                Box.createRigidArea(
                        new Dimension(0, 15)
                )
        );

        painelConteudo.add(
                criarPainelDadosPaciente()
        );

        painelConteudo.add(
                Box.createRigidArea(
                        new Dimension(0, 15)
                )
        );

        painelConteudo.add(
                criarPainelTabelas()
        );

        JScrollPane scrollPrincipal =
                new JScrollPane(painelConteudo);

        scrollPrincipal.setBorder(null);

        scrollPrincipal
                .getVerticalScrollBar()
                .setUnitIncrement(16);

        painelPrincipal.add(
                scrollPrincipal,
                BorderLayout.CENTER
        );

        add(
                painelPrincipal,
                BorderLayout.CENTER
        );
    }

    private JPanel criarPainelPesquisa() {
        JPanel painel =
                new JPanel(new GridBagLayout());

        painel.setBackground(Color.WHITE);

        painel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 220, 220)
                        ),
                        new EmptyBorder(
                                15,
                                15,
                                15,
                                15
                        )
                )
        );

        painel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        150
                )
        );

        painel.setAlignmentX(
                JComponent.LEFT_ALIGNMENT
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets = new Insets(
                5,
                5,
                5,
                5
        );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;

        painel.add(
                new JLabel("CPF da paciente:"),
                gbc
        );

        gbc.gridx = 1;

        txtCpf =
                criarCampoFormatado(
                        "###.###.###-##"
                );

        painel.add(txtCpf, gbc);

        gbc.gridx = 2;

        painel.add(
                new JLabel("Data inicial:"),
                gbc
        );

        gbc.gridx = 3;

        txtDataInicial =
                criarCampoFormatado(
                        "##/##/####"
                );

        painel.add(txtDataInicial, gbc);

        gbc.gridx = 4;

        painel.add(
                new JLabel("Data final:"),
                gbc
        );

        gbc.gridx = 5;

        txtDataFinal =
                criarCampoFormatado(
                        "##/##/####"
                );

        painel.add(txtDataFinal, gbc);

        JButton btnGerar =
                new JButton("Gerar Relatório");

        btnGerar.setBackground(
                Cores.COR_RODAPE
        );

        btnGerar.setForeground(Color.WHITE);
        btnGerar.setFocusPainted(false);

        gbc.gridx = 6;
        painel.add(btnGerar, gbc);

        btnGerar.addActionListener(
                e -> gerarRelatorio()
        );

        JButton btnExportarPdf =
                new JButton("Exportar PDF");

        btnExportarPdf.setBackground(
                Cores.COR_RODAPE
        );

        btnExportarPdf.setForeground(
                Color.WHITE
        );

        btnExportarPdf.setFocusPainted(false);

        gbc.gridx = 7;
        painel.add(btnExportarPdf, gbc);

        btnExportarPdf.addActionListener(
                e -> exportarPdf()
        );

        return painel;
    }

    private JPanel criarPainelDadosPaciente() {
        JPanel painel =
                new JPanel(
                        new GridLayout(
                                2,
                                2,
                                15,
                                10
                        )
                );

        painel.setBackground(Color.WHITE);

        painel.setBorder(
                BorderFactory.createTitledBorder(
                        "Dados da Paciente"
                )
        );

        painel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        130
                )
        );

        painel.setAlignmentX(
                JComponent.LEFT_ALIGNMENT
        );

        lblNome =
                new JLabel("Nome: --");

        lblDataNascimento =
                new JLabel(
                        "Data de nascimento: --"
                );

        lblCartaoSus =
                new JLabel("Cartão SUS: --");

        lblDataEntrada =
                new JLabel(
                        "Data de entrada: --"
                );

        painel.add(lblNome);
        painel.add(lblDataNascimento);
        painel.add(lblCartaoSus);
        painel.add(lblDataEntrada);

        return painel;
    }

    private JPanel criarPainelTabelas() {
        JPanel painel =
                new JPanel(
                        new GridLayout(
                                2,
                                1,
                                0,
                                20
                        )
                );

        painel.setBackground(
                Cores.COR_FUNDO_CLARO
        );

        painel.setAlignmentX(
                JComponent.LEFT_ALIGNMENT
        );

        String[] colunasVacinas = {
                "ID",
                "Vacina",
                "Data de Aplicação"
        };

        modeloVacinas =
                new DefaultTableModel(
                        colunasVacinas,
                        0
                ) {
                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        JTable tabelaVacinas =
                new JTable(modeloVacinas);

        tabelaVacinas.setRowHeight(28);

        JScrollPane scrollVacinas =
                new JScrollPane(tabelaVacinas);

        scrollVacinas.setBorder(
                BorderFactory.createTitledBorder(
                        "Vacinas no Período"
                )
        );

        String[] colunasEventos = {
                "ID",
                "Tipo",
                "Descrição",
                "Data"
        };

        modeloEventos =
                new DefaultTableModel(
                        colunasEventos,
                        0
                ) {
                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        JTable tabelaEventos =
                new JTable(modeloEventos);

        tabelaEventos.setRowHeight(28);

        JScrollPane scrollEventos =
                new JScrollPane(tabelaEventos);

        scrollEventos.setBorder(
                BorderFactory.createTitledBorder(
                        "Eventos Sentinelas no Período"
                )
        );

        painel.add(scrollVacinas);
        painel.add(scrollEventos);

        painel.setPreferredSize(
                new Dimension(900, 450)
        );

        return painel;
    }

    private JFormattedTextField criarCampoFormatado(
            String mascara
    ) {
        try {
            MaskFormatter formatterMascara =
                    new MaskFormatter(mascara);

            formatterMascara.setPlaceholderCharacter(
                    '_'
            );

            JFormattedTextField campo =
                    new JFormattedTextField(
                            formatterMascara
                    );

            campo.setColumns(10);

            return campo;

        } catch (ParseException ex) {
            throw new RuntimeException(
                    "Erro ao criar campo formatado.",
                    ex
            );
        }
    }

    private void gerarRelatorio() {
        String cpf =
                CPFUtils.limparCPF(
                        txtCpf.getText()
                );

        if (!CPFUtils.validarTamanhoCPF(cpf)) {
            mostrarAviso(
                    "Digite um CPF válido com 11 dígitos."
            );
            return;
        }

        try {
            LocalDate dataInicial =
                    converterData(
                            txtDataInicial.getText()
                    );

            LocalDate dataFinal =
                    converterData(
                            txtDataFinal.getText()
                    );

            if (dataFinal.isBefore(dataInicial)) {
                mostrarAviso(
                        "A data final não pode ser anterior à data inicial."
                );
                return;
            }

            pacienteAtual =
                    pacienteService
                            .findPacienteByCpf(cpf);

            dataInicialAtual = dataInicial;
            dataFinalAtual = dataFinal;

            preencherDadosPaciente();

            carregarVacinas(
                    dataInicial,
                    dataFinal
            );

            carregarEventos(
                    dataInicial,
                    dataFinal
            );

        } catch (DateTimeParseException ex) {
            mostrarAviso(
                    "Informe as datas no formato dd/MM/yyyy."
            );

        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao gerar relatório:\n"
                            + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private LocalDate converterData(
            String texto
    ) {
        if (
                texto == null
                        || texto.contains("_")
        ) {
            throw new DateTimeParseException(
                    "Data incompleta",
                    texto == null ? "" : texto,
                    0
            );
        }

        return LocalDate.parse(
                texto,
                formatter
        );
    }

    private void preencherDadosPaciente() {
        lblNome.setText(
                "Nome: "
                        + pacienteAtual
                        .getNomeCompleto()
        );

        lblDataNascimento.setText(
                "Data de nascimento: "
                        + pacienteAtual
                        .getDataNascimento()
                        .format(formatter)
        );

        lblCartaoSus.setText(
                "Cartão SUS: "
                        + pacienteAtual
                        .getCartaoSUS()
        );

        lblDataEntrada.setText(
                "Data de entrada: "
                        + pacienteAtual
                        .getDataEntrada()
                        .format(formatter)
        );
    }

    private void carregarVacinas(
            LocalDate dataInicial,
            LocalDate dataFinal
    ) {
        modeloVacinas.setRowCount(0);

        vacinasAtuais =
                vacinaService
                        .buscarPorPacienteEPeriodo(
                                pacienteAtual,
                                dataInicial,
                                dataFinal
                        );

        for (Vacina vacina : vacinasAtuais) {
            modeloVacinas.addRow(
                    new Object[]{
                            vacina.getId(),
                            vacina.getNome(),
                            vacina
                                    .getDataAplicacao()
                                    .format(formatter)
                    }
            );
        }
    }

    private void carregarEventos(
            LocalDate dataInicial,
            LocalDate dataFinal
    ) {
        modeloEventos.setRowCount(0);

        eventosAtuais =
                eventoSentinelaService
                        .buscarPorPacienteEPeriodo(
                                pacienteAtual,
                                dataInicial,
                                dataFinal
                        );

        for (
                EventoSentinela evento
                : eventosAtuais
        ) {
            modeloEventos.addRow(
                    new Object[]{
                            evento.getId(),
                            formatarEvento(
                                    evento
                                            .getEventosOcorridos()
                                            .name()
                            ),
                            evento.getDescricao(),
                            evento
                                    .getDataEvento()
                                    .format(formatter)
                    }
            );
        }
    }

    private void exportarPdf() {
        if (
                pacienteAtual == null
                        || dataInicialAtual == null
                        || dataFinalAtual == null
                        || vacinasAtuais == null
                        || eventosAtuais == null
        ) {
            mostrarAviso(
                    "Primeiro gere o relatório antes de exportar."
            );
            return;
        }

        JFileChooser seletorArquivo =
                new JFileChooser();

        String nomePaciente =
                pacienteAtual
                        .getNomeCompleto()
                        .replaceAll(
                                "[^a-zA-ZÀ-ÿ0-9]",
                                "_"
                        );

        seletorArquivo.setSelectedFile(
                new File(
                        "relatorio_"
                                + nomePaciente
                                + "_"
                                + LocalDate.now()
                                + ".pdf"
                )
        );

        int resultado =
                seletorArquivo
                        .showSaveDialog(this);

        if (
                resultado
                        != JFileChooser.APPROVE_OPTION
        ) {
            return;
        }

        File arquivo =
                seletorArquivo.getSelectedFile();

        if (
                !arquivo
                        .getName()
                        .toLowerCase()
                        .endsWith(".pdf")
        ) {
            arquivo =
                    new File(
                            arquivo.getAbsolutePath()
                                    + ".pdf"
                    );
        }

        try {
            relatorioPdfService
                    .gerarRelatorioIndividual(
                            arquivo,
                            pacienteAtual,
                            dataInicialAtual,
                            dataFinalAtual,
                            vacinasAtuais,
                            eventosAtuais
                    );

            JOptionPane.showMessageDialog(
                    this,
                    "PDF gerado com sucesso em:\n"
                            + arquivo
                            .getAbsolutePath(),
                    "PDF gerado",
                    JOptionPane.INFORMATION_MESSAGE
            );

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(
                        arquivo
                );
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao exportar o PDF:\n"
                            + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private String formatarEvento(
            String nomeEnum
    ) {
        String texto =
                nomeEnum
                        .replace("_", " ")
                        .toLowerCase();

        return texto
                .substring(0, 1)
                .toUpperCase()
                + texto.substring(1);
    }

    private void mostrarAviso(
            String mensagem
    ) {
        JOptionPane.showMessageDialog(
                this,
                mensagem,
                "Aviso",
                JOptionPane.WARNING_MESSAGE
        );
    }

    public void limparCampos() {
        if (txtCpf != null) {
            txtCpf.setText("");
            txtDataInicial.setText("");
            txtDataFinal.setText("");
        }

        pacienteAtual = null;
        dataInicialAtual = null;
        dataFinalAtual = null;
        vacinasAtuais = null;
        eventosAtuais = null;

        if (modeloVacinas != null) {
            modeloVacinas.setRowCount(0);
        }

        if (modeloEventos != null) {
            modeloEventos.setRowCount(0);
        }

        if (lblNome != null) {
            lblNome.setText("Nome: --");

            lblDataNascimento.setText(
                    "Data de nascimento: --"
            );

            lblCartaoSus.setText(
                    "Cartão SUS: --"
            );

            lblDataEntrada.setText(
                    "Data de entrada: --"
            );
        }
    }
}
