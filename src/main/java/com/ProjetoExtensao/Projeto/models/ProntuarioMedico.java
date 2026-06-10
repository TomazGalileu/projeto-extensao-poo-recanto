package com.ProjetoExtensao.Projeto.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prontuarios_medicos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class ProntuarioMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "paciente_id", nullable = false, unique = true)
    private Paciente paciente;

    @OneToMany(
            mappedBy = "prontuarioMedico",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Prescricao> prescricoes = new ArrayList<>();

    @OneToMany(
            mappedBy = "prontuarioMedico",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Exame> exames = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "historico_internacoes",
            joinColumns = @JoinColumn(name = "prontuario_id")
    )
    @Column(name = "descricao", columnDefinition = "TEXT")
    private List<String> historicoInternacoes = new ArrayList<>();

    public ProntuarioMedico(Paciente paciente) {
        this.paciente = paciente;
    }

    public void adicionarPrescricao(Prescricao prescricao) {
        prescricoes.add(prescricao);
        prescricao.setProntuarioMedico(this);
    }

    public void adicionarExame(Exame exame) {
        exames.add(exame);
        exame.setProntuarioMedico(this);
    }

    public void adicionarInternacao(String descricao) {
        historicoInternacoes.add(descricao);
    }

    @Transient
    public List<Consulta> getConsultas() {
        if (paciente == null || paciente.getConsultas() == null) {
            return new ArrayList<>();
        }

        return paciente.getConsultas();
    }

    @Transient
    public List<Vacina> getHistoricoVacinacao() {
        if (paciente == null || paciente.getVacinas() == null) {
            return new ArrayList<>();
        }

        return paciente.getVacinas();
    }
}