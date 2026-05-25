package com.ProjetoExtensao.Projeto.view;

import com.ProjetoExtensao.Projeto.infra.Cores;
import com.ProjetoExtensao.Projeto.infra.IconManager;
import com.ProjetoExtensao.Projeto.infra.PanelsFactory;
import com.ProjetoExtensao.Projeto.servicos.NavigationService;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

@org.springframework.stereotype.Component
@NoArgsConstructor
public class TelaGeral extends JFrame {
    @Autowired
    private PanelsFactory panelsFactory;
    @Autowired
    private IconManager iconManager;
    @Autowired
    private NavigationService navigationService;


    @PostConstruct
    private void initUI() {
        setTitle("Tela 2 - Geral");
        setSize(900, 700);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        getContentPane().setBackground(Cores.COR_FUNDO_ESCURO);

        add(panelsFactory.getHeaderPanel(), BorderLayout.NORTH);
        add(panelsFactory.getFooterPanel(), BorderLayout.SOUTH);
        add(createMainPanel(), BorderLayout.CENTER);
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(Cores.COR_FUNDO_CLARO);
        mainPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Painel Administrativo");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Cores.COR_LETRA_PAINEL);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0));
        statsPanel.setOpaque(false);
        statsPanel.add(createStatItem("Pacientes", "100", Cores.COR_VERMELHO_IDOSAS));
        statsPanel.add(createStatItem("Enfermaria", "20", Cores.COR_VERDE_ENFERMARIA));
        statsPanel.add(createStatItem("Visitas", "2", Cores.COR_VERDE_ENFERMARIA));

        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(statsPanel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(2, 4, 20, 20));
        gridPanel.setOpaque(false);

        // Botão Pacientes
        JButton btnPacientes = createDashboardButton("Pacientes", "pacientes.png");
        btnPacientes.addActionListener(e -> {
            navigationService.abrirTelaPacientes();
            dispose();
        });

        // Botão Consultas
        JButton btnConsultas = createDashboardButton("Consultas", "consultas.png");
        btnConsultas.addActionListener(e -> {
            navigationService.abrirTelaConsultas();
            dispose();
        });

        // Botão Eventos Sentinelas
        JButton btnEventos = createDashboardButton("Eventos Sentinelas", "eventos.png");
        btnEventos.addActionListener(e -> {
            navigationService.abrirTelaEventosSentinelas();
            dispose();
        });

        gridPanel.add(btnPacientes);
        gridPanel.add(createDashboardButton("Família", "familia.png"));
        gridPanel.add(createDashboardButton("Documentos", "documentos.png"));
        gridPanel.add(btnEventos);
        gridPanel.add(createDashboardButton("Prontuários", "prontuarios.png"));
        gridPanel.add(btnConsultas);
        gridPanel.add(createDashboardButton("Vacinas", "vacinas.png"));
        gridPanel.add(createDashboardButton("Relatórios", "relatorios.png"));

        mainPanel.add(gridPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createStatItem(String title, String value, Color valueColor) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        titleLabel.setForeground(Cores.COR_LETRA_PAINEL);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 20));
        valueLabel.setForeground(valueColor);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(valueLabel);

        return panel;
    }

    private JButton createDashboardButton(String text, String iconName) {
        ImageIcon icon = iconManager.createScaledIcon("/images/" + iconName, 48, 48);

        JButton button = new JButton(text, icon);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setForeground(Cores.COR_LETRA_PAINEL);
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(new Color(0xDDDDDD)));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }
}
