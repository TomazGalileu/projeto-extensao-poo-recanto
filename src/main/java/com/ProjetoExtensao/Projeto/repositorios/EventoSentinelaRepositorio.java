package com.ProjetoExtensao.Projeto.repositorios;

import com.ProjetoExtensao.Projeto.models.EventoSentinela;
import com.ProjetoExtensao.Projeto.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ProjetoExtensao.Projeto.utils.EventosOcorridos;

import java.util.List;
import java.time.LocalDate;

public interface EventoSentinelaRepositorio extends JpaRepository<EventoSentinela, Long> {
    List<EventoSentinela> findByPaciente(Paciente paciente);
    
    List<EventoSentinela> findByPacienteOrderByDataEventoDesc(Paciente paciente);

    // Buscar eventos de um paciente dentro de um período
    List<EventoSentinela> findByPacienteAndDataEventoBetweenOrderByDataEventoDesc(
        Paciente paciente,
        LocalDate dataInicio,
        LocalDate dataFim
    );

    // Busca todos os eventos ocorridos dentro de um período
    List<EventoSentinela> findByDataEventoBetween(
        LocalDate dataInicio,
        LocalDate dataFim
    );

    List<EventoSentinela> findByEventosOcorridosAndDataEventoBetween(
        EventosOcorridos tipoEvento,
        LocalDate dataInicio,
        LocalDate dataFim
    );    
}
