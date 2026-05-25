package com.ProjetoExtensao.Projeto.repositorios;

import com.ProjetoExtensao.Projeto.models.ResponsavelSaude;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResponsavelRepositorio extends JpaRepository<ResponsavelSaude, Long> {
    Optional<ResponsavelSaude> findByEmail(String email);

    Optional<ResponsavelSaude> findByNomeCompleto(String nome);
}
