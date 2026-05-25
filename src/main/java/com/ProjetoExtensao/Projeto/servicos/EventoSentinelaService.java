package com.ProjetoExtensao.Projeto.servicos;

import com.ProjetoExtensao.Projeto.models.EventoSentinela;
import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.repositorios.EventoSentinelaRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EventoSentinelaService {
    private EventoSentinelaRepositorio eventoSentinelaRepositorio;

    public void salvarEvento(EventoSentinela evento) {
        eventoSentinelaRepositorio.save(evento);
    }

    public List<EventoSentinela> findEventosByPaciente(Paciente paciente) {
        return eventoSentinelaRepositorio.findByPacienteOrderByDataEventoDesc(paciente);
    }

    public List<EventoSentinela> findAllEventos() {
        return eventoSentinelaRepositorio.findAll();
    }

    public EventoSentinela findEventoById(Long id) {
        return eventoSentinelaRepositorio.findById(id).orElseThrow(() -> new RuntimeException("Evento não encontrado"));
    }
}
