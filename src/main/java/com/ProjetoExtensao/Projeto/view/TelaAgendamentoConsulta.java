package com.ProjetoExtensao.Projeto.view;

import com.ProjetoExtensao.Projeto.infra.Cores;
import com.ProjetoExtensao.Projeto.infra.IconManager;
import com.ProjetoExtensao.Projeto.infra.PanelsFactory;
import com.ProjetoExtensao.Projeto.models.ResponsavelSaude;
import com.ProjetoExtensao.Projeto.models.TipoConsulta;
import com.ProjetoExtensao.Projeto.servicos.ConsultaService;
import com.ProjetoExtensao.Projeto.servicos.NavigationService;
import com.ProjetoExtensao.Projeto.servicos.PacienteService;
import com.ProjetoExtensao.Projeto.servicos.ResponsavelService;
import com.ProjetoExtensao.Projeto.utils.CPFUtils;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

@org.springframework.stereotype.Component
@NoArgsConstructor
public class TelaAgendamentoConsulta extends JFrame {
    @Autowired
    private PanelsFactory panelsFactory;
    @Autowired
    private IconManager iconManager;
    @Autowired
    private ResponsavelService responsavelService;
    @Autowired
    private ConsultaService consultaService;
    @Autowired
    private NavigationService navigationService;
    @Autowired
    private PacienteService pacienteService;

    private JComboBox<String> especialidadeComboBox;
    private JComboBox<String> medicosComboBox;
    private JComboBox<String> horaComboBox;
    private JComboBox<String> minutoComboBox;

    private JFormattedTextField cpfField;
    private JFormattedTextField dataField;
    private JTextArea motivoConsultaArea;
    private JTextArea diagnosticoArea;
    private JTextArea anotacoesMedicoArea;

