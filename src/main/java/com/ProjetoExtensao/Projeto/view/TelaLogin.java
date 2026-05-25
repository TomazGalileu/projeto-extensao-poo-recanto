package com.ProjetoExtensao.Projeto.view;

import com.ProjetoExtensao.Projeto.infra.Cores;
import com.ProjetoExtensao.Projeto.infra.PanelsFactory;
import com.ProjetoExtensao.Projeto.models.ResponsavelSaude;
import com.ProjetoExtensao.Projeto.servicos.NavigationService;
import com.ProjetoExtensao.Projeto.servicos.ResponsavelService;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
@NoArgsConstructor
public class TelaLogin extends JFrame {
    @Autowired
    private ResponsavelService responsavelService;

    @Autowired
    private PanelsFactory panelsFactory;

    @Autowired
    private NavigationService navigationService;

    private JLabel erroEmail;
    private JLabel erroSenha;

    private JTextField txtEmail;
    private JPasswordField txtSenha;

    @PostConstruct
    private void initUI() {
        setTitle("Login - Recanto do Sagrado Coração");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Cores.COR_FUNDO_ESCURO);

        add(panelsFactory.getFooterPanel(), BorderLayout.SOUTH);
        add(createMainPanel(), BorderLayout.CENTER);
    }

    private JPanel createMainPanel() {
        JPanel painelCentral = new JPanel();
        painelCentral.setLayout(new BoxLayout(painelCentral, BoxLayout.Y_AXIS));
        painelCentral.setPreferredSize(new Dimension(350, 300));
        painelCentral.setBackground(Cores.COR_FUNDO_CLARO);
        painelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("Realize o login");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        painelCentral.add(lblTitulo);
        painelCentral.add(Box.createRigidArea(new Dimension(0, 20)));

        // GRUPO USUÁRIO
        JPanel grupoEmail = new JPanel();
        grupoEmail.setLayout(new BoxLayout(grupoEmail, BoxLayout.Y_AXIS));
        grupoEmail.setBackground(Cores.COR_FUNDO_CLARO);

        JLabel lblEmail = new JLabel("Email:");
        grupoEmail.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        grupoEmail.add(txtEmail);

        erroEmail = new JLabel("");
        erroEmail.setForeground(Color.RED);
        erroEmail.setFont(new Font("SansSerif", Font.PLAIN, 11));
        erroEmail.setVisible(false);
        grupoEmail.add(erroEmail);

        painelCentral.add(grupoEmail);
        painelCentral.add(Box.createRigidArea(new Dimension(0, 15)));

        // GRUPO SENHA
        JPanel grupoSenha = new JPanel();
        grupoSenha.setLayout(new BoxLayout(grupoSenha, BoxLayout.Y_AXIS));
        grupoSenha.setBackground(Cores.COR_FUNDO_CLARO);

        JLabel lblSenha = new JLabel("Senha:");
        grupoSenha.add(lblSenha);

        txtSenha = new JPasswordField();
        txtSenha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        grupoSenha.add(txtSenha);

        erroSenha = new JLabel("");
        erroSenha.setForeground(Color.RED);
        erroSenha.setFont(new Font("SansSerif", Font.PLAIN, 11));
        erroSenha.setVisible(false);
        grupoSenha.add(erroSenha);

        painelCentral.add(grupoSenha);
        painelCentral.add(Box.createRigidArea(new Dimension(0, 20)));

        // Botões
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.setOpaque(false);

        JButton btnLogar = new JButton("Logar");
        btnLogar.setPreferredSize(new Dimension(100, 30));
        btnLogar.setBackground(Cores.COR_RODAPE);
        btnLogar.setForeground(Color.WHITE);
        btnPanel.add(btnLogar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setPreferredSize(new Dimension(100, 30));
        btnCancelar.setBackground(Cores.COR_RODAPE);
        btnCancelar.setForeground(Color.WHITE);
        btnPanel.add(btnCancelar);

        painelCentral.add(btnPanel);

        // Wrapper para centralizar tudo
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(Color.decode("#dcdcdc"));
        wrapper.add(painelCentral);

        // Listeners
        btnLogar.addActionListener(e -> processarLogin());
        btnCancelar.addActionListener(e -> dispose());

        return wrapper;
    }

    private void processarLogin() {
        limparErros();

        String email = txtEmail.getText();
        ResponsavelSaude responsavelSaude = responsavelService.findResponsavelByEmail(email);

        if (responsavelSaude != null) {
            String senha = new String(txtSenha.getPassword());

            if (responsavelSaude.getSenha().equals(senha)) {
                navigationService.abrirTelaGeral();
                dispose();
            } else {
                mostrarErroSenha("Senha inválida");
            }
        } else {
            mostrarErroEmail("Email inválido");
        }
    }

    public void mostrarErroEmail(String msg) {
        erroEmail.setText(msg);
        erroEmail.setVisible(true);
    }

    public void mostrarErroSenha(String msg) {
        erroSenha.setText(msg);
        erroSenha.setVisible(true);
    }

    public void limparErros() {
        erroEmail.setVisible(false);
        erroSenha.setVisible(false);
    }
}
