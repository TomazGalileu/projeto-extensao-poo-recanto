package com.ProjetoExtensao.Projeto.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "prescricoes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Prescricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String medicamento;

    @Column(nullable = false)
    private String dosagem;

    @Column(nullable = false)
    private String frequencia;

    @Column(nullable = false)
    private LocalDate dataInicio;

    private LocalDate dataFim;

    @Column(nullable = false)
    private Boolean ativa = true;

    @ManyToOne
    @JoinColumn(name = "prontuario_id", nullable = false)
    private ProntuarioMedico prontuarioMedico;
}