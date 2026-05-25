package com.ProjetoExtensao.Projeto.repositorios;

import com.ProjetoExtensao.Projeto.models.EventoSentinela;
import com.ProjetoExtensao.Projeto.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventoSentinelaRepositorio extends JpaRepository<EventoSentinela, Long> {
    List<EventoSentinela> findByPaciente(Paciente paciente);
    
    List<EventoSentinela> findByPacienteOrderByDataEventoDesc(Paciente paciente);
}
