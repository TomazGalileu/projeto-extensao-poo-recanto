package com.ProjetoExtensao.Projeto;

import com.ProjetoExtensao.Projeto.servicos.NavigationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ProjetoApplication {

	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "false");

		ApplicationContext context = SpringApplication.run(ProjetoApplication.class, args);

		NavigationService navigationService = context.getBean(NavigationService.class);
		navigationService.abrirTelaLogin();
	}
}