    @PostConstruct
    public void initUI() {
        setTitle("Recanto do Sagrado Coração - Agendamento de Consulta");
        setSize(1200, 800);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        
        // Adicionar listener para limpar campos quando a tela ficar visível
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                limparCampos();
            }
        });

        // Usando PanelsFactory para cabeçalho e rodapé
        JPanel headerPanel = panelsFactory.getHeaderPanel();
        JPanel footerPanel = panelsFactory.getFooterPanel();

        // Painel principal (formulário)
        JPanel mainPanel = criarPainelPrincipal();
        mainPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Adiciona tudo ao frame
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel criarPainelPrincipal() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Cores.COR_FUNDO_CLARO);

        Color azulEscuro = Cores.COR_RODAPE;
        Color cinzaTitulo = Cores.COR_LETRA_PAINEL;

        // --- CABEÇALHO DE SEÇÃO --- //
        JPanel sectionHeader = new JPanel(new BorderLayout(10, 0));
        sectionHeader.setBackground(Cores.COR_FUNDO_CLARO);
        sectionHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        sectionHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        sectionHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Cores.COR_LETRA_PAINEL));
        mainPanel.add(sectionHeader);

        JLabel sectionTitle = new JLabel("Agendamento de Consulta");
        sectionTitle.setFont(new Font("Arial", Font.PLAIN, 36));
        sectionTitle.setForeground(cinzaTitulo);
        sectionTitle.setBorder(new EmptyBorder(0, 20, 0, 0));
        sectionHeader.add(sectionTitle, BorderLayout.WEST);

        JPanel sectionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        sectionButtonsPanel.setOpaque(false);

        JButton voltarConsultaBtn = new JButton("\u2190 Voltar para a Consulta");
        voltarConsultaBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        voltarConsultaBtn.setForeground(cinzaTitulo);
        voltarConsultaBtn.setContentAreaFilled(false);
        voltarConsultaBtn.setBorderPainted(false);
        voltarConsultaBtn.setFocusPainted(false);
        sectionButtonsPanel.add(voltarConsultaBtn);

        sectionHeader.add(sectionButtonsPanel, BorderLayout.EAST);

        // --- AÇÃO DO BOTÃO "Voltar para a Consulta" ---
        voltarConsultaBtn.addActionListener(e -> {
            navigationService.abrirTelaConsultas();
            dispose();
        });

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- FORMULÁRIO --- //
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(true);
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(formPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // --- NOME DO PACIENTE ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 0);
        JLabel nomeLabel = new JLabel("CPF do Paciente");
        nomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        nomeLabel.setForeground(azulEscuro);
        formPanel.add(nomeLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        try {
            MaskFormatter cpfFormatter = new MaskFormatter("###.###.###-##");
            cpfFormatter.setPlaceholderCharacter('_');
            cpfField = new JFormattedTextField(cpfFormatter);
            cpfField.setFont(new Font("Arial", Font.PLAIN, 18));
            cpfField.setForeground(azulEscuro);
            cpfField.setToolTipText("Digite o CPF do Paciente");
        } catch (ParseException e) {
            cpfField = new JFormattedTextField();
        }
        formPanel.add(cpfField, gbc);

        // Reset gridwidth for subsequent rows, keep weightx
        gbc.gridwidth = 1;

        // --- SELECIONAR MÉDICO ---
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 0);
        JLabel medicoLabel = new JLabel("Selecionar Médico");
        medicoLabel.setFont(new Font("Arial", Font.BOLD, 20));
        medicoLabel.setForeground(azulEscuro);
        formPanel.add(medicoLabel, gbc);

        // --- SELECIONAR ESPECIALIDADE ---
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 10, 5, 0);
        JLabel especialidadeLabel = new JLabel("Selecionar o tipo de Consulta");
        especialidadeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        especialidadeLabel.setForeground(azulEscuro);
        formPanel.add(especialidadeLabel, gbc);

        medicosComboBox = new JComboBox<>();
        medicosComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
        medicosComboBox.setForeground(cinzaTitulo);
        popularMedicos();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(medicosComboBox, gbc);

        especialidadeComboBox = new JComboBox<>();
        especialidadeComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
        especialidadeComboBox.setForeground(cinzaTitulo);
        popularEspecialidades();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 10, 20, 0);
        formPanel.add(especialidadeComboBox, gbc);

        // --- DATA ---
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 0);
        JLabel dataLabel = new JLabel("Data");
        dataLabel.setFont(new Font("Arial", Font.BOLD, 20));
        dataLabel.setForeground(azulEscuro);
        formPanel.add(dataLabel, gbc);

        // --- HORÁRIO ---
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 10, 5, 0);
        JLabel horarioLabel = new JLabel("Horário");
        horarioLabel.setFont(new Font("Arial", Font.BOLD, 20));
        horarioLabel.setForeground(azulEscuro);
        formPanel.add(horarioLabel, gbc);

        // Nested panel for data field and calendar icon
        JPanel dataFieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        dataFieldPanel.setOpaque(false);
        try {
            MaskFormatter dataFormatter = new MaskFormatter("##/##/####");
            dataFormatter.setPlaceholderCharacter('_');
            dataField = new JFormattedTextField(dataFormatter);
            dataField.setFont(new Font("Arial", Font.PLAIN, 18));
            dataField.setToolTipText("dd/mm/aaaa");
            dataField.setForeground(azulEscuro);
            dataField.setPreferredSize(new Dimension(170, 35));
        } catch (ParseException e) {
            dataField = new JFormattedTextField();
        }
        dataFieldPanel.add(dataField);

        // Re-enable calendar icon
        ImageIcon calendarIcon = iconManager.createScaledIcon("/assets/calendar.png", 20, 20);
        if (calendarIcon != null) {
            JLabel calendarIconLabel = new JLabel(calendarIcon);
            dataFieldPanel.add(calendarIconLabel);
        }

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(dataFieldPanel, gbc);

        // Painel para hora e minuto
        JPanel horarioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        horarioPanel.setOpaque(false);
        
        horaComboBox = new JComboBox<>();
        horaComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
        for (int i = 0; i < 24; i++) {
            horaComboBox.addItem(String.format("%02d", i));
        }
        horarioPanel.add(horaComboBox);
        
        JLabel separadorLabel = new JLabel(":");
        separadorLabel.setFont(new Font("Arial", Font.BOLD, 18));
        horarioPanel.add(separadorLabel);
        
        minutoComboBox = new JComboBox<>();
        minutoComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
        for (int i = 0; i < 60; i += 5) {
            minutoComboBox.addItem(String.format("%02d", i));
        }
        horarioPanel.add(minutoComboBox);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 10, 20, 0);
        formPanel.add(horarioPanel, gbc);

        // --- MOTIVO DA CONSULTA ---
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 0);
        JLabel motivoLabel = new JLabel("Motivo da Consulta *");
        motivoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        motivoLabel.setForeground(azulEscuro);
        formPanel.add(motivoLabel, gbc);

        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 15, 0);
        motivoConsultaArea = new JTextArea(3, 20);
        motivoConsultaArea.setFont(new Font("Arial", Font.PLAIN, 14));
        motivoConsultaArea.setLineWrap(true);
        motivoConsultaArea.setWrapStyleWord(true);
        JScrollPane motivoScroll = new JScrollPane(motivoConsultaArea);
        formPanel.add(motivoScroll, gbc);

        // --- DIAGNÓSTICO ---
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 0, 5, 0);
        JLabel diagnosticoLabel = new JLabel("Diagnóstico");
        diagnosticoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        diagnosticoLabel.setForeground(azulEscuro);
        formPanel.add(diagnosticoLabel, gbc);

        gbc.gridy = 9;
        gbc.insets = new Insets(0, 0, 15, 0);
        diagnosticoArea = new JTextArea(3, 20);
        diagnosticoArea.setFont(new Font("Arial", Font.PLAIN, 14));
        diagnosticoArea.setLineWrap(true);
        diagnosticoArea.setWrapStyleWord(true);
        JScrollPane diagnosticoScroll = new JScrollPane(diagnosticoArea);
        formPanel.add(diagnosticoScroll, gbc);

        // --- ANOTAÇÕES DO MÉDICO ---
        gbc.gridy = 10;
        gbc.insets = new Insets(0, 0, 5, 0);
        JLabel anotacoesLabel = new JLabel("Anotações do Médico");
        anotacoesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        anotacoesLabel.setForeground(azulEscuro);
        formPanel.add(anotacoesLabel, gbc);

        gbc.gridy = 11;
        gbc.insets = new Insets(0, 0, 20, 0);
        anotacoesMedicoArea = new JTextArea(3, 20);
        anotacoesMedicoArea.setFont(new Font("Arial", Font.PLAIN, 14));
        anotacoesMedicoArea.setLineWrap(true);
        anotacoesMedicoArea.setWrapStyleWord(true);
        JScrollPane anotacoesScroll = new JScrollPane(anotacoesMedicoArea);
        formPanel.add(anotacoesScroll, gbc);

        // --- BOTÃO ATUALIZAR ---
        JButton refreshButton = panelsFactory.getRefreshButton();
        JButton adminButton = panelsFactory.getAdminButton();

        adminButton.addActionListener(e -> {
            navigationService.abrirTelaGeral();
            dispose();
        });
        if (refreshButton != null) {
            refreshButton.addActionListener(e -> limparCampos());
        }

        // Adiciona o botão Agendar ao formPanel com GridBagLayout
        JButton agendarBtn = new JButton("Agendar");
        agendarBtn.setFont(new Font("Arial", Font.BOLD, 20));
        agendarBtn.setBackground(Cores.COR_RODAPE);
        agendarBtn.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(20, 0, 0, 0);
        formPanel.add(agendarBtn, gbc);

        agendarBtn.addActionListener(e -> salvarConsulta());
        return mainPanel;
    }

    private void salvarConsulta() {
        // Validar campos obrigatórios
        if (!validarCampos()) {
            return;
        }

        try {
            String pacienteCpf = CPFUtils.limparCPF(cpfField.getText());
            String data = dataField.getText();
            String hora = horaComboBox.getSelectedItem() + ":" + minutoComboBox.getSelectedItem();

            String medicoNome = (String) medicosComboBox.getSelectedItem();
            String tipoConsulta = (String) especialidadeComboBox.getSelectedItem();
            
            String motivoConsulta = motivoConsultaArea.getText().trim();
            String diagnostico = diagnosticoArea.getText().trim();
            String anotacoesMedico = anotacoesMedicoArea.getText().trim();

            // Verificar se o paciente existe
            try {
                pacienteService.findPacienteByCpf(pacienteCpf);
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, "CPF não cadastrado! Por favor, cadastre o paciente primeiro.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            consultaService.salvarConsulta(pacienteCpf, data, hora, medicoNome, tipoConsulta, motivoConsulta, diagnostico, anotacoesMedico);

            JOptionPane.showMessageDialog(this, "✓ Consulta agendada com sucesso!\n\nVocê pode agendar outra consulta ou fechar esta janela.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao agendar consulta: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCampos() {
        // Validar CPF usando a classe utilitária
        String cpf = CPFUtils.limparCPF(cpfField.getText());
        if (cpf.isEmpty() || !CPFUtils.validarTamanhoCPF(cpf)) {
            JOptionPane.showMessageDialog(this, "CPF é obrigatório e deve ter 11 dígitos!", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar médico
        if (medicosComboBox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Selecione um médico!", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar tipo de consulta
        if (especialidadeComboBox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Selecione o tipo de consulta!", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar data
        String data = dataField.getText();
        if (data.isEmpty() || data.equals("dd/MM/yyyy") || data.contains("_")) {
            JOptionPane.showMessageDialog(this, "Data é obrigatória!", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar motivo da consulta
        if (motivoConsultaArea.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Motivo da consulta é obrigatório!", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void popularMedicos() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("Selecione o médico");

        List<ResponsavelSaude> responsavelSaudeList = responsavelService.findAllResponsaveis();
        for (ResponsavelSaude rs : responsavelSaudeList) {
            model.addElement(rs.getNomeCompleto());
        }
        medicosComboBox.setModel(model);
    }

    private void popularEspecialidades() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("Selecione o tipo de consulta");

        List<TipoConsulta> tipos = Arrays.stream(TipoConsulta.values()).toList();
        for (TipoConsulta tipo : tipos) {
            model.addElement(tipo.toString());
        }
        especialidadeComboBox.setModel(model);
    }

    private void addPlaceholder(JTextField textField, String placeholder) {
        Color placeholderColor = Color.GRAY;
        Color originalColor = new Color(0x1C2F5C);

        textField.setText(placeholder);
        textField.setForeground(placeholderColor);

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

    private void limparCampos() {
        if (cpfField != null) {
            cpfField.setText("");
        }
        if (dataField != null) {
            dataField.setText("");
        }
        if (medicosComboBox != null) medicosComboBox.setSelectedIndex(0);
        if (especialidadeComboBox != null) especialidadeComboBox.setSelectedIndex(0);
        if (horaComboBox != null) horaComboBox.setSelectedIndex(0);
        if (minutoComboBox != null) minutoComboBox.setSelectedIndex(0);
        if (motivoConsultaArea != null) motivoConsultaArea.setText("");
        if (diagnosticoArea != null) diagnosticoArea.setText("");
        if (anotacoesMedicoArea != null) anotacoesMedicoArea.setText("");
    }

}
