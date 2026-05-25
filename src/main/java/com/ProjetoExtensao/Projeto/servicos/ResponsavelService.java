package com.ProjetoExtensao.Projeto.servicos;

import com.ProjetoExtensao.Projeto.models.ResponsavelSaude;
import com.ProjetoExtensao.Projeto.repositorios.ResponsavelRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ResponsavelService {
    private final ResponsavelRepositorio responsavelRepositorio;

    public ResponsavelSaude findResponsavelByEmail(String email) {
        return responsavelRepositorio.findByEmail(email).orElse(null);
    }

    public List<ResponsavelSaude> findAllResponsaveis() {
        return responsavelRepositorio.findAll();
    }

    public ResponsavelSaude findResponsavelByNome(String nome) {
        return responsavelRepositorio.findByNomeCompleto(nome).orElseThrow(() -> new RuntimeException("Médico não encontrado"));
    }
}
