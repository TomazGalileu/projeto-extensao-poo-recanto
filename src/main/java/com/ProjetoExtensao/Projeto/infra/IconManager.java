package com.ProjetoExtensao.Projeto.infra;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

@Component
public class IconManager {
    public ImageIcon createIcon(String path) {
        URL url = IconManager.class.getResource(path);
        if (url == null) {
            System.err.println("Erro ao carregar o recurso: " + path);
            return null;
        }
        return new ImageIcon(url);
    }

    public ImageIcon createScaledIcon(String path, int width, int height) {
        ImageIcon originalIcon = createIcon(path);
        if (originalIcon == null) {
            return null;
        }
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}
