package com.ProjetoExtensao.Projeto.view;

import com.ProjetoExtensao.Projeto.infra.Cores;
import com.ProjetoExtensao.Projeto.infra.PanelsFactory;
import com.ProjetoExtensao.Projeto.models.EventoSentinela;
import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.servicos.EventoSentinelaService;
import com.ProjetoExtensao.Projeto.servicos.NavigationService;
import com.ProjetoExtensao.Projeto.servicos.PacienteService;
import com.ProjetoExtensao.Projeto.utils.CPFUtils;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@NoArgsConstructor
public class TelaEventosSentinelas extends JFrame {
    @Autowired
    private PacienteService pacienteService;
    
    @Autowired
    private EventoSentinelaService eventoSentinelaService;
    
    @Autowired
    private NavigationService navigationService;
    
    @Autowired
    private TelaCadastroEventoSentinela telaCadastroEventoSentinela;
    
    @Autowired
    private PanelsFactory panelsFactory;

    private JTextField txtCpfBusca;
    private JTable tabelaEventos;
    private DefaultTableModel modeloTabela;
    private JButton refreshButton;
    private Paciente pacienteAtual = null;

    @PostConstruct
    public void initUI() {
        setTitle("Recanto do Sagrado Coração - Eventos Sentinelas");
        setSize(1200, 800);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Adicionar listener para limpar campos ao abrir
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

        // PAINEL CENTRAL
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Cores.COR_FUNDO_CLARO);
        contentPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        add(contentPanel, BorderLayout.CENTER);

