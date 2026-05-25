package com.ProjetoExtensao.Projeto.view;

import com.ProjetoExtensao.Projeto.infra.Cores;
import com.ProjetoExtensao.Projeto.infra.DateTimeFormatter;
import com.ProjetoExtensao.Projeto.infra.IconManager;
import com.ProjetoExtensao.Projeto.infra.PanelsFactory;
import com.ProjetoExtensao.Projeto.models.Consulta;
import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.servicos.ConsultaService;
import com.ProjetoExtensao.Projeto.servicos.NavigationService;
import com.ProjetoExtensao.Projeto.servicos.PacienteService;
import com.ProjetoExtensao.Projeto.utils.CPFUtils;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;

@org.springframework.stereotype.Component
@NoArgsConstructor
public class TelaConsultas extends JFrame {
    @Autowired
    private PanelsFactory panelsFactory;
    @Autowired
    private IconManager iconManager;
    @Autowired
    private NavigationService navigationService;
    @Autowired
    private ConsultaService consultaService;
    @Autowired
    private PacienteService pacienteService;

    private JTextField medicoDetalhesField;
    private JTextField consultaNumField;
    private JTextField dataConsultaField;
    private JTextField horaConsultaField;
    private JTextField pacienteDetalhesField;
    private JTextField especialidadeDetalhesField;
    private JTextField diagnosticoField;
    private JTextArea triagemArea;
    private JTextArea medicacaoArea;

    // Tabela de consultas
    private JTable tabelaConsultas;
    private javax.swing.table.DefaultTableModel modeloTabela;
    private JPanel painelTabela;
    private List<Consulta> consultasEncontradas;

    // Campos de pesquisa
    private JTextField pacientCpfField;
    private JTextField medicoPesquisaField;
    private JPanel pesquisaPanel;
    private JButton refreshButton;


