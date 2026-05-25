package com.ProjetoExtensao.Projeto.models;

import com.ProjetoExtensao.Projeto.utils.EventosOcorridos;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "eventos_sentinelas")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class EventoSentinela {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private EventosOcorridos eventosOcorridos;

    @Column(nullable = false, length = 255)
    private String descricao;
        
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate dataEvento;

    public EventoSentinela(Paciente paciente, EventosOcorridos eventosOcorridos, String descricao, LocalDate dataEvento) {
        this.paciente = paciente;
        this.eventosOcorridos = eventosOcorridos;
        this.descricao = descricao;
        this.dataEvento = dataEvento;
    }
}