        // CABEÇALHO DE SEÇÃO "Eventos Sentinelas"
        JPanel sectionHeader = new JPanel(new BorderLayout(10, 0));
        sectionHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Cores.COR_LETRA_PAINEL));
        sectionHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        sectionHeader.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        JLabel sectionTitle = new JLabel("Eventos Sentinelas");
        sectionTitle.setFont(new Font("Arial", Font.PLAIN, 36));
        sectionTitle.setForeground(cinzaTitulo);
        sectionTitle.setBorder(new EmptyBorder(0, 20, 0, 0));
        sectionHeader.add(sectionTitle, BorderLayout.WEST);

        JPanel sectionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        sectionButtonsPanel.setOpaque(false);

        JButton novoEventoBtn = new JButton("Novo Evento");
        novoEventoBtn.setFont(new Font("Arial", Font.BOLD, 16));
        novoEventoBtn.setBackground(azulEscuro);
        novoEventoBtn.setForeground(Color.WHITE);
        novoEventoBtn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        novoEventoBtn.setFocusPainted(false);
        sectionButtonsPanel.add(novoEventoBtn);

        novoEventoBtn.addActionListener(e -> abrirCadastroEvento());

        sectionHeader.add(sectionButtonsPanel, BorderLayout.EAST);
        contentPanel.add(sectionHeader);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // SEÇÃO "Parâmetros de Pesquisa"
        JPanel pesquisaPanel = new JPanel();
        pesquisaPanel.setLayout(new GridBagLayout());
        pesquisaPanel.setBackground(Cores.COR_FUNDO_CLARO);
        pesquisaPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        pesquisaPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        pesquisaPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

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

        JLabel cpfLabel = new JLabel("CPF do Paciente");
        cpfLabel.setFont(new Font("Arial", Font.BOLD, 14));
        cpfLabel.setForeground(azulEscuro);
        pesquisaPanel.add(cpfLabel, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        txtCpfBusca = new JTextField();
        txtCpfBusca.setFont(new Font("Arial", Font.PLAIN, 16));
        txtCpfBusca.setForeground(azulEscuro);
        CPFUtils.aplicarFormatacaoAutomatica(txtCpfBusca);
        addPlaceholder(txtCpfBusca, "Digite o CPF do paciente");
        pesquisaPanel.add(txtCpfBusca, gbc);

        gbc.gridx = 2;
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

        // TABELA DE EVENTOS
        String[] colunas = {"ID", "Tipo de Evento", "Descrição", "Data"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaEventos = new JTable(modeloTabela);
        tabelaEventos.setFont(new Font("Arial", Font.PLAIN, 14));
        tabelaEventos.setRowHeight(30);
        tabelaEventos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tabelaEventos.getTableHeader().setBackground(azulEscuro);
        tabelaEventos.getTableHeader().setForeground(Color.WHITE);
        tabelaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tabelaEventos);
        scrollPane.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        contentPanel.add(scrollPane);

        // Ação do botão Atualizar
        if (refreshButton != null) {
            refreshButton.addActionListener(e -> {
                limparTabela();
                txtCpfBusca.setText("");
                addPlaceholder(txtCpfBusca, "Digite o CPF do paciente");
            });
        }

        // Ação do botão Pesquisar
        pesquisarBtn.addActionListener(e -> buscarPaciente());
    }

    private void buscarPaciente() {
        String cpf = txtCpfBusca.getText();

        if (cpf.isEmpty() || cpf.equals("Digite o CPF do paciente")) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, digite um CPF para pesquisar.", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String cpfLimpo = CPFUtils.limparCPF(cpf);
            
            if (!CPFUtils.validarTamanhoCPF(cpfLimpo)) {
                JOptionPane.showMessageDialog(this, 
                    "CPF inválido. Digite um CPF com 11 dígitos.", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                pacienteAtual = pacienteService.findPacienteByCpf(cpfLimpo);
                atualizarTabelaEventos();
            } catch (RuntimeException ex) {
                pacienteAtual = null;
                limparTabela();
                JOptionPane.showMessageDialog(this, 
                    "Paciente não encontrado. Verifique o CPF digitado.", 
                    "Paciente Não Encontrado", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (RuntimeException ex) {
            limparTabela();
            JOptionPane.showMessageDialog(this, 
                "Erro ao buscar paciente:\n" + ex.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public void atualizarTabelaEventos() {
        limparTabela();
        
        if (pacienteAtual == null) {
            return;
        }

        List<EventoSentinela> eventos = eventoSentinelaService.findEventosByPaciente(pacienteAtual);
        
        if (eventos.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Nenhum evento registrado para este paciente.", 
                "Sem Eventos", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (EventoSentinela evento : eventos) {
            Object[] linha = {
                evento.getId(),
                formatarNomeEvento(evento.getEventosOcorridos().name()),
                evento.getDescricao(),
                evento.getDataEvento().format(formatter)
            };
            modeloTabela.addRow(linha);
        }
    }

    private String formatarNomeEvento(String nomeEnum) {
        return nomeEnum.replace("_", " ").toLowerCase()
            .substring(0, 1).toUpperCase() + 
            nomeEnum.replace("_", " ").toLowerCase().substring(1);
    }

    private void limparTabela() {
        modeloTabela.setRowCount(0);
    }

    private void addPlaceholder(JTextField textField, String placeholder) {
        Color placeholderColor = new Color(150, 150, 150);
        Color originalColor = Cores.COR_RODAPE;

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

    private void limparCamposPesquisa() {
        if (txtCpfBusca != null) {
            txtCpfBusca.setText("");
            addPlaceholder(txtCpfBusca, "Digite o CPF do paciente");
        }
        limparTabela();
    }

    private void abrirCadastroEvento() {
        telaCadastroEventoSentinela.limparCamposAoAbrir();
        telaCadastroEventoSentinela.setVisible(true);
    }

    public void setCpfBusca(String cpf) {
        txtCpfBusca.setText(cpf);
        buscarPaciente();
    }

    public void limparCampos() {
        limparCamposPesquisa();
        pacienteAtual = null;
    }

    public void atualizarTabelaAposCadastro(Paciente paciente) {
        if (paciente != null) {
            pacienteAtual = paciente;
            txtCpfBusca.setText(CPFUtils.formatarCPF(paciente.getCpf()));
            txtCpfBusca.setForeground(Cores.COR_RODAPE);
            atualizarTabelaEventos();
        }
    }
}
