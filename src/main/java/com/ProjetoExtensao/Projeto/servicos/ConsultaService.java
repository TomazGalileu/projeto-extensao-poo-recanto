package com.ProjetoExtensao.Projeto.servicos;

import com.ProjetoExtensao.Projeto.infra.DateTimeFormatter;
import com.ProjetoExtensao.Projeto.models.Consulta;
import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.models.TipoConsulta;
import com.ProjetoExtensao.Projeto.repositorios.ConsultaRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ConsultaService {
    private ConsultaRepositorio consultaRepositorio;
    private ResponsavelService responsavelService;
    private PacienteService pacienteService;

    public Consulta findConsultaByPaciente(Paciente paciente) {
        return consultaRepositorio.findByPaciente(paciente).orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
    }
    
    public List<Consulta> findAllConsultasByPaciente(Paciente paciente) {
        return consultaRepositorio.findAllByPaciente(paciente);
    }
    
    public Consulta findConsultaById(Long id) {
        return consultaRepositorio.findById(id).orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
    }

    public void salvarConsulta(String pacienteCpf, String data, String hora, String medicoNome, String tipoConsulta, String motivoConsulta, String diagnostico, String anotacoesMedico) {
        Consulta consulta = new Consulta();

        consulta.setData(LocalDate.parse(data, DateTimeFormatter.DATE_TIME_FORMATTER));
        consulta.setHora(LocalTime.parse(hora, DateTimeFormatter.TIME_FORMATTER));
        consulta.setTipoConsulta(TipoConsulta.getType(tipoConsulta));

        consulta.setPaciente(pacienteService.findPacienteByCpf(pacienteCpf));
        consulta.setResponsavelSaude(responsavelService.findResponsavelByNome(medicoNome));
        
        consulta.setMotivoConsulta(motivoConsulta);
        consulta.setDiagnostico(diagnostico.isEmpty() ? null : diagnostico);
        consulta.setAnotacoesMedico(anotacoesMedico.isEmpty() ? null : anotacoesMedico);

        consultaRepositorio.save(consulta);
    }
}