    @PostConstruct
    public void initUi() {
        setTitle("Recanto do Sagrado Coração - Consultas");
        setSize(1200, 800);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setLayout(new BorderLayout());
        
        // Adicionar listener para limpar campos quando a tela ficar visível
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                limparCamposPesquisa();
            }
        });

        Color azulEscuro = Cores.COR_RODAPE;
        Color cinzaTitulo = Cores.COR_LETRA_PAINEL;

        // CABEÇALHO INSTITUCIONAL
        JPanel headerPanel = panelsFactory.getHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        this.refreshButton = panelsFactory.getRefreshButton();

        // RODAPÉ
        JPanel footerPanel = panelsFactory.getFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);

        // PAINEL CENTRAL QUE CONTÉM O CONTEÚDO PRINCIPAL
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Cores.COR_FUNDO_CLARO);
        contentPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        add(contentPanel, BorderLayout.CENTER);

        // CABEÇALHO DE SEÇÃO "Consultas"
        JPanel sectionHeader = new JPanel(new BorderLayout(10, 0));
        sectionHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Cores.COR_LETRA_PAINEL));
        sectionHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        sectionHeader.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sectionTitle = new JLabel("Consultas");
        sectionTitle.setFont(new Font("Arial", Font.PLAIN, 36));
        sectionTitle.setForeground(cinzaTitulo);
        sectionTitle.setBorder(new EmptyBorder(0, 20, 0, 0));
        sectionHeader.add(sectionTitle, BorderLayout.WEST);

        JPanel sectionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        sectionButtonsPanel.setOpaque(false);

        JButton novaConsultaBtn = new JButton("Nova Consulta");
        novaConsultaBtn.setFont(new Font("Arial", Font.BOLD, 16));
        novaConsultaBtn.setBackground(azulEscuro);
        novaConsultaBtn.setForeground(Color.WHITE);
        novaConsultaBtn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        novaConsultaBtn.setFocusPainted(false);
        sectionButtonsPanel.add(novaConsultaBtn);

        // Ação do botão "Nova Consulta"
        novaConsultaBtn.addActionListener(e -> {
            navigationService.abrirTelaAgendamentoConsultas();
            dispose();
        });

        sectionHeader.add(sectionButtonsPanel, BorderLayout.EAST);
        contentPanel.add(sectionHeader);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // SEÇÃO "Parâmetros de Pesquisa"
        pesquisaPanel = new JPanel();
        pesquisaPanel.setLayout(new GridBagLayout());
        pesquisaPanel.setBackground(Cores.COR_FUNDO_CLARO);
        Border innerBorder = new EmptyBorder(20, 20, 20, 20);
        pesquisaPanel.setBorder(innerBorder);
        pesquisaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        pesquisaPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // Painel para o título com fundo E3E0E0
        JPanel tituloPesquisaPanel = new JPanel(new BorderLayout());
        tituloPesquisaPanel.setBackground(Cores.COR_FUNDO_CLARO);
        tituloPesquisaPanel.setBorder(new EmptyBorder(0, 5, 0, 0));
        JLabel paramPesquisaLabel = new JLabel("Parâmetros de Pesquisa");
        paramPesquisaLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        paramPesquisaLabel.setForeground(new Color(0x556270));
        paramPesquisaLabel.setHorizontalAlignment(SwingConstants.LEFT);
        tituloPesquisaPanel.add(paramPesquisaLabel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        pesquisaPanel.add(tituloPesquisaPanel, gbc);

        gbc.gridwidth = 1;
        gbc.weightx = 0.5;

        JLabel pacientePesquisaLabel = new JLabel("CPF do Paciente");
        pacientePesquisaLabel.setFont(new Font("Arial", Font.BOLD, 14));
        pacientePesquisaLabel.setBackground(Cores.COR_FUNDO_CLARO);
        pacientePesquisaLabel.setForeground(azulEscuro);
        pesquisaPanel.add(pacientePesquisaLabel, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        pacientCpfField = new JTextField();
        pacientCpfField.setFont(new Font("Arial", Font.PLAIN, 16));
        pacientCpfField.setForeground(azulEscuro);
        
        // Adicionar formatação automática de CPF usando a classe utilitária
        CPFUtils.aplicarFormatacaoAutomatica(pacientCpfField);
        
        addPlaceholder(pacientCpfField, "CPF do Paciente");
        pesquisaPanel.add(pacientCpfField, gbc);

        gbc.gridx = 2; // Coluna 3
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 0.0;
        JButton pesquisarBtn = new JButton("Pesquisar");
        pesquisarBtn.setFont(new Font("Arial", Font.BOLD, 14));
        pesquisarBtn.setBackground(azulEscuro);
        pesquisarBtn.setForeground(Color.WHITE);
        pesquisarBtn.setFocusPainted(false);
        pesquisarBtn.setBorder(new EmptyBorder(8, 15, 8, 15));
        pesquisaPanel.add(pesquisarBtn, gbc);

        contentPanel.add(pesquisaPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // SEÇÃO "Detalhes da Consulta" com Scroll
        JPanel detalhesPanel = new JPanel();
        detalhesPanel.setLayout(new GridBagLayout());
        detalhesPanel.setBackground(Color.WHITE);
        detalhesPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        detalhesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel detalhesTituloLabel = new JLabel("Detalhes da Consulta");
        detalhesTituloLabel.setFont(new Font("Arial", Font.BOLD, 18));
        detalhesTituloLabel.setForeground(azulEscuro);
        detalhesTituloLabel.setHorizontalAlignment(SwingConstants.LEFT);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(0, 0, 15, 0);
        detalhesPanel.add(detalhesTituloLabel, gbc);

        Color borderColor = new Color(200, 200, 200);

        // Linha 1: Consulta, Data, Hora
        // Título Consulta
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.33;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 0, 0, 0);
        JLabel consultaLabel = new JLabel("Consulta:");
        consultaLabel.setFont(new Font("Arial", Font.BOLD, 14));
        consultaLabel.setForeground(azulEscuro);
        detalhesPanel.add(consultaLabel, gbc);

        // Título Data da Consulta
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.33; // Coluna 2
        gbc.insets = new Insets(10, 10, 0, 0);
        JLabel dataConsultaLabel = new JLabel("Data da Consulta:");
        dataConsultaLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dataConsultaLabel.setForeground(azulEscuro);
        detalhesPanel.add(dataConsultaLabel, gbc);

        // Título Hora da Consulta
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 0.34;
        gbc.insets = new Insets(10, 10, 0, 0);
        JLabel horaConsultaLabel = new JLabel("Hora da Consulta:");
        horaConsultaLabel.setFont(new Font("Arial", Font.BOLD, 14));
        horaConsultaLabel.setForeground(azulEscuro);
        detalhesPanel.add(horaConsultaLabel, gbc);

        // Campos de texto para a Linha 1
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 2;
        consultaNumField = new JTextField();
        consultaNumField.setFont(new Font("Arial", Font.PLAIN, 16));
        consultaNumField.setBackground(Color.WHITE);
        consultaNumField.setEditable(false);
        consultaNumField.setBorder(BorderFactory.createLineBorder(borderColor));
        detalhesPanel.add(consultaNumField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 10, 10, 0);
        dataConsultaField = new JTextField();
        dataConsultaField.setFont(new Font("Arial", Font.PLAIN, 16));
        dataConsultaField.setBackground(Color.WHITE);
        dataConsultaField.setEditable(false);
        dataConsultaField.setBorder(BorderFactory.createLineBorder(borderColor));
        detalhesPanel.add(dataConsultaField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(0, 10, 10, 0);
        horaConsultaField = new JTextField();
        horaConsultaField.setFont(new Font("Arial", Font.PLAIN, 16));
        horaConsultaField.setBackground(Color.WHITE);
        horaConsultaField.setEditable(false);
        horaConsultaField.setBorder(BorderFactory.createLineBorder(borderColor));
        detalhesPanel.add(horaConsultaField, gbc);

        // Linha 2: Paciente
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 0, 0, 0);
        JLabel pacienteLabel = new JLabel("Paciente:");
        pacienteLabel.setFont(new Font("Arial", Font.BOLD, 14));
        pacienteLabel.setForeground(azulEscuro);
        detalhesPanel.add(pacienteLabel, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 20, 0);
        pacienteDetalhesField = new JTextField();
        pacienteDetalhesField.setFont(new Font("Arial", Font.PLAIN, 16));
        pacienteDetalhesField.setBackground(Color.WHITE);
        pacienteDetalhesField.setEditable(false);
        pacienteDetalhesField.setBorder(BorderFactory.createLineBorder(borderColor));
        detalhesPanel.add(pacienteDetalhesField, gbc);

        // Linha 3: Médico, Especialidade
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 0, 0, 0);
        JLabel medicoDetalhesLabel = new JLabel("Médico:");
        medicoDetalhesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        medicoDetalhesLabel.setForeground(azulEscuro);
        detalhesPanel.add(medicoDetalhesLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(10, 10, 0, 0);
        JLabel especialidadeDetalhesLabel = new JLabel("Tipo de Consulta:");
        especialidadeDetalhesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        especialidadeDetalhesLabel.setForeground(azulEscuro);
        detalhesPanel.add(especialidadeDetalhesLabel, gbc);

        // Campos de texto para a Linha 3
        gbc.insets = new Insets(0, 0, 20, 0);

        gbc.gridx = 0;
        gbc.gridy = 6;
        medicoDetalhesField = new JTextField();
        medicoDetalhesField.setFont(new Font("Arial", Font.PLAIN, 16));
        medicoDetalhesField.setBackground(Color.WHITE);
        medicoDetalhesField.setEditable(false);
        medicoDetalhesField.setBorder(BorderFactory.createLineBorder(borderColor));
        detalhesPanel.add(medicoDetalhesField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 10, 20, 0);
        especialidadeDetalhesField = new JTextField();
        especialidadeDetalhesField.setFont(new Font("Arial", Font.PLAIN, 16));
        especialidadeDetalhesField.setBackground(Color.WHITE);
        especialidadeDetalhesField.setEditable(false);
        especialidadeDetalhesField.setBorder(BorderFactory.createLineBorder(borderColor));
        detalhesPanel.add(especialidadeDetalhesField, gbc);

        // Linha 4: Diagnóstico
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 0, 0, 0);
        JLabel diagnosticoLabel = new JLabel("Diagnóstico:");
        diagnosticoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        diagnosticoLabel.setForeground(azulEscuro);
        detalhesPanel.add(diagnosticoLabel, gbc);

        gbc.gridy = 8;
        gbc.insets = new Insets(0, 0, 20, 0);
        diagnosticoField = new JTextField();
        diagnosticoField.setFont(new Font("Arial", Font.PLAIN, 16));
        diagnosticoField.setBackground(Color.WHITE);
        diagnosticoField.setEditable(false);
        diagnosticoField.setBorder(BorderFactory.createLineBorder(borderColor));
        detalhesPanel.add(diagnosticoField, gbc);

        // Linha 5: Triagem
        gbc.gridy = 9;
        gbc.gridx = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 0, 0, 0);
        JLabel triagemLabel = new JLabel("Triagem (Motivo da Consulta):");
        triagemLabel.setFont(new Font("Arial", Font.BOLD, 14));
        triagemLabel.setForeground(azulEscuro);
        detalhesPanel.add(triagemLabel, gbc);

        gbc.gridy = 10;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 20, 0);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weighty = 0.5;
        triagemArea = new JTextArea(5, 20);
        triagemArea.setFont(new Font("Arial", Font.PLAIN, 16));
        triagemArea.setBackground(Color.WHITE);
        triagemArea.setLineWrap(true);
        triagemArea.setWrapStyleWord(true);
        triagemArea.setEditable(false);
        triagemArea.setBorder(BorderFactory.createLineBorder(borderColor));
        detalhesPanel.add(triagemArea, gbc);

        // Linha 6: Medicação
        gbc.gridy = 11;
        gbc.gridx = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 0, 0, 0);
        gbc.weighty = 0.0;
        JLabel medicacaoLabel = new JLabel("Anotações do Médico:");
        medicacaoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        medicacaoLabel.setForeground(azulEscuro);
        detalhesPanel.add(medicacaoLabel, gbc);

        gbc.gridy = 12;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 20, 0);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weighty = 0.5;
        medicacaoArea = new JTextArea(5, 20);
        medicacaoArea.setFont(new Font("Arial", Font.PLAIN, 16));
        medicacaoArea.setBackground(Color.WHITE);
        medicacaoArea.setLineWrap(true);
        medicacaoArea.setWrapStyleWord(true);
        medicacaoArea.setEditable(false);
        medicacaoArea.setBorder(BorderFactory.createLineBorder(borderColor));
        detalhesPanel.add(medicacaoArea, gbc);

        // Adicionar scroll ao painel de detalhes
        JScrollPane scrollPane = new JScrollPane(detalhesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        contentPanel.add(scrollPane);

        // Ação do botão Atualizar
        if (refreshButton != null) {
            refreshButton.addActionListener(e -> {
                limparCamposDetalhes();
                pacientCpfField.setText("");

                addPlaceholder(pacientCpfField, "CPF do Paciente");
                pesquisaPanel.setBackground(new Color(0xE3E0E0));
            });
        }

        // Ação do botão Pesquisar
        pesquisarBtn.addActionListener(e -> {
            String pacienteCpf = pacientCpfField.getText();

            if (pacienteCpf.isEmpty() || pacienteCpf.equals("CPF do Paciente")) {
                JOptionPane.showMessageDialog(this, "Por favor, digite um CPF para pesquisar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                // Remover formatação do CPF antes de buscar usando a classe utilitária
                String cpfLimpo = CPFUtils.limparCPF(pacienteCpf);
                
                if (!CPFUtils.validarTamanhoCPF(cpfLimpo)) {
                    JOptionPane.showMessageDialog(this, "CPF inválido. Digite um CPF com 11 dígitos.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Tentar buscar o paciente
                Paciente paciente = null;
                try {
                    paciente = pacienteService.findPacienteByCpf(cpfLimpo);
                } catch (RuntimeException ex) {
                    limparCamposDetalhes();
                    JOptionPane.showMessageDialog(this, 
                        "Paciente não encontrado!\n\nCPF pesquisado: " + cpfLimpo + "\n\nVerifique se o paciente está cadastrado.", 
                        "Paciente não encontrado", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Buscar todas as consultas do paciente
                consultasEncontradas = consultaService.findAllConsultasByPaciente(paciente);
                
                if (consultasEncontradas == null || consultasEncontradas.isEmpty()) {
                    limparCamposDetalhes();
                    JOptionPane.showMessageDialog(this, 
                        "Paciente encontrado, mas não possui consultas cadastradas.\n\nPaciente: " + paciente.getNomeCompleto(), 
                        "Sem consultas", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else if (consultasEncontradas.size() == 1) {
                    // Se tem apenas uma consulta, mostrar direto
                    atualizarDadosConsulta(consultasEncontradas.get(0));
                } else {
                    // Se tem múltiplas consultas, mostrar lista para seleção
                    mostrarListaConsultas(consultasEncontradas, paciente);
                }
            } catch (RuntimeException ex) {
                limparCamposDetalhes();
                JOptionPane.showMessageDialog(this, 
                    "Erro ao buscar consulta:\n" + ex.getMessage(), 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void atualizarDadosConsulta(Consulta consulta) {
        consultaNumField.setText(consulta.getId().toString());
        dataConsultaField.setText(consulta.getData().format(DateTimeFormatter.DATE_TIME_FORMATTER));
        horaConsultaField.setText(consulta.getHora().toString());
        pacienteDetalhesField.setText(consulta.getPaciente().getNomeCompleto());
        medicoDetalhesField.setText(consulta.getResponsavelSaude().getNomeCompleto());
        especialidadeDetalhesField.setText(consulta.getTipoConsulta().toString());
        
        // Preencher campos de texto
        if (triagemArea != null) {
            triagemArea.setText(consulta.getMotivoConsulta() != null ? consulta.getMotivoConsulta() : "");
        }
        if (diagnosticoField != null) {
            diagnosticoField.setText(consulta.getDiagnostico() != null ? consulta.getDiagnostico() : "");
        }
        if (medicacaoArea != null) {
            medicacaoArea.setText(consulta.getAnotacoesMedico() != null ? consulta.getAnotacoesMedico() : "");
        }
    }

    // Método para limpar os campos da seção Detalhes da Consulta
    private void limparCamposDetalhes() {
        consultaNumField.setText("");
        dataConsultaField.setText("");
        horaConsultaField.setText("");
        pacienteDetalhesField.setText("");
        medicoDetalhesField.setText("");
        especialidadeDetalhesField.setText("");
        diagnosticoField.setText("");
        triagemArea.setText("");
        medicacaoArea.setText("");

        // Garantir que a cor do texto seja azul escuro ao limpar os campos para futura digitação/carregamento
        if (consultaNumField != null) consultaNumField.setForeground(Cores.COR_RODAPE);
        if (dataConsultaField != null) dataConsultaField.setForeground(Cores.COR_RODAPE);
        if (horaConsultaField != null) horaConsultaField.setForeground(Cores.COR_RODAPE);
        if (pacienteDetalhesField != null) pacienteDetalhesField.setForeground(Cores.COR_RODAPE);
        if (medicoDetalhesField != null) medicoDetalhesField.setForeground(Cores.COR_RODAPE);
        if (especialidadeDetalhesField != null) especialidadeDetalhesField.setForeground(Cores.COR_RODAPE);
        if (diagnosticoField != null) diagnosticoField.setForeground(Cores.COR_RODAPE);
        if (triagemArea != null) triagemArea.setForeground(Cores.COR_RODAPE);
        if (medicacaoArea != null) medicacaoArea.setForeground(Cores.COR_RODAPE);

        if (pesquisaPanel != null) {
            pesquisaPanel.setBackground(Cores.COR_FUNDO_CLARO); // Volta para a cor original
        }
    }

    // Método para adicionar placeholder visual (mantido apenas para campos de pesquisa)
    private void addPlaceholder(JTextField textField, String placeholder) {
        Color placeholderColor = new Color(150, 150, 150);
        Color originalColor = Cores.COR_RODAPE;

        // Define o texto inicial e a cor
        textField.setText(placeholder);
        textField.setForeground(placeholderColor);

        // Adiciona o FocusListener
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(originalColor);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(placeholderColor);
                }
            }
        });
    }

    // Método para limpar campos de pesquisa ao abrir a tela
    private void limparCamposPesquisa() {
        if (pacientCpfField != null) {
            pacientCpfField.setText("");
            addPlaceholder(pacientCpfField, "CPF do Paciente");
        }
        limparCamposDetalhes();
    }

    // Método para mostrar lista de consultas quando há múltiplas
    private void mostrarListaConsultas(List<Consulta> consultas, Paciente paciente) {
        String[] opcoes = new String[consultas.size()];
        for (int i = 0; i < consultas.size(); i++) {
            Consulta c = consultas.get(i);
            opcoes[i] = String.format("Consulta %d - Dr(a). %s - %s - %s às %s", 
                c.getId(),
                c.getResponsavelSaude().getNomeCompleto(),
                c.getTipoConsulta(),
                c.getData().format(DateTimeFormatter.DATE_TIME_FORMATTER),
                c.getHora());
        }
        
        String escolha = (String) JOptionPane.showInputDialog(
            this,
            "Paciente: " + paciente.getNomeCompleto() + "\n\nEste paciente possui " + consultas.size() + " consultas.\nSelecione uma para ver os detalhes:",
            "Selecionar Consulta",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opcoes,
            opcoes[0]
        );
        
        if (escolha != null) {
            // Encontrar qual consulta foi selecionada
            for (int i = 0; i < opcoes.length; i++) {
                if (opcoes[i].equals(escolha)) {
                    atualizarDadosConsulta(consultas.get(i));
                    break;
                }
            }
        }
    }
}
