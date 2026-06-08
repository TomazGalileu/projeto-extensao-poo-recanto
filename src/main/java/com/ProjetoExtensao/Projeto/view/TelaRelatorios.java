package com.ProjetoExtensao.Projeto.view;

import com.ProjetoExtensao.Projeto.infra.Cores;
import com.ProjetoExtensao.Projeto.servicos.NavigationService;
import com.ProjetoExtensao.Projeto.servicos.RelatorioService;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import com.ProjetoExtensao.Projeto.utils.EventosOcorridos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

@org.springframework.stereotype.Component
@NoArgsConstructor
public class TelaRelatorios extends JFrame {

    @Autowired
    private RelatorioService relatorioService;

    @Autowired
    private NavigationService navigationService;

    private JTextField campoCpf;
    private JTextField campoDataInicio;
    private JTextField campoDataFim;
    private JTextField campoAno;
    private JComboBox<Integer> comboMes;
    private JComboBox<EventosOcorridos> comboTipoEvento;
    private JTextArea areaRelatorio;

    private final DateTimeFormatter formatadorData =
        DateTimeFormatter.ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    @PostConstruct
    private void initUI() {
        setTitle("Relatorios");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        add(criarPainelSuperior(), BorderLayout.NORTH);
        add(criarPainelCentral(), BorderLayout.CENTER);
        add(criarPainelInferior(), BorderLayout.SOUTH);
    }

    private JPanel criarPainelSuperior() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(new EmptyBorder(20, 30, 10, 30));
        painel.setBackground(Cores.COR_FUNDO_CLARO);

        JLabel titulo = new JLabel("Relatorios por Paciente");
        titulo.setFont(new Font("Arial", Font.BOLD, 26));
        titulo.setForeground(Cores.COR_LETRA_PAINEL);

        JLabel subtitulo = new JLabel("Informe o CPF da idosa para gerar um relatorio com dados pessoais, consultas e eventos sentinelas.");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitulo.setForeground(Cores.COR_LETRA_PAINEL);

        painel.add(titulo, BorderLayout.NORTH);
        painel.add(subtitulo, BorderLayout.SOUTH);

        return painel;
    }

    private JPanel criarPainelCentral() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(new EmptyBorder(10, 30, 10, 30));
        painel.setBackground(Cores.COR_FUNDO_CLARO);

        JPanel painelBusca = new JPanel();
        painelBusca.setLayout(new BoxLayout(painelBusca, BoxLayout.Y_AXIS));
        painelBusca.setOpaque(false);

        JPanel painelCampos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelCampos.setOpaque(false);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBotoes.setOpaque(false);

        JPanel painelMensal = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelMensal.setOpaque(false);

        JLabel labelCpf = new JLabel("CPF:");
        labelCpf.setFont(new Font("Arial", Font.PLAIN, 14));

        campoCpf = new JTextField(14);

        JLabel labelDataInicio = new JLabel("Data inicial:");
        labelDataInicio.setFont(new Font("Arial", Font.PLAIN, 14));

        campoDataInicio = new JTextField(8);
        campoDataInicio.setToolTipText("Formato: dd/MM/yyyy");

        JLabel labelDataFim = new JLabel("Data final:");
        labelDataFim.setFont(new Font("Arial", Font.PLAIN, 14));

        campoDataFim = new JTextField(8);
        campoDataFim.setToolTipText("Formato: dd/MM/yyyy");

        JLabel labelTipoEvento = new JLabel("Tipo de evento:");
        labelTipoEvento.setFont(new Font("Arial", Font.PLAIN, 14));

        comboTipoEvento = new JComboBox<>(EventosOcorridos.values());

        JLabel labelAno = new JLabel("Ano:");
        labelAno.setFont(new Font("Arial", Font.PLAIN, 14));

        campoAno = new JTextField(4);
        campoAno.setToolTipText("Exemplo: 2026");

        JLabel labelMes = new JLabel("Mes:");
        labelMes.setFont(new Font("Arial", Font.PLAIN, 14));

        comboMes = new JComboBox<>(new Integer[]{
            1, 2, 3, 4, 5, 6,
            7, 8, 9, 10, 11, 12
        });

        JButton botaoGerar = new JButton("Gerar Relatorio Geral");
        botaoGerar.addActionListener(e -> gerarRelatorio());

        JButton botaoGerarPeriodo = new JButton("Gerar por Periodo");
        botaoGerarPeriodo.addActionListener(e -> gerarRelatorioPorPeriodo());

        JButton botaoIndicadorEventos = new JButton("Indicador de Eventos");
        botaoIndicadorEventos.addActionListener(e -> gerarIndicadorEventos());

        JButton botaoIndicadorPorTipo = new JButton("Indicador por Tipo");
        botaoIndicadorPorTipo.addActionListener(e -> gerarIndicadorPorTipo());

        JButton botaoRelatorioMensal = new JButton("Relatorio Mensal");
        botaoRelatorioMensal.addActionListener(e -> gerarRelatorioMensal());

        JButton botaoRelatorioAnual = new JButton("Relatorio Anual");
        botaoRelatorioAnual.addActionListener(e -> gerarRelatorioAnual());

        painelCampos.add(labelCpf);
        painelCampos.add(campoCpf);
        painelCampos.add(labelDataInicio);
        painelCampos.add(campoDataInicio);
        painelCampos.add(labelDataFim);
        painelCampos.add(campoDataFim);
        painelCampos.add(labelTipoEvento);
        painelCampos.add(comboTipoEvento);
        painelMensal.add(labelAno);
        painelMensal.add(campoAno);
        painelMensal.add(labelMes);
        painelMensal.add(comboMes);
        painelMensal.add(botaoRelatorioMensal);

        painelBotoes.add(botaoGerar);
        painelBotoes.add(botaoGerarPeriodo);
        painelBotoes.add(botaoIndicadorEventos);
        painelBotoes.add(botaoIndicadorPorTipo);
        painelMensal.add(botaoRelatorioAnual);

        painelBusca.add(painelCampos);
        painelBusca.add(painelBotoes);
        painelBusca.add(painelMensal);
    

        areaRelatorio = new JTextArea();
        areaRelatorio.setEditable(false);
        areaRelatorio.setFont(new Font("Monospaced", Font.PLAIN, 14));
        areaRelatorio.setLineWrap(false);
        areaRelatorio.setWrapStyleWord(false);

        JScrollPane scrollPane = new JScrollPane(areaRelatorio);

        painel.add(painelBusca, BorderLayout.NORTH);
        painel.add(scrollPane, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarPainelInferior() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painel.setBorder(new EmptyBorder(10, 30, 20, 30));
        painel.setBackground(Cores.COR_FUNDO_CLARO);

        JButton botaoVoltar = new JButton("Voltar");
        botaoVoltar.addActionListener(e -> {
            navigationService.abrirTelaGeral();
            dispose();
        });

        painel.add(botaoVoltar);

        return painel;
    }

    private void gerarRelatorio() {
        String cpf = campoCpf.getText();

        if (cpf == null || cpf.isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe o CPF da paciente.",
                    "Campo obrigatorio",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String relatorio = relatorioService.gerarRelatorioPorCpf(cpf);
        areaRelatorio.setText(relatorio);
        areaRelatorio.setCaretPosition(0);
    }

    private void gerarRelatorioPorPeriodo() {
        String cpf = campoCpf.getText();

        if (cpf == null || cpf.isBlank()) {
            JOptionPane.showMessageDialog(
                this,
                "Informe o CPF da paciente.",
                "Campo obrigatorio",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            LocalDate dataInicio =
                LocalDate.parse(campoDataInicio.getText(), formatadorData);

            LocalDate dataFim =
                LocalDate.parse(campoDataFim.getText(), formatadorData);

            String relatorio =
                relatorioService.gerarRelatorioPorCpfEPeriodo(
                    cpf,
                    dataInicio,
                    dataFim
                );

            areaRelatorio.setText(relatorio);
            areaRelatorio.setCaretPosition(0);

        } catch (DateTimeParseException exception) {
            JOptionPane.showMessageDialog(
                this,
                "Informe as datas no formato dd/MM/yyyy. Exemplo: 01/01/2026",
                "Data invalida",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void gerarIndicadorEventos() {
        try {
            LocalDate dataInicio =
                LocalDate.parse(campoDataInicio.getText(), formatadorData);

            LocalDate dataFim =
                LocalDate.parse(campoDataFim.getText(), formatadorData);

            String indicador =
                relatorioService.gerarIndicadorEventosSentinelasPorPeriodo(
                    dataInicio,
                    dataFim
                );

            areaRelatorio.setText(indicador);
            areaRelatorio.setCaretPosition(0);

        } catch (DateTimeParseException exception) {
            JOptionPane.showMessageDialog(
                this,
                "Informe as datas no formato dd/MM/yyyy. Exemplo: 01/01/2026",
                "Data invalida",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void gerarIndicadorPorTipo() {
        try {
            LocalDate dataInicio =
                    LocalDate.parse(campoDataInicio.getText(), formatadorData);

            LocalDate dataFim =
                    LocalDate.parse(campoDataFim.getText(), formatadorData);

            EventosOcorridos tipoEvento =
                    (EventosOcorridos) comboTipoEvento.getSelectedItem();

            String indicador =
                    relatorioService.gerarIndicadorPorTipoEvento(
                            tipoEvento,
                            dataInicio,
                            dataFim
                    );

            areaRelatorio.setText(indicador);
            areaRelatorio.setCaretPosition(0);

        } catch (DateTimeParseException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe as datas no formato dd/MM/yyyy. Exemplo: 01/01/2026",
                    "Data invalida",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void gerarRelatorioMensal() {
        String anoDigitado = campoAno.getText();

        if (anoDigitado == null || anoDigitado.isBlank()) {
            JOptionPane.showMessageDialog(
                this,
                "Informe o ano do relatorio mensal.",
                "Campo obrigatorio",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            int ano = Integer.parseInt(anoDigitado);
            Integer mesSelecionado = (Integer) comboMes.getSelectedItem();

            if (ano < 1900 || ano > 2100) {
                JOptionPane.showMessageDialog(
                    this,
                    "Informe um ano valido entre 1900 e 2100.",
                    "Ano invalido",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (mesSelecionado == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "Selecione um mes.",
                    "Campo obrigatorio",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String relatorio =
                    relatorioService.gerarRelatorioInstitucionalMensal(
                        ano,
                        mesSelecionado
                    );

            areaRelatorio.setText(relatorio);
            areaRelatorio.setCaretPosition(0);

        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(
                this,
                "O ano deve conter somente numeros. Exemplo: 2026",
                "Ano invalido",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void gerarRelatorioAnual() {
        String anoDigitado = campoAno.getText();

        if (anoDigitado == null || anoDigitado.isBlank()) {
            JOptionPane.showMessageDialog(
                this,
                "Informe o ano do relatorio anual.",
                "Campo obrigatorio",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            int ano = Integer.parseInt(anoDigitado);

            if (ano < 1900 || ano > 2100) {
                JOptionPane.showMessageDialog(
                    this,
                    "Informe um ano valido entre 1900 e 2100.",
                    "Ano invalido",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String relatorio =
                    relatorioService.gerarRelatorioInstitucionalAnual(ano);

            areaRelatorio.setText(relatorio);
            areaRelatorio.setCaretPosition(0);

        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(
                this,
                "O ano deve conter somente numeros. Exemplo: 2026",
                "Ano invalido",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }
}