package com.ProjetoExtensao.Projeto.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "consultas")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate data;

    @Column(nullable = false, columnDefinition = "TIME")
    private LocalTime hora;

    @Enumerated(EnumType.STRING)
    private TipoConsulta tipoConsulta;

    @ManyToOne
    @JoinColumn(name = "responsavel_id", nullable = false)
    private ResponsavelSaude responsavelSaude;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @Column(columnDefinition = "TEXT")
    private String motivoConsulta;

    @Column(columnDefinition = "TEXT")
    private String diagnostico;

    @Column(columnDefinition = "TEXT")
    private String anotacoesMedico;

    public Consulta(LocalDate data, LocalTime hora, String tipoConsulta, ResponsavelSaude responsavelSaude, Paciente paciente) {
        this.data = data;
        this.hora = hora;
        this.tipoConsulta = TipoConsulta.getType(tipoConsulta);
        this.responsavelSaude = responsavelSaude;
        this.paciente = paciente;
    }
}
