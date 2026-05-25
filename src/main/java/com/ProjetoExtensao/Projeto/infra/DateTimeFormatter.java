package com.ProjetoExtensao.Projeto.infra;

import org.springframework.stereotype.Component;

@Component
public class DateTimeFormatter {
    public static final java.time.format.DateTimeFormatter DATE_TIME_FORMATTER = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final java.time.format.DateTimeFormatter TIME_FORMATTER = java.time.format.DateTimeFormatter.ofPattern("HH:mm");
}
