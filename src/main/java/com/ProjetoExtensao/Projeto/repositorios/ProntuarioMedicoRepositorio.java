package com.ProjetoExtensao.Projeto.repositorios;

import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.models.ProntuarioMedico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProntuarioMedicoRepositorio
        extends JpaRepository<ProntuarioMedico, Long> {

    Optional<ProntuarioMedico> findByPaciente(Paciente paciente);
}