package com.ProjetoExtensao.Projeto.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "exames")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Exame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private LocalDate dataSolicitacao;

    @Column(columnDefinition = "TEXT")
    private String resultado;

    private LocalDate dataResultado;

    @ManyToOne
    @JoinColumn(name = "prontuario_id", nullable = false)
    private ProntuarioMedico prontuarioMedico;
}