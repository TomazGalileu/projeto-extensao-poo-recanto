package com.ProjetoExtensao.Projeto.utils;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Classe utilitária para trabalhar com CPF
 * Fornece métodos para validação, formatação e limpeza de CPF
 */
public class CPFUtils {

    /**
     * Remove toda a formatação do CPF, deixando apenas os números
     * Exemplo: "123.456.789-01" vira "12345678901"
     * 
     * @param cpf CPF formatado ou não
     * @return CPF apenas com números
     */
    public static String limparCPF(String cpf) {
        if (cpf == null) {
            return "";
        }
        return cpf.replaceAll("[^0-9]", "");
    }

    /**
     * Formata um CPF adicionando pontos e traço
     * Exemplo: "12345678901" vira "123.456.789-01"
     * 
     * @param cpf CPF sem formatação (apenas números)
     * @return CPF formatado
     */
    public static String formatarCPF(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            return "";
        }
        
        // Remove qualquer formatação existente
        String cpfLimpo = limparCPF(cpf);
        
        // Se não tiver 11 dígitos, retorna como está
        if (cpfLimpo.length() != 11) {
            return cpf;
        }
        
        // Formata: 123.456.789-01
        return cpfLimpo.substring(0, 3) + "." +
               cpfLimpo.substring(3, 6) + "." +
               cpfLimpo.substring(6, 9) + "-" +
               cpfLimpo.substring(9, 11);
    }

    /**
     * Valida se o CPF tem 11 dígitos
     * 
     * @param cpf CPF para validar
     * @return true se o CPF tem 11 dígitos, false caso contrário
     */
    public static boolean validarTamanhoCPF(String cpf) {
        String cpfLimpo = limparCPF(cpf);
        return cpfLimpo.length() == 11;
    }

    /**
     * Aplica formatação automática de CPF em um campo de texto
     * Enquanto o usuário digita, o CPF é formatado automaticamente
     * Aceita apenas números e limita a 11 dígitos
     * 
     * @param textField Campo de texto onde será aplicada a formatação
     */
    public static void aplicarFormatacaoAutomatica(JTextField textField) {
        AbstractDocument doc = (AbstractDocument) textField.getDocument();
        doc.setDocumentFilter(new DocumentFilter() {
            
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) 
                    throws BadLocationException {
                // Aceita apenas números
                String newStr = string.replaceAll("[^0-9]", "");
                if (newStr.isEmpty()) return;
                
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String textSemFormatacao = currentText.replaceAll("[^0-9]", "");
                
                // Limita a 11 dígitos
                if (textSemFormatacao.length() + newStr.length() <= 11) {
                    super.insertString(fb, offset, newStr, attr);
                    formatarTextoNoCampo(fb);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) 
                    throws BadLocationException {
                // Aceita apenas números
                String newStr = text.replaceAll("[^0-9]", "");
                
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String textSemFormatacao = currentText.replaceAll("[^0-9]", "");
                
                // Calcular quantos dígitos serão removidos
                String textoRemovido = currentText.substring(offset, Math.min(offset + length, currentText.length()));
                int digitosRemovidos = textoRemovido.replaceAll("[^0-9]", "").length();
                
                // Limita a 11 dígitos
                if (textSemFormatacao.length() - digitosRemovidos + newStr.length() <= 11) {
                    super.replace(fb, offset, length, newStr, attrs);
                    formatarTextoNoCampo(fb);
                }
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                super.remove(fb, offset, length);
                formatarTextoNoCampo(fb);
            }

            /**
             * Formata o texto no campo adicionando pontos e traço
             */
            private void formatarTextoNoCampo(FilterBypass fb) throws BadLocationException {
                String text = fb.getDocument().getText(0, fb.getDocument().getLength());
                String apenasNumeros = text.replaceAll("[^0-9]", "");
                
                StringBuilder formatted = new StringBuilder();
                for (int i = 0; i < apenasNumeros.length(); i++) {
                    // Adiciona ponto após o 3º e 6º dígito
                    if (i == 3 || i == 6) {
                        formatted.append(".");
                    } 
                    // Adiciona traço após o 9º dígito
                    else if (i == 9) {
                        formatted.append("-");
                    }
                    formatted.append(apenasNumeros.charAt(i));
                }
                
                // Atualiza o campo apenas se o texto mudou
                if (!text.equals(formatted.toString())) {
                    fb.remove(0, fb.getDocument().getLength());
                    fb.insertString(0, formatted.toString(), null);
                }
            }
        });
    }
}
