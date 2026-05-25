package com.ProjetoExtensao.Projeto.view;

import com.ProjetoExtensao.Projeto.infra.Cores;
import com.ProjetoExtensao.Projeto.models.EventoSentinela;
import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.servicos.EventoSentinelaService;
import com.ProjetoExtensao.Projeto.servicos.PacienteService;
import com.ProjetoExtensao.Projeto.utils.CPFUtils;
import com.ProjetoExtensao.Projeto.utils.EventosOcorridos;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.time.LocalDate;

@Component
@NoArgsConstructor
public class TelaCadastroEventoSentinela extends JFrame {
    @Autowired
    private PacienteService pacienteService;
    
    @Autowired
    private EventoSentinelaService eventoSentinelaService;
    
    @Lazy
    @Autowired
    private TelaEventosSentinelas telaEventosSentinelas;

    private JFormattedTextField txtCpf;
    private JTextField txtNomePaciente;
    private JComboBox<String> comboEvento;
    private JTextField txtDescricao;
    private JFormattedTextField txtDataEvento;
    private Paciente pacienteAtual = null;

    @PostConstruct
    public void initUI() {
        setTitle("Cadastro de Evento Sentinela");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Adicionar listener para limpar campos ao fechar
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                limparCampos();
            }
        });

        JPanel panelMain = new JPanel(new GridBagLayout());
        panelMain.setBackground(Cores.COR_FUNDO_CLARO);
        panelMain.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints grid = new GridBagConstraints();
        grid.insets = new Insets(10, 10, 10, 10);
        grid.fill = GridBagConstraints.HORIZONTAL;

        Font fonteLabel = new Font("Segoe UI", Font.BOLD, 14);
        Font fonteCampo = new Font("Segoe UI", Font.PLAIN, 13);

        // CPF do Paciente
        grid.gridx = 0;
        grid.gridy = 0;
        panelMain.add(createLabel("CPF do Paciente:", fonteLabel), grid);
        
        grid.gridx = 1;
        JPanel panelCpf = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelCpf.setBackground(Cores.COR_FUNDO_CLARO);
        
        txtCpf = createFormattedTextField("###.###.###-##", fonteCampo);
        JButton btnBuscarCpf = new JButton("Buscar");
        btnBuscarCpf.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnBuscarCpf.setBackground(Cores.COR_RODAPE);
        btnBuscarCpf.setForeground(Color.WHITE);
        btnBuscarCpf.setFocusPainted(false);
        btnBuscarCpf.addActionListener(e -> buscarPacientePorCpf());
        
        panelCpf.add(txtCpf);
        panelCpf.add(btnBuscarCpf);
        panelMain.add(panelCpf, grid);

        // Nome do Paciente (somente leitura)
        grid.gridx = 0;
        grid.gridy++;
        panelMain.add(createLabel("Nome do Paciente:", fonteLabel), grid);
        
        grid.gridx = 1;
        txtNomePaciente = new JTextField(20);
        txtNomePaciente.setFont(fonteCampo);
        txtNomePaciente.setEditable(false);
        txtNomePaciente.setBackground(new Color(240, 240, 240));
        panelMain.add(txtNomePaciente, grid);

        // Evento Ocorrido
        grid.gridx = 0;
        grid.gridy++;
        panelMain.add(createLabel("Evento Ocorrido:", fonteLabel), grid);
        
        grid.gridx = 1;
        String[] eventos = {
            "Selecione um evento",
            "Tentativa de Suicídio",
            "Quedas",
            "Diarreia",
            "Escabiose",
            "Desidratação",
            "Úlcera por Pressão",
            "Desnutrição",
            "Óbito",
            "Pressão Arterial",
            "Glicemia",
            "Temperatura"
        };
        comboEvento = new JComboBox<>(eventos);
        comboEvento.setFont(fonteCampo);
        panelMain.add(comboEvento, grid);

        // Descrição
        grid.gridx = 0;
        grid.gridy++;
        panelMain.add(createLabel("Descrição:", fonteLabel), grid);
        
        grid.gridx = 1;
        txtDescricao = new JTextField(20);
        txtDescricao.setFont(fonteCampo);
        panelMain.add(txtDescricao, grid);

        // Data do Evento
        grid.gridx = 0;
        grid.gridy++;
        panelMain.add(createLabel("Data do Evento:", fonteLabel), grid);
        
        grid.gridx = 1;
        txtDataEvento = createFormattedTextField("##/##/####", fonteCampo);
        panelMain.add(txtDataEvento, grid);

        // Botões
        grid.gridx = 0;
        grid.gridy++;
        grid.gridwidth = 2;
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBotoes.setBackground(Cores.COR_FUNDO_CLARO);
        
        JButton btnSalvar = createButton("Salvar");
        JButton btnLimpar = createButton("Limpar");
        JButton btnCancelar = createButton("Cancelar");
        
        btnSalvar.addActionListener(e -> salvarEvento());
        btnLimpar.addActionListener(e -> limparCampos());
        btnCancelar.addActionListener(e -> {
            limparCampos();
            dispose();
        });
        
        panelBotoes.add(btnSalvar);
        panelBotoes.add(btnLimpar);
        panelBotoes.add(btnCancelar);
        
        panelMain.add(panelBotoes, grid);

        add(panelMain, BorderLayout.CENTER);
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }

    private JFormattedTextField createFormattedTextField(String mask, Font font) {
        JFormattedTextField txt = null;
        try {
            MaskFormatter formatter = new MaskFormatter(mask);
            formatter.setPlaceholderCharacter('_');
            txt = new JFormattedTextField(formatter);
            txt.setColumns(15);
            txt.setFont(font);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return txt;
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(Cores.COR_RODAPE);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return btn;
    }

    private void buscarPacientePorCpf() {
        String cpfLimpo = CPFUtils.limparCPF(txtCpf.getText());
        
        if (!CPFUtils.validarTamanhoCPF(cpfLimpo)) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, digite um CPF válido com 11 dígitos.", 
                "CPF Inválido", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            pacienteAtual = pacienteService.findPacienteByCpf(cpfLimpo);
            txtNomePaciente.setText(pacienteAtual.getNomeCompleto());
        } catch (RuntimeException e) {
            pacienteAtual = null;
            txtNomePaciente.setText("");
            JOptionPane.showMessageDialog(this, 
                "Paciente não encontrado. Verifique o CPF digitado.", 
                "Paciente Não Encontrado", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void salvarEvento() {
        if (!validarCampos()) {
            return;
        }

        try {
            EventosOcorridos eventoSelecionado = converterEventoSelecionado();
            LocalDate dataEvento = converterData(txtDataEvento.getText());

            EventoSentinela evento = new EventoSentinela(
                pacienteAtual,
                eventoSelecionado,
                txtDescricao.getText().trim(),
                dataEvento
            );

            eventoSentinelaService.salvarEvento(evento);

            JOptionPane.showMessageDialog(this, 
                "✓ Evento cadastrado com sucesso!\n\nO evento foi registrado no sistema.", 
                "Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);

            // Atualizar a tabela na tela de eventos sentinelas
            telaEventosSentinelas.atualizarTabelaAposCadastro(pacienteAtual);

            limparCampos();
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao salvar evento: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCampos() {
        if (pacienteAtual == null) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, busque um paciente válido pelo CPF.", 
                "Paciente Não Selecionado", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (comboEvento.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, selecione um evento.", 
                "Evento Não Selecionado", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (txtDescricao.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, preencha a descrição do evento.", 
                "Descrição Obrigatória", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (txtDataEvento.getText().contains("_")) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, preencha a data do evento.", 
                "Data Obrigatória", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private EventosOcorridos converterEventoSelecionado() {
        int index = comboEvento.getSelectedIndex();
        EventosOcorridos[] eventos = {
            null,
            EventosOcorridos.TENTATIVA_DE_SUICIDIO,
            EventosOcorridos.QUEDAS,
            EventosOcorridos.DIARREIA,
            EventosOcorridos.ESCABIOSE,
            EventosOcorridos.DESIDRATACAO,
            EventosOcorridos.ULCERA_POR_PRESSAO,
            EventosOcorridos.DESNUTRICAO,
            EventosOcorridos.OBITO,
            EventosOcorridos.PRESSAO_ARTERIAL,
            EventosOcorridos.GLICEMIA,
            EventosOcorridos.TEMPERATURA
        };
        return eventos[index];
    }

    private LocalDate converterData(String dataStr) {
        String[] partes = dataStr.split("/");
        int dia = Integer.parseInt(partes[0]);
        int mes = Integer.parseInt(partes[1]);
        int ano = Integer.parseInt(partes[2]);
        return LocalDate.of(ano, mes, dia);
    }

    public void limparCampos() {
        txtCpf.setText("");
        txtNomePaciente.setText("");
        comboEvento.setSelectedIndex(0);
        txtDescricao.setText("");
        txtDataEvento.setText("");
        pacienteAtual = null;
    }

    public void limparCamposAoAbrir() {
        limparCampos();
    }
}
