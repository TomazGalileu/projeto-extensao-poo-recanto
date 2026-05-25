package com.ProjetoExtensao.Projeto.view;

import com.ProjetoExtensao.Projeto.infra.Cores;
import com.ProjetoExtensao.Projeto.infra.DateTimeFormatter;
import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.servicos.PacienteService;
import com.ProjetoExtensao.Projeto.utils.CPFUtils;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Component
@NoArgsConstructor
public class TelaCadastroPacientes extends JFrame {
    @Autowired
    private PacienteService pacienteService;
    
    @Autowired
    private TelaPacientes telaPacientes;

    private JTextField txtNome, txtCartaoSUS, txtNomeMae;
    private JFormattedTextField txtCPF, txtDataNasc, txtDataEntrada;
    private JCheckBox chkInativo;
    
    private Paciente pacienteEmEdicao = null; // Paciente sendo editado (null = novo cadastro)

    @PostConstruct
    public void initUI() {
        setTitle("Cadastro de Pacientes");
        setSize(900, 550);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Painel principal (cinza claro)
        JPanel panelMain = new JPanel(new GridBagLayout());
        panelMain.setBackground(Cores.COR_FUNDO_CLARO);

        GridBagConstraints grid = new GridBagConstraints();
        grid.insets = new Insets(8, 8, 8, 8);
        grid.fill = GridBagConstraints.HORIZONTAL;

        Font fonteLabel = new Font("Segoe UI", Font.BOLD, 14);
        Font fonteCampo = new Font("Segoe UI", Font.PLAIN, 13);

        // Nome completo
        grid.gridx = 0;
        grid.gridy = 0;
        panelMain.add(createLabel("Nome Completo:", fonteLabel), grid);
        grid.gridx = 1;
        txtNome = createTextField(fonteCampo);
        panelMain.add(txtNome, grid);

        // Cartão SUS
        grid.gridx = 2;
        panelMain.add(createLabel("Cartão SUS:", fonteLabel), grid);
        grid.gridx = 3;
        txtCartaoSUS = createTextField(fonteCampo);
        panelMain.add(txtCartaoSUS, grid);

        // CPF
        grid.gridx = 0;
        grid.gridy++;
        panelMain.add(createLabel("CPF:", fonteLabel), grid);
        grid.gridx = 1;
        txtCPF = createFormattedTextField("###.###.###-##", fonteCampo);
        panelMain.add(txtCPF, grid);

        // Data de nascimento
        grid.gridx = 2;
        panelMain.add(createLabel("Data de Nascimento:", fonteLabel), grid);
        grid.gridx = 3;
        txtDataNasc = createFormattedTextField("##/##/####", fonteCampo);
        panelMain.add(txtDataNasc, grid);

        // Situação
//        grid.gridx = 2;
//        panelMain.add(createLabel("Situação:", fonteLabel), grid);
//        grid.gridx = 3;
//        String[] situacoes = {
//                "Situação de rua / sem moradia",
//                "Abandono familiar",
//                "Violência física / psicológica",
//                "Negligência / maus tratos",
//                "Óbito de responsáveis"
//        };
//        comboSituacao = new JComboBox<>(situacoes);
//        comboSituacao.setFont(fonteCampo);
//        panelMain.add(comboSituacao, grid);

        // Nome da mãe
        grid.gridx = 0;
        grid.gridy++;
        panelMain.add(createLabel("Nome da mãe:", fonteLabel), grid);
        grid.gridx = 1;
        txtNomeMae = createTextField(fonteCampo);
        panelMain.add(txtNomeMae, grid);
        grid.gridx = 2;

        // Data de entrada
        grid.gridx = 2;
        panelMain.add(createLabel("Data de Entrada:", fonteLabel), grid);
        grid.gridx = 3;
        txtDataEntrada = createFormattedTextField("##/##/####", fonteCampo);
        panelMain.add(txtDataEntrada, grid);

        // Checkbox Paciente Inativo
        grid.gridx = 0;
        grid.gridy++;
        grid.gridwidth = 2;
        chkInativo = new JCheckBox("Paciente Inativo");
        chkInativo.setFont(fonteLabel);
        chkInativo.setBackground(Cores.COR_FUNDO_CLARO);
        panelMain.add(chkInativo, grid);
        grid.gridwidth = 1;

        // Botões
        grid.gridx = 1;
        grid.gridy++;
        JButton btnSalvar = createButton("Salvar");
        JButton btnLimpar = createButton("Limpar");
        JButton btnCancelar = createButton("Cancelar");

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Cores.COR_FUNDO_CLARO);
        btnPanel.add(btnSalvar);
        btnPanel.add(btnLimpar);
        btnPanel.add(btnCancelar);
        grid.gridwidth = 3;
        panelMain.add(btnPanel, grid);

        add(panelMain, BorderLayout.CENTER);

        // Eventos dos botões
        btnSalvar.addActionListener(e -> salvar());
        btnLimpar.addActionListener(e -> limparCampos());
        btnCancelar.addActionListener(e -> cancelar());
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }

    private JTextField createTextField(Font font) {
        JTextField txt = new JTextField(15);
        txt.setFont(font);
        return txt;
    }

    // Método para criar campo formatado com máscara
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


    private void salvar() {
        // Validar campos obrigatórios
        if (!validarCampos()) {
            return;
        }

        try {
            Paciente paciente;
            
            // Verificar se é edição ou novo cadastro
            if (pacienteEmEdicao != null) {
                paciente = pacienteEmEdicao; // Edição
            } else {
                paciente = new Paciente(); // Novo cadastro
            }

            paciente.setNomeCompleto(txtNome.getText());
            
            // Remover formatação do CPF usando a classe utilitária
            String cpfLimpo = CPFUtils.limparCPF(txtCPF.getText());
            paciente.setCpf(cpfLimpo);

            // Converter data de nascimento de DD/MM/AAAA para LocalDate
            String dataNascStr = txtDataNasc.getText();
            LocalDate dataNasc = converterData(dataNascStr);
            paciente.setDataNascimento(dataNasc);

            // Validar que Cartão SUS é numérico
            String cartaoSUS = txtCartaoSUS.getText().trim();
            if (!cartaoSUS.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Cartão SUS deve conter apenas números!", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            paciente.setCartaoSUS(cartaoSUS);

            paciente.setNomeMae(txtNomeMae.getText());

            // Converter data de entrada de DD/MM/AAAA para LocalDate
            String dataEntradaStr = txtDataEntrada.getText();
            LocalDate dataEntrada = converterData(dataEntradaStr);
            paciente.setDataEntrada(dataEntrada);

            // Definir status ativo/inativo
            paciente.setAtivo(!chkInativo.isSelected());

            pacienteService.salvarPaciente(paciente);

            // Atualizar a tabela de pacientes após salvar
            telaPacientes.atualizarTabela();

            // Exibir mensagem amigável
            String mensagem = (pacienteEmEdicao != null) 
                ? "✓ Paciente atualizado com sucesso!\n\nOs dados foram salvos no sistema." 
                : "✓ Paciente cadastrado com sucesso!\n\nO paciente foi adicionado ao sistema.";
            
            JOptionPane.showMessageDialog(this, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            // Limpar os campos e fechar a tela
            limparCampos();
            pacienteEmEdicao = null;
            
            // Fechar a janela e retornar para a página anterior
            dispose();
            
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Erro ao converter data. Verifique o formato DD/MM/AAAA", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar paciente: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para validar campos obrigatórios
    private boolean validarCampos() {
        // Validar nome completo
        if (txtNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome completo é obrigatório!", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar CPF usando a classe utilitária
        String cpf = CPFUtils.limparCPF(txtCPF.getText());
        if (!CPFUtils.validarTamanhoCPF(cpf)) {
            JOptionPane.showMessageDialog(this, "CPF deve conter 11 dígitos!", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar data de nascimento
        if (txtDataNasc.getText().contains("_")) {
            JOptionPane.showMessageDialog(this, "Data de nascimento é obrigatória!", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar Cartão SUS
        if (txtCartaoSUS.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cartão SUS é obrigatório!", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar data de entrada
        if (txtDataEntrada.getText().contains("_")) {
            JOptionPane.showMessageDialog(this, "Data de entrada é obrigatória!", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    // Método para converter data de DD/MM/AAAA para LocalDate
    private LocalDate converterData(String dataStr) {
        String[] partes = dataStr.split("/");
        int dia = Integer.parseInt(partes[0]);
        int mes = Integer.parseInt(partes[1]);
        int ano = Integer.parseInt(partes[2]);
        return LocalDate.of(ano, mes, dia);
    }

    private void limparCampos() {
        txtNome.setText("");
        txtCartaoSUS.setText("");
        txtCPF.setText("");
        txtDataNasc.setText("");
        txtNomeMae.setText("");
        txtDataEntrada.setText("");
        chkInativo.setSelected(false);
        pacienteEmEdicao = null;
        //comboSituacao.setSelectedIndex(0);
    }

    // Método público para limpar campos ao abrir a tela
    public void limparCamposAoAbrir() {
        limparCampos();
    }

    // Método para carregar dados de um paciente para edição
    public void carregarPacienteParaEdicao(Long pacienteId) {
        try {
            pacienteEmEdicao = pacienteService.findPacienteById(pacienteId);
            
            // Preencher os campos com os dados do paciente
            txtNome.setText(pacienteEmEdicao.getNomeCompleto());
            
            // Formatar CPF para exibição
            String cpf = pacienteEmEdicao.getCpf();
            String cpfFormatado = cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9, 11);
            txtCPF.setText(cpfFormatado);
            
            // Formatar data de nascimento para DD/MM/AAAA
            LocalDate dataNasc = pacienteEmEdicao.getDataNascimento();
            String dataNascFormatada = String.format("%02d/%02d/%04d", dataNasc.getDayOfMonth(), dataNasc.getMonthValue(), dataNasc.getYear());
            txtDataNasc.setText(dataNascFormatada);
            
            txtCartaoSUS.setText(pacienteEmEdicao.getCartaoSUS());
            txtNomeMae.setText(pacienteEmEdicao.getNomeMae());
            
            // Formatar data de entrada para DD/MM/AAAA
            LocalDate dataEntrada = pacienteEmEdicao.getDataEntrada();
            String dataEntradaFormatada = String.format("%02d/%02d/%04d", dataEntrada.getDayOfMonth(), dataEntrada.getMonthValue(), dataEntrada.getYear());
            txtDataEntrada.setText(dataEntradaFormatada);
            
            // Marcar checkbox se paciente estiver inativo
            chkInativo.setSelected(!pacienteEmEdicao.getAtivo());
            
            // Alterar título da janela
            setTitle("Editar Paciente");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar paciente: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelar() {
        limparCampos();
        setTitle("Cadastro de Pacientes");
        dispose();
    }
}
