package com.ProjetoExtensao.Projeto.repositorios;

import com.ProjetoExtensao.Projeto.models.EventoSentinela;
import com.ProjetoExtensao.Projeto.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.ProjetoExtensao.Projeto.utils.EventosOcorridos;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EventoSentinelaRepositorio
        extends JpaRepository<EventoSentinela, Long> {

    List<EventoSentinela> findByPaciente(Paciente paciente);

    List<EventoSentinela>
    findByPacienteOrderByDataEventoDesc(Paciente paciente);

    List<EventoSentinela>
    findByPacienteAndDataEventoBetweenOrderByDataEventoDesc(
        Paciente paciente,
        LocalDate dataInicial,
        LocalDate dataFinal
    );

    @Query("""
        SELECT COUNT(DISTINCT e.paciente.id)
        FROM EventoSentinela e
        WHERE e.paciente.ativo = true
    """)
    long contarPacientesComEvento();

    @Query("""
        SELECT COUNT(DISTINCT e.paciente.id)
        FROM EventoSentinela e
        WHERE e.eventosOcorridos = :tipoEvento
        AND e.paciente.ativo = true
    """)
    long contarPacientesPorTipoEvento(
        @Param("tipoEvento") EventosOcorridos tipoEvento
    );
}
