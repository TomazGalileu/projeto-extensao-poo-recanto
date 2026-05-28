package com.ProjetoExtensao.Projeto.view;

import com.ProjetoExtensao.Projeto.infra.Cores;
import com.ProjetoExtensao.Projeto.servicos.NavigationService;
import com.ProjetoExtensao.Projeto.servicos.RelatorioService;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

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
    private JTextArea areaRelatorio;

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

        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBusca.setOpaque(false);

        JLabel labelCpf = new JLabel("CPF:");
        labelCpf.setFont(new Font("Arial", Font.PLAIN, 14));

        campoCpf = new JTextField(18);

        JButton botaoGerar = new JButton("Gerar Relatorio");
        botaoGerar.addActionListener(e -> gerarRelatorio());

        painelBusca.add(labelCpf);
        painelBusca.add(campoCpf);
        painelBusca.add(botaoGerar);

        areaRelatorio = new JTextArea();
        areaRelatorio.setEditable(false);
        areaRelatorio.setFont(new Font("Monospaced", Font.PLAIN, 14));
        areaRelatorio.setLineWrap(true);
        areaRelatorio.setWrapStyleWord(true);

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
}