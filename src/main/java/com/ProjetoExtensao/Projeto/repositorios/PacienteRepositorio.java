package com.ProjetoExtensao.Projeto.repositorios;

import com.ProjetoExtensao.Projeto.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PacienteRepositorio extends JpaRepository<Paciente, Long> {
    List<Paciente> findByNomeCompletoContainingIgnoreCase(String nome);

    List<Paciente> findByCpfContaining(String cpf);

    Optional<Paciente> findByCpf(String cpf);

    // Buscar pacientes por status (ativo/inativo)
    List<Paciente> findByAtivo(Boolean ativo);
}
