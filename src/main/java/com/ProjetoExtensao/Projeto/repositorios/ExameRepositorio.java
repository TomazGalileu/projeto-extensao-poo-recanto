package com.ProjetoExtensao.Projeto.repositorios;

import com.ProjetoExtensao.Projeto.models.Exame;
import com.ProjetoExtensao.Projeto.models.ProntuarioMedico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExameRepositorio extends JpaRepository<Exame, Long> {

    List<Exame> findByProntuarioMedicoOrderByDataSolicitacaoDesc(
            ProntuarioMedico prontuarioMedico
    );
}