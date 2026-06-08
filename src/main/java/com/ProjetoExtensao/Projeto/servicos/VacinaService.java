package com.ProjetoExtensao.Projeto.servicos;

import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.models.Vacina;
import com.ProjetoExtensao.Projeto.repositorios.VacinaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ProjetoExtensao.Projeto.repositorios.PacienteRepositorio;

import java.time.LocalDate;
import java.util.List;

@Service
public class VacinaService {

    @Autowired
    private VacinaRepositorio vacinaRepositorio;

    @Autowired
    private PacienteRepositorio pacienteRepositorio;

    public Vacina salvarVacina(
            String nome,
            LocalDate dataAplicacao,
            Paciente paciente
    ) {
        Vacina vacina = new Vacina();

        vacina.setNome(nome);
        vacina.setDataAplicacao(dataAplicacao);
        vacina.setPaciente(paciente);

        return vacinaRepositorio.save(vacina);
    }

    public List<Vacina> buscarPorPaciente(Long pacienteId) {
        return vacinaRepositorio.findByPacienteId(pacienteId);
    }

    public List<Vacina> listarTodas() {
        return vacinaRepositorio.findAll();
    }

    public void excluirVacina(Long vacinaId) {
        vacinaRepositorio.deleteById(vacinaId);
    }

    public double calcularPercentualVacinacao(String nomeVacina) {
    long totalPacientesAtivas =
            pacienteRepositorio.countByAtivo(true);

    if (totalPacientesAtivas == 0) {
        return 0.0;
    }

    long pacientesVacinadas =
            vacinaRepositorio
                    .contarPacientesVacinadasPorNome(nomeVacina);

    return (pacientesVacinadas * 100.0)
            / totalPacientesAtivas;
    }

    public List<Vacina> buscarPorPacienteEPeriodo(
        Paciente paciente,
        LocalDate dataInicial,
        LocalDate dataFinal
    ) {
    return vacinaRepositorio
            .findByPacienteAndDataAplicacaoBetweenOrderByDataAplicacaoDesc(
                    paciente,
                    dataInicial,
                    dataFinal
            );
    }
}