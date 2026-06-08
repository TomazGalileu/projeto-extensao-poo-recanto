package com.ProjetoExtensao.Projeto.view;

import com.ProjetoExtensao.Projeto.infra.Cores;
import com.ProjetoExtensao.Projeto.infra.PanelsFactory;
import com.ProjetoExtensao.Projeto.servicos.EventoSentinelaService;
import com.ProjetoExtensao.Projeto.servicos.NavigationService;
import com.ProjetoExtensao.Projeto.utils.EventosOcorridos;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

@Component
@NoArgsConstructor
public class TelaIndicadores extends JFrame {

    @Autowired
    private PanelsFactory panelsFactory;

    @Autowired
    private EventoSentinelaService eventoSentinelaService;

    @Autowired
    private NavigationService navigationService;

    private JLabel lblMortalidade;
    private JLabel lblDiarreia;
    private JLabel lblEscabiose;
    private JLabel lblDesidratacao;
    private JLabel lblUlceraPressao;
    private JLabel lblDesnutricao;

    @PostConstruct
    public void initUI() {
        setTitle("Recanto do Sagrado Coração - Indicadores");
        setSize(1200, 800);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(panelsFactory.getHeaderPanel(), BorderLayout.NORTH);
        add(panelsFactory.getFooterPanel(), BorderLayout.SOUTH);

        JPanel painelCentral = new JPanel();
        painelCentral.setLayout(
                new BoxLayout(painelCentral, BoxLayout.Y_AXIS)
        );
        painelCentral.setBackground(Cores.COR_FUNDO_CLARO);
        painelCentral.setBorder(
                new EmptyBorder(20, 40, 20, 40)
        );

        JLabel titulo = new JLabel("Indicadores Institucionais");
        titulo.setFont(new Font("Arial", Font.PLAIN, 36));
        titulo.setForeground(Cores.COR_LETRA_PAINEL);
        titulo.setAlignmentX(JComponent.LEFT_ALIGNMENT);

        JButton btnRelatorioIndividual =
            new JButton("Relatório Individual");

        btnRelatorioIndividual.setFont(
            new Font("Arial", Font.BOLD, 14)
        );
        btnRelatorioIndividual.setBackground(
            Cores.COR_RODAPE
        );
        btnRelatorioIndividual.setForeground(Color.WHITE);
        btnRelatorioIndividual.setFocusPainted(false);

        btnRelatorioIndividual.addActionListener(e -> {
            navigationService.abrirTelaRelatorioIndividual();
        });

        JPanel painelTitulo =
            new JPanel(new BorderLayout());

        painelTitulo.setBackground(
            Cores.COR_FUNDO_CLARO
        );

        painelTitulo.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 50)
        );

        painelTitulo.setAlignmentX(
            JComponent.LEFT_ALIGNMENT
        );

        painelTitulo.add(titulo, BorderLayout.WEST);
        painelTitulo.add(
            btnRelatorioIndividual,
            BorderLayout.EAST
        );

        painelCentral.add(painelTitulo);

        painelCentral.add(
                Box.createRigidArea(new Dimension(0, 30))
        );

        JPanel painelIndicadores = new JPanel(
                new GridLayout(2, 3, 20, 20)
        );
        painelIndicadores.setBackground(Cores.COR_FUNDO_CLARO);
        painelIndicadores.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        painelIndicadores.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 400)
        );

        lblMortalidade = criarCardIndicador(
                "Taxa de Mortalidade"
        );

        lblDiarreia = criarCardIndicador(
                "Incidência de Diarreia"
        );

        lblEscabiose = criarCardIndicador(
                "Incidência de Escabiose"
        );

        lblDesidratacao = criarCardIndicador(
                "Incidência de Desidratação"
        );

        lblUlceraPressao = criarCardIndicador(
                "Prevalência de Úlcera por Pressão"
        );

        lblDesnutricao = criarCardIndicador(
                "Prevalência de Desnutrição"
        );

        painelIndicadores.add(
                criarPainelCard("Mortalidade", lblMortalidade)
        );

        painelIndicadores.add(
                criarPainelCard("Diarreia", lblDiarreia)
        );

        painelIndicadores.add(
                criarPainelCard("Escabiose", lblEscabiose)
        );

        painelIndicadores.add(
                criarPainelCard("Desidratação", lblDesidratacao)
        );

        painelIndicadores.add(
                criarPainelCard(
                        "Úlcera por Pressão",
                        lblUlceraPressao
                )
        );

        painelIndicadores.add(
                criarPainelCard("Desnutrição", lblDesnutricao)
        );

        painelCentral.add(painelIndicadores);

        add(painelCentral, BorderLayout.CENTER);

        addComponentListener(
                new java.awt.event.ComponentAdapter() {
                    @Override
                    public void componentShown(
                            java.awt.event.ComponentEvent e
                    ) {
                        atualizarIndicadores();
                    }
                }
        );

        atualizarIndicadores();
    }

    private JLabel criarCardIndicador(String nome) {
        JLabel label = new JLabel("0,00%");
        label.setFont(new Font("Arial", Font.BOLD, 28));
        label.setForeground(Cores.COR_RODAPE);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        return label;
    }

    private JPanel criarPainelCard(
            String titulo,
            JLabel labelPercentual
    ) {
        JPanel painel = new JPanel();
        painel.setLayout(
                new BoxLayout(painel, BoxLayout.Y_AXIS)
        );
        painel.setBackground(Color.WHITE);
        painel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(0xDDDDDD)
                        ),
                        new EmptyBorder(25, 20, 25, 20)
                )
        );

        JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setFont(
                new Font("Arial", Font.BOLD, 17)
        );
        labelTitulo.setForeground(
                Cores.COR_LETRA_PAINEL
        );
        labelTitulo.setAlignmentX(
                JComponent.CENTER_ALIGNMENT
        );

        labelPercentual.setAlignmentX(
                JComponent.CENTER_ALIGNMENT
        );

        painel.add(
                Box.createVerticalGlue()
        );
        painel.add(labelTitulo);
        painel.add(
                Box.createRigidArea(
                        new Dimension(0, 20)
                )
        );
        painel.add(labelPercentual);
        painel.add(
                Box.createVerticalGlue()
        );

        return painel;
    }

    private void atualizarIndicadores() {
        try {
            double mortalidade =
                    eventoSentinelaService
                            .calcularPercentualPorTipoEvento(
                                    EventosOcorridos.OBITO
                            );

            double diarreia =
                    eventoSentinelaService
                            .calcularPercentualPorTipoEvento(
                                    EventosOcorridos.DIARREIA
                            );

            double escabiose =
                    eventoSentinelaService
                            .calcularPercentualPorTipoEvento(
                                    EventosOcorridos.ESCABIOSE
                            );

            double desidratacao =
                    eventoSentinelaService
                            .calcularPercentualPorTipoEvento(
                                    EventosOcorridos.DESIDRATACAO
                            );

            double ulceraPressao =
                    eventoSentinelaService
                            .calcularPercentualPorTipoEvento(
                                    EventosOcorridos.ULCERA_POR_PRESSAO
                            );

            double desnutricao =
                    eventoSentinelaService
                            .calcularPercentualPorTipoEvento(
                                    EventosOcorridos.DESNUTRICAO
                            );

            lblMortalidade.setText(
                    formatarPercentual(mortalidade)
            );

            lblDiarreia.setText(
                    formatarPercentual(diarreia)
            );

            lblEscabiose.setText(
                    formatarPercentual(escabiose)
            );

            lblDesidratacao.setText(
                    formatarPercentual(desidratacao)
            );

            lblUlceraPressao.setText(
                    formatarPercentual(ulceraPressao)
            );

            lblDesnutricao.setText(
                    formatarPercentual(desnutricao)
            );

        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao calcular os indicadores:\n"
                            + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private String formatarPercentual(double percentual) {
        return String.format("%.2f%%", percentual);
    }
}