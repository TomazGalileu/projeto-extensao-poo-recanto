package com.ProjetoExtensao.Projeto.view;

import com.ProjetoExtensao.Projeto.infra.Cores;
import com.ProjetoExtensao.Projeto.infra.DateTimeFormatter;
import com.ProjetoExtensao.Projeto.infra.PanelsFactory;
import com.ProjetoExtensao.Projeto.models.Consulta;
import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.servicos.ConsultaService;
import com.ProjetoExtensao.Projeto.servicos.PacienteService;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

@org.springframework.stereotype.Component
@NoArgsConstructor
public class TelaProntuarioPaciente extends JFrame {
    @Autowired
    private PanelsFactory panelsFactory;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private ConsultaService consultaService;

    private JLabel lblNome;
    private JLabel lblCpf;
    private JLabel lblDataNascimento;
    private JLabel lblNomeMae;
    private JLabel lblCartaoSus;
    private JLabel lblDataEntrada;
    private JLabel lblStatus;
    private DefaultTableModel modeloTabelaConsultas;

    @PostConstruct
    private void initUI() {
        setTitle("Prontuário da Paciente");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(Cores.COR_FUNDO_CLARO);
        painelPrincipal.add(panelsFactory.getHeaderPanel(), BorderLayout.NORTH);
        painelPrincipal.add(panelsFactory.getFooterPanel(), BorderLayout.SOUTH);
        painelPrincipal.add(criarPainelCentral(), BorderLayout.CENTER);

        setContentPane(painelPrincipal);
    }

    private JPanel criarPainelCentral() {
        JPanel painelCentral = new JPanel();
        painelCentral.setLayout(new BoxLayout(painelCentral, BoxLayout.Y_AXIS));
        painelCentral.setBorder(new EmptyBorder(30, 50, 30, 50));
        painelCentral.setBackground(Cores.COR_FUNDO_CLARO);

        JLabel titulo = new JLabel("Prontuário Médico");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(Cores.COR_LETRA_PAINEL);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelCentral.add(titulo);
        painelCentral.add(Box.createVerticalStrut(20));

        painelCentral.add(criarPainelDadosPaciente());
        painelCentral.add(Box.createVerticalStrut(20));
        painelCentral.add(criarPainelHistoricoConsultas());

        return painelCentral;
    }

    private JPanel criarPainelDadosPaciente() {
        JPanel painelDados = new JPanel(new GridLayout(4, 2, 20, 10));
        painelDados.setBackground(Color.WHITE);
        painelDados.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(20, 20, 20, 20)
        ));
        painelDados.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));
        painelDados.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblNome = criarCampoInfo(painelDados, "Nome completo");
        lblCpf = criarCampoInfo(painelDados, "CPF");
        lblDataNascimento = criarCampoInfo(painelDados, "Data de nascimento");
        lblNomeMae = criarCampoInfo(painelDados, "Nome da mãe");
        lblCartaoSus = criarCampoInfo(painelDados, "Cartão SUS");
        lblDataEntrada = criarCampoInfo(painelDados, "Data de entrada");
        lblStatus = criarCampoInfo(painelDados, "Status");

        return painelDados;
    }

    private JLabel criarCampoInfo(JPanel painelDestino, String titulo) {
        JPanel painelCampo = new JPanel(new BorderLayout(5, 5));
        painelCampo.setBackground(Color.WHITE);

        JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 13));
        labelTitulo.setForeground(Cores.COR_RODAPE);

        JLabel labelValor = new JLabel("-");
        labelValor.setFont(new Font("Arial", Font.PLAIN, 14));
        labelValor.setForeground(Cores.COR_LETRA_PAINEL);

        painelCampo.add(labelTitulo, BorderLayout.NORTH);
        painelCampo.add(labelValor, BorderLayout.CENTER);
        painelDestino.add(painelCampo);

        return labelValor;
    }

    private JPanel criarPainelHistoricoConsultas() {
        JPanel painelHistorico = new JPanel(new BorderLayout(10, 10));
        painelHistorico.setBackground(Color.WHITE);
        painelHistorico.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(20, 20, 20, 20)
        ));
        painelHistorico.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titulo = new JLabel("Histórico de consultas");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(Cores.COR_RODAPE);
        painelHistorico.add(titulo, BorderLayout.NORTH);

        String[] colunas = {"Data", "Hora", "Tipo", "Profissional", "Motivo", "Diagnóstico", "Anotações"};
        modeloTabelaConsultas = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabelaConsultas = new JTable(modeloTabelaConsultas);
        tabelaConsultas.setRowHeight(25);
        JScrollPane scroll = new JScrollPane(tabelaConsultas);
        scroll.setPreferredSize(new Dimension(900, 350));
        painelHistorico.add(scroll, BorderLayout.CENTER);

        return painelHistorico;
    }

    public void carregarProntuario(Long pacienteId) {
        try {
            Paciente paciente = pacienteService.findPacienteById(pacienteId);
            preencherDadosPaciente(paciente);
            preencherHistoricoConsultas(consultaService.findHistoricoConsultasByPaciente(paciente));
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro ao carregar prontuário", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherDadosPaciente(Paciente paciente) {
        lblNome.setText(paciente.getNomeCompleto());
        lblCpf.setText(paciente.getCpf());
        lblDataNascimento.setText(paciente.getDataNascimento().format(DateTimeFormatter.DATE_TIME_FORMATTER));
        lblNomeMae.setText(paciente.getNomeMae() != null ? paciente.getNomeMae() : "-");
        lblCartaoSus.setText(paciente.getCartaoSUS());
        lblDataEntrada.setText(paciente.getDataEntrada().format(DateTimeFormatter.DATE_TIME_FORMATTER));
        lblStatus.setText(paciente.getAtivo() ? "Ativo" : "Inativo");
    }

    private void preencherHistoricoConsultas(List<Consulta> consultas) {
        modeloTabelaConsultas.setRowCount(0);

        for (Consulta consulta : consultas) {
            modeloTabelaConsultas.addRow(new Object[]{
                    consulta.getData() != null ? consulta.getData().format(DateTimeFormatter.DATE_TIME_FORMATTER) : "-",
                    consulta.getHora() != null ? consulta.getHora().toString() : "-",
                    consulta.getTipoConsulta() != null ? consulta.getTipoConsulta().toString() : "-",
                    consulta.getResponsavelSaude() != null ? consulta.getResponsavelSaude().getNomeCompleto() : "-",
                    consulta.getMotivoConsulta() != null ? consulta.getMotivoConsulta() : "-",
                    consulta.getDiagnostico() != null ? consulta.getDiagnostico() : "-",
                    consulta.getAnotacoesMedico() != null ? consulta.getAnotacoesMedico() : "-"
            });
        }
    }
}
