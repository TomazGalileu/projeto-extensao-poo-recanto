package com.ProjetoExtensao.Projeto.view;

import com.ProjetoExtensao.Projeto.infra.Cores;
import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.servicos.PacienteService;
import com.ProjetoExtensao.Projeto.servicos.VacinaService;
import com.ProjetoExtensao.Projeto.utils.CPFUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class TelaCadastroVacina extends JFrame {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private VacinaService vacinaService;

    private JTextField txtCpf;
    private JTextField txtNomeVacina;
    private JTextField txtDataAplicacao;

    public TelaCadastroVacina() {
        setTitle("Cadastrar Vacina");
        setSize(550, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        criarInterface();
    }

    private void criarInterface() {
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBackground(Cores.COR_FUNDO_CLARO);
        painelPrincipal.setBorder(new EmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel titulo = new JLabel("Cadastro de Vacina");
        titulo.setFont(new Font("Arial", Font.BOLD, 26));
        titulo.setForeground(Cores.COR_LETRA_PAINEL);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        painelPrincipal.add(titulo, gbc);

        gbc.gridwidth = 1;

        gbc.gridy = 1;
        gbc.gridx = 0;
        painelPrincipal.add(new JLabel("CPF da paciente:"), gbc);

        txtCpf = new JTextField();
        txtCpf.setFont(new Font("Arial", Font.PLAIN, 16));
        CPFUtils.aplicarFormatacaoAutomatica(txtCpf);

        gbc.gridx = 1;
        painelPrincipal.add(txtCpf, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        painelPrincipal.add(new JLabel("Nome da vacina:"), gbc);

        txtNomeVacina = new JTextField();
        txtNomeVacina.setFont(new Font("Arial", Font.PLAIN, 16));

        gbc.gridx = 1;
        painelPrincipal.add(txtNomeVacina, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        painelPrincipal.add(new JLabel("Data de aplicação:"), gbc);

        txtDataAplicacao = new JTextField();
        txtDataAplicacao.setFont(new Font("Arial", Font.PLAIN, 16));
        txtDataAplicacao.setToolTipText("Use o formato dd/MM/yyyy");

        gbc.gridx = 1;
        painelPrincipal.add(txtDataAplicacao, gbc);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.setBackground(Cores.COR_FUNDO_CLARO);

        JButton btnCancelar = new JButton("Cancelar");
        JButton btnSalvar = new JButton("Salvar");

        btnSalvar.setBackground(Cores.COR_RODAPE);
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFocusPainted(false);

        btnCancelar.addActionListener(e -> dispose());
        btnSalvar.addActionListener(e -> salvarVacina());

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnSalvar);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        painelPrincipal.add(painelBotoes, gbc);

        add(painelPrincipal);
    }

    private void salvarVacina() {
        String cpf = CPFUtils.limparCPF(txtCpf.getText());
        String nomeVacina = txtNomeVacina.getText().trim();
        String dataTexto = txtDataAplicacao.getText().trim();

        if (!CPFUtils.validarTamanhoCPF(cpf)) {
            mostrarAviso("Digite um CPF válido com 11 dígitos.");
            return;
        }

        if (nomeVacina.isEmpty()) {
            mostrarAviso("Digite o nome da vacina.");
            return;
        }

        if (dataTexto.isEmpty()) {
            mostrarAviso("Digite a data de aplicação.");
            return;
        }

        try {
            Paciente paciente =
                    pacienteService.findPacienteByCpf(cpf);

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd/MM/yyyy");

            LocalDate dataAplicacao =
                    LocalDate.parse(dataTexto, formatter);

            vacinaService.salvarVacina(
                    nomeVacina,
                    dataAplicacao,
                    paciente
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Vacina cadastrada com sucesso.",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limparCampos();
            dispose();

        } catch (DateTimeParseException ex) {
            mostrarAviso(
                    "Data inválida. Utilize o formato dd/MM/yyyy."
            );

        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível cadastrar a vacina:\n"
                            + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void mostrarAviso(String mensagem) {
        JOptionPane.showMessageDialog(
                this,
                mensagem,
                "Aviso",
                JOptionPane.WARNING_MESSAGE
        );
    }

    public void limparCampos() {
        txtCpf.setText("");
        txtNomeVacina.setText("");
        txtDataAplicacao.setText("");
    }
}