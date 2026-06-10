package com.ProjetoExtensao.Projeto.repositorios;

import com.ProjetoExtensao.Projeto.models.Prescricao;
import com.ProjetoExtensao.Projeto.models.ProntuarioMedico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescricaoRepositorio
        extends JpaRepository<Prescricao, Long> {

    List<Prescricao> findByProntuarioMedicoOrderByDataInicioDesc(
            ProntuarioMedico prontuarioMedico
    );

    List<Prescricao> findByProntuarioMedicoAndAtivaTrueOrderByDataInicioDesc(
            ProntuarioMedico prontuarioMedico
    );
}