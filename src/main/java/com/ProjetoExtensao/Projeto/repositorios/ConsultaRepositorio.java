package com.ProjetoExtensao.Projeto.repositorios;

import com.ProjetoExtensao.Projeto.models.Consulta;
import com.ProjetoExtensao.Projeto.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ConsultaRepositorio extends JpaRepository<Consulta, Long> {

    Optional<Consulta> findByPaciente(Paciente paciente);

    // Buscar todas as consultas de um paciente
    List<Consulta> findAllByPaciente(Paciente paciente);

    // Buscar consultas de um paciente dentro de um período
    List<Consulta> findAllByPacienteAndDataBetween(
            Paciente paciente,
            LocalDate dataInicio,
            LocalDate dataFim
    );

    // Buscar consultas ordenadas da mais recente para a mais antiga
    List<Consulta> findAllByPacienteOrderByDataDescHoraDesc(
            Paciente paciente
    );
}