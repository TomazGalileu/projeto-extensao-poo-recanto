package com.ProjetoExtensao.Projeto.Config;

import com.ProjetoExtensao.Projeto.models.Consulta;
import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.models.ResponsavelSaude;
import com.ProjetoExtensao.Projeto.repositorios.ConsultaRepositorio;
import com.ProjetoExtensao.Projeto.repositorios.PacienteRepositorio;
import com.ProjetoExtensao.Projeto.repositorios.ResponsavelRepositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.time.LocalTime;

@Configuration
public class DatabaseConfig {
    private final JdbcTemplate jdbcTemplate;

    public DatabaseConfig(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Bean
    CommandLineRunner initDB(ResponsavelRepositorio responsavelRepositorio, PacienteRepositorio pacienteRepositorio, ConsultaRepositorio consultaRepositorio) {
        return args -> {
            if (!isTablePopulated("pacientes")) {
                System.out.println("Preenchendo o banco de dados...");

                ResponsavelSaude rs1 = responsavelRepositorio.save(new ResponsavelSaude("ana.silva@saude.com", "1234", "Ana Paula da Silva"));
                ResponsavelSaude rs2 = responsavelRepositorio.save(new ResponsavelSaude("joao.lima@saude.com", "abcd", "João Carlos Lima"));
                ResponsavelSaude rs3 = responsavelRepositorio.save(new ResponsavelSaude("mariana.costa@saude.com", "pass", "Mariana Costa Oliveira"));
                ResponsavelSaude rs4 = responsavelRepositorio.save(new ResponsavelSaude("felipe.almeida@saude.com", "4321", "Felipe Gomes de Almeida"));
                ResponsavelSaude rs5 = responsavelRepositorio.save(new ResponsavelSaude("larissa.oliveira@saude.com", "qwer", "Larissa Moura de Oliveira"));

                Paciente p1 = pacienteRepositorio.save(new Paciente("Ana Beatriz Silva", "12345678901", LocalDate.of(1995, 3, 12), "Maria da Silva", "704030195830001", LocalDate.of(2024, 6, 10)));
                Paciente p2 = pacienteRepositorio.save(new Paciente("João Pedro Lima", "23456789012", LocalDate.of(1988, 7, 23), "Fernanda Lima", "209384750192837", LocalDate.of(2024, 6, 11)));
                Paciente p3 = pacienteRepositorio.save(new Paciente("Mariana Costa", "34567890123", LocalDate.of(2000, 1, 5), "Tatiane Costa", "807364950123845", LocalDate.of(2024, 6, 12)));
                Paciente p4 = pacienteRepositorio.save(new Paciente("Carlos Eduardo Rocha", "45678901234", LocalDate.of(1972, 11, 30), "Elaine Rocha", "906573820194857", LocalDate.of(2024, 6, 13)));
                Paciente p5 = pacienteRepositorio.save(new Paciente("Juliana Martins", "56789012345", LocalDate.of(1999, 9, 18), "Sandra Martins", "102938475601938", LocalDate.of(2024, 6, 14)));
                Paciente p6 = pacienteRepositorio.save(new Paciente("Felipe Almeida", "67890123456", LocalDate.of(1985, 4, 9), "Luciana Almeida", "384756102938475", LocalDate.of(2024, 6, 15)));
                Paciente p7 = pacienteRepositorio.save(new Paciente("Larissa Oliveira", "78901234567", LocalDate.of(1992, 2, 20), "Rosana Oliveira", "837465928374659", LocalDate.of(2024, 6, 15)));
                Paciente p8 = pacienteRepositorio.save(new Paciente("Vinícius Souza", "89012345678", LocalDate.of(2003, 12, 1), "Patrícia Souza", "564738291028374", LocalDate.of(2024, 6, 15)));
                Paciente p9 = pacienteRepositorio.save(new Paciente("Camila Ferreira", "90123456789", LocalDate.of(1990, 8, 14), "Sônia Ferreira", "908172635465738", LocalDate.of(2024, 6, 15)));
                Paciente p10 = pacienteRepositorio.save(new Paciente("Gabriel Mendes", "01234567890", LocalDate.of(1997, 5, 25), "Adriana Mendes", "284756918273645", LocalDate.of(2024, 6, 15)));

                consultaRepositorio.save(new Consulta(LocalDate.now(), LocalTime.of(10,00), "ROTINA", rs1, p1));
                consultaRepositorio.save(new Consulta(LocalDate.now(), LocalTime.of(12,00), "ROTINA", rs1, p2));
                consultaRepositorio.save(new Consulta(LocalDate.now(), LocalTime.of(8,30), "ESPECIALIZADA", rs2, p4));
                consultaRepositorio.save(new Consulta(LocalDate.now(), LocalTime.of(14,20), "ESPECIALIZADA", rs3, p8));
                consultaRepositorio.save(new Consulta(LocalDate.now(), LocalTime.of(9,00), "EMERGENCIAL", rs4, p9));

                System.out.println("Preenchimento do banco de dados concluído.");
            } else {
                System.out.println("Banco de dados preenchido!");
            }
        };
    }

    private boolean isTablePopulated(String table) {
        try {
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + table.toLowerCase(), Integer.class);
            return count != null && count > 0;
        } catch (DataAccessException e) {
            System.out.println("Tabela '" + table.toLowerCase() + "' não encontrada.");
            return false;
        }
    }
}
