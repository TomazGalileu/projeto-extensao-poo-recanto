package com.ProjetoExtensao.Projeto.servicos;

import com.ProjetoExtensao.Projeto.models.EventoSentinela;
import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.repositorios.EventoSentinelaRepositorio;
import com.ProjetoExtensao.Projeto.repositorios.PacienteRepositorio;
import com.ProjetoExtensao.Projeto.utils.EventosOcorridos;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class EventoSentinelaService {

    private EventoSentinelaRepositorio eventoSentinelaRepositorio;
    private PacienteRepositorio pacienteRepositorio;

    public void salvarEvento(EventoSentinela evento) {
        eventoSentinelaRepositorio.save(evento);
    }

    public List<EventoSentinela> findEventosByPaciente(Paciente paciente) {
        return eventoSentinelaRepositorio
                .findByPacienteOrderByDataEventoDesc(paciente);
    }

    public List<EventoSentinela> findAllEventos() {
        return eventoSentinelaRepositorio.findAll();
    }

    public EventoSentinela findEventoById(Long id) {
        return eventoSentinelaRepositorio
                .findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Evento não encontrado")
                );
    }

    public double calcularPercentualPacientesComEvento() {
        long totalPacientesAtivas =
                pacienteRepositorio.countByAtivo(true);

        if (totalPacientesAtivas == 0) {
            return 0.0;
        }

        long pacientesComEvento =
                eventoSentinelaRepositorio
                        .contarPacientesComEvento();

        return (pacientesComEvento * 100.0)
                / totalPacientesAtivas;
    }

    public double calcularPercentualPorTipoEvento(
            EventosOcorridos tipoEvento
    ) {
        long totalPacientesAtivas =
                pacienteRepositorio.countByAtivo(true);

        if (totalPacientesAtivas == 0) {
            return 0.0;
        }

        long pacientesComEvento =
                eventoSentinelaRepositorio
                        .contarPacientesPorTipoEvento(tipoEvento);

        return (pacientesComEvento * 100.0)
                / totalPacientesAtivas;
    }

    public List<EventoSentinela> buscarPorPacienteEPeriodo(
        Paciente paciente,
        LocalDate dataInicial,
        LocalDate dataFinal
    ) {
    return eventoSentinelaRepositorio
            .findByPacienteAndDataEventoBetweenOrderByDataEventoDesc(
                    paciente,
                    dataInicial,
                    dataFinal
            );
    }
}
