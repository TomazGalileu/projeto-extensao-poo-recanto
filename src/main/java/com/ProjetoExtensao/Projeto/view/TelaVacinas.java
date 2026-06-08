package com.ProjetoExtensao.Projeto.view;

import com.ProjetoExtensao.Projeto.infra.Cores;
import com.ProjetoExtensao.Projeto.infra.PanelsFactory;
import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.servicos.PacienteService;
import com.ProjetoExtensao.Projeto.servicos.VacinaService;
import com.ProjetoExtensao.Projeto.utils.CPFUtils;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;

@Component
@NoArgsConstructor
public class TelaVacinas extends JFrame {

    @Autowired
    private PanelsFactory panelsFactory;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private VacinaService vacinaService;

    @Autowired
    private TelaCadastroVacina telaCadastroVacina;

    private JTextField txtCpfBusca;
    private Paciente pacienteAtual;

    private JTextField txtNomeVacinaIndicador;
    private JLabel lblPercentual;

    private JTable tabelaVacinas;
    private DefaultTableModel modeloTabela;

    @PostConstruct
    public void initUI() {
        setTitle("Recanto do Sagrado Coração - Vacinas");
        setSize(1200, 800);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(panelsFactory.getHeaderPanel(), BorderLayout.NORTH);
        add(panelsFactory.getFooterPanel(), BorderLayout.SOUTH);

        JPanel painelCentral = new JPanel(new BorderLayout(0, 20));
        painelCentral.setBackground(Cores.COR_FUNDO_CLARO);
        painelCentral.setBorder(new EmptyBorder(20, 40, 20, 40));

        /*
         * Parte superior da tela:
         * título e área de pesquisa.
         */
        JPanel painelSuperior = new JPanel();
        painelSuperior.setLayout(
                new BoxLayout(painelSuperior, BoxLayout.Y_AXIS)
        );
        painelSuperior.setBackground(Cores.COR_FUNDO_CLARO);

        JLabel titulo = new JLabel("Controle de Vacinas");
        titulo.setFont(new Font("Arial", Font.PLAIN, 36));
        titulo.setForeground(Cores.COR_LETRA_PAINEL);
        titulo.setAlignmentX(JComponent.LEFT_ALIGNMENT);

        JButton btnNovaVacina = new JButton("Nova Vacina");
        btnNovaVacina.setFont(new Font("Arial", Font.BOLD, 14));
        btnNovaVacina.setBackground(Cores.COR_RODAPE);
        btnNovaVacina.setForeground(Color.WHITE);
        btnNovaVacina.setFocusPainted(false);
        btnNovaVacina.setBorder(
        new EmptyBorder(8, 15, 8, 15)
        );

        btnNovaVacina.addActionListener(e -> {
        telaCadastroVacina.limparCampos();
        telaCadastroVacina.setVisible(true);
        });

        JPanel painelTitulo = new JPanel(new BorderLayout());
        painelTitulo.setBackground(Cores.COR_FUNDO_CLARO);
        painelTitulo.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        painelTitulo.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 50)
        );

        painelTitulo.add(titulo, BorderLayout.WEST);
        painelTitulo.add(btnNovaVacina, BorderLayout.EAST);

        painelSuperior.add(painelTitulo);

        painelSuperior.add(
                Box.createRigidArea(new Dimension(0, 20))
        );

        /*
         * Área de pesquisa pelo CPF.
         */
        JPanel painelPesquisa = new JPanel(
                new BorderLayout(10, 0)
        );
        painelPesquisa.setBackground(Cores.COR_FUNDO_CLARO);
        painelPesquisa.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 40)
        );
        painelPesquisa.setAlignmentX(JComponent.LEFT_ALIGNMENT);

        JLabel labelCpf = new JLabel("CPF da Paciente:");
        labelCpf.setFont(new Font("Arial", Font.BOLD, 14));
        labelCpf.setForeground(Cores.COR_LETRA_PAINEL);

        txtCpfBusca = new JTextField();
        txtCpfBusca.setFont(new Font("Arial", Font.PLAIN, 16));

        CPFUtils.aplicarFormatacaoAutomatica(txtCpfBusca);

        JButton btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.setFont(new Font("Arial", Font.BOLD, 14));
        btnPesquisar.setBackground(Cores.COR_RODAPE);
        btnPesquisar.setForeground(Color.WHITE);
        btnPesquisar.setFocusPainted(false);
        btnPesquisar.setBorder(
                new EmptyBorder(8, 15, 8, 15)
        );

        painelPesquisa.add(labelCpf, BorderLayout.WEST);
        painelPesquisa.add(txtCpfBusca, BorderLayout.CENTER);
        painelPesquisa.add(btnPesquisar, BorderLayout.EAST);

        painelSuperior.add(painelPesquisa);

        painelSuperior.add(
                Box.createRigidArea(new Dimension(0, 15))
        );

        JPanel painelIndicador = new JPanel(
                new BorderLayout(10, 0)
        );
        painelIndicador.setBackground(Cores.COR_FUNDO_CLARO);
        painelIndicador.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 40)
        );
        painelIndicador.setAlignmentX(JComponent.LEFT_ALIGNMENT);

        JLabel labelVacinaIndicador =
                new JLabel("Vacina para indicador:");

        labelVacinaIndicador.setFont(
                new Font("Arial", Font.BOLD, 14)
        );
        labelVacinaIndicador.setForeground(
                Cores.COR_LETRA_PAINEL
        );

        txtNomeVacinaIndicador = new JTextField();
        txtNomeVacinaIndicador.setFont(
                new Font("Arial", Font.PLAIN, 16)
        );

        JButton btnCalcularPercentual =
                new JButton("Calcular Percentual");

        btnCalcularPercentual.setFont(
                new Font("Arial", Font.BOLD, 14)
        );
        btnCalcularPercentual.setBackground(
                Cores.COR_RODAPE
        );
        btnCalcularPercentual.setForeground(Color.WHITE);
        btnCalcularPercentual.setFocusPainted(false);
        btnCalcularPercentual.setBorder(
                new EmptyBorder(8, 15, 8, 15)
        );

        painelIndicador.add(
                labelVacinaIndicador,
                BorderLayout.WEST
        );
        painelIndicador.add(
                txtNomeVacinaIndicador,
                BorderLayout.CENTER
        );
        painelIndicador.add(
                btnCalcularPercentual,
                BorderLayout.EAST
        );

        painelSuperior.add(painelIndicador);

        lblPercentual = new JLabel(
                "Percentual de vacinação: --"
        );
        lblPercentual.setFont(
                new Font("Arial", Font.BOLD, 16)
        );
        lblPercentual.setForeground(
                Cores.COR_LETRA_PAINEL
        );
        lblPercentual.setAlignmentX(
                JComponent.LEFT_ALIGNMENT
        );

        painelSuperior.add(
                Box.createRigidArea(new Dimension(0, 10))
        );
        painelSuperior.add(lblPercentual);

        btnCalcularPercentual.addActionListener(
                e -> calcularPercentual()
        );

        /*
         * Tabela de vacinas.
         */
        String[] colunas = {
                "ID",
                "Vacina",
                "Data de Aplicação"
        };

        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {
                return false;
            }
        };

        tabelaVacinas = new JTable(modeloTabela);
        tabelaVacinas.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );
        tabelaVacinas.setRowHeight(30);
        tabelaVacinas.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tabelaVacinas.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 14)
        );
        tabelaVacinas.getTableHeader()
                .setBackground(Cores.COR_RODAPE);
        tabelaVacinas.getTableHeader()
                .setForeground(Color.WHITE);

        JScrollPane scrollPane =
                new JScrollPane(tabelaVacinas);

        /*
         * Ação do botão Pesquisar.
         */
        btnPesquisar.addActionListener(
                e -> buscarPaciente()
        );

        painelCentral.add(
                painelSuperior,
                BorderLayout.NORTH
        );
        painelCentral.add(
                scrollPane,
                BorderLayout.CENTER
        );

        add(painelCentral, BorderLayout.CENTER);
    }

    private void buscarPaciente() {
        String cpfDigitado = txtCpfBusca.getText();
        String cpfLimpo = CPFUtils.limparCPF(cpfDigitado);

        if (!CPFUtils.validarTamanhoCPF(cpfLimpo)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Digite um CPF válido com 11 dígitos.",
                    "CPF inválido",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {
            pacienteAtual =
                    pacienteService.findPacienteByCpf(cpfLimpo);

            carregarVacinas();
        } catch (RuntimeException ex) {
            pacienteAtual = null;
            limparTabela();

            JOptionPane.showMessageDialog(
                    this,
                    "Paciente não encontrada.",
                    "Paciente não encontrada",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void carregarVacinas() {
        limparTabela();

        if (pacienteAtual == null) {
            return;
        }

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");

        vacinaService
                .buscarPorPaciente(pacienteAtual.getId())
                .forEach(vacina -> {
                    Object[] linha = {
                            vacina.getId(),
                            vacina.getNome(),
                            vacina.getDataAplicacao()
                                    .format(formatter)
                    };

                    modeloTabela.addRow(linha);
                });
    }

    private void limparTabela() {
        modeloTabela.setRowCount(0);
    }

    private void calcularPercentual() {
    String nomeVacina =
            txtNomeVacinaIndicador.getText().trim();

    if (nomeVacina.isEmpty()) {
        JOptionPane.showMessageDialog(
                this,
                "Digite o nome da vacina.",
                "Campo obrigatório",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    try {
        double percentual =
                vacinaService.calcularPercentualVacinacao(nomeVacina);

        lblPercentual.setText(
                String.format(
                        "Percentual de vacinação de %s: %.2f%%",
                        nomeVacina,
                        percentual
                )
        );

    } catch (RuntimeException ex) {
        JOptionPane.showMessageDialog(
                this,
                "Erro ao calcular o percentual:\n" + ex.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
}
