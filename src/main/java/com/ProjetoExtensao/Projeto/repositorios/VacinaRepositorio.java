package com.ProjetoExtensao.Projeto.repositorios;

import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.models.Vacina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VacinaRepositorio
        extends JpaRepository<Vacina, Long> {

    List<Vacina> findByPacienteId(Long pacienteId);

    List<Vacina> findByPacienteAndDataAplicacaoBetweenOrderByDataAplicacaoDesc(
            Paciente paciente,
            LocalDate dataInicial,
            LocalDate dataFinal
    );

    @Query("""
        SELECT COUNT(DISTINCT v.paciente.id)
        FROM Vacina v
        WHERE LOWER(v.nome) = LOWER(:nome)
        AND v.paciente.ativo = true
    """)
    long contarPacientesVacinadasPorNome(
            @Param("nome") String nome
    );
}

