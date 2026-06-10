package com.ProjetoExtensao.Projeto.servicos;

import com.ProjetoExtensao.Projeto.models.Exame;
import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.models.Prescricao;
import com.ProjetoExtensao.Projeto.models.ProntuarioMedico;
import com.ProjetoExtensao.Projeto.repositorios.ExameRepositorio;
import com.ProjetoExtensao.Projeto.repositorios.PacienteRepositorio;
import com.ProjetoExtensao.Projeto.repositorios.PrescricaoRepositorio;
import com.ProjetoExtensao.Projeto.repositorios.ProntuarioMedicoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Service
@Transactional
public class ProntuarioMedicoService {

    @Autowired
    private PacienteRepositorio pacienteRepositorio;

    @Autowired
    private ProntuarioMedicoRepositorio prontuarioMedicoRepositorio;

    @Autowired
    private PrescricaoRepositorio prescricaoRepositorio;

    @Autowired
    private ExameRepositorio exameRepositorio;

    public ProntuarioMedico buscarOuCriarProntuario(Long pacienteId) {
        Paciente paciente = buscarPacientePorId(pacienteId);

        return prontuarioMedicoRepositorio
            .findByPaciente(paciente)
            .orElseGet(() -> criarProntuario(paciente));
    }

    public Prescricao adicionarPrescricao(
            Long pacienteId,
            String medicamento,
            String dosagem,
            String frequencia,
            LocalDate dataInicio,
            LocalDate dataFim
    ) {
        ProntuarioMedico prontuario =
            buscarOuCriarProntuario(pacienteId);

        Prescricao prescricao = new Prescricao();

        prescricao.setMedicamento(medicamento);
        prescricao.setDosagem(dosagem);
        prescricao.setFrequencia(frequencia);
        prescricao.setDataInicio(dataInicio);
        prescricao.setDataFim(dataFim);
        prescricao.setAtiva(true);
        prescricao.setProntuarioMedico(prontuario);

        return prescricaoRepositorio.save(prescricao);
    }

    public List<Prescricao> listarPrescricoes(Long pacienteId) {
        ProntuarioMedico prontuario =
            buscarOuCriarProntuario(pacienteId);

        return prescricaoRepositorio
                .findByProntuarioMedicoOrderByDataInicioDesc(
                    prontuario
                );
    }

    public List<Prescricao> listarPrescricoesAtivas(Long pacienteId) {
        ProntuarioMedico prontuario =
            buscarOuCriarProntuario(pacienteId);

        return prescricaoRepositorio
                .findByProntuarioMedicoAndAtivaTrueOrderByDataInicioDesc(
                    prontuario
                );
    }

    public Prescricao encerrarPrescricao(
            Long prescricaoId,
            LocalDate dataFim
    ) {
        Prescricao prescricao =
                prescricaoRepositorio
                        .findById(prescricaoId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Prescricao nao encontrada."
                                )
                        );

        prescricao.setAtiva(false);
        prescricao.setDataFim(dataFim);

        return prescricaoRepositorio.save(prescricao);
    }

    public Exame adicionarExame(
        Long pacienteId,
        String nome,
        LocalDate dataSolicitacao
    ) {
        ProntuarioMedico prontuario =
            buscarOuCriarProntuario(pacienteId);

        Exame exame = new Exame();

        exame.setNome(nome);
        exame.setDataSolicitacao(dataSolicitacao);
        exame.setProntuarioMedico(prontuario);

        return exameRepositorio.save(exame);
    }

    public List<Exame> listarExames(Long pacienteId) {
        ProntuarioMedico prontuario =
            buscarOuCriarProntuario(pacienteId);

        return exameRepositorio
                .findByProntuarioMedicoOrderByDataSolicitacaoDesc(
                    prontuario
                );
    }

    public Exame registrarResultadoExame(
        Long exameId,
        String resultado,
        LocalDate dataResultado
    ) {
        Exame exame =
                exameRepositorio
                        .findById(exameId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Exame nao encontrado."
                                )
                        );

        exame.setResultado(resultado);
        exame.setDataResultado(dataResultado);

        return exameRepositorio.save(exame);
    }

    public ProntuarioMedico adicionarInternacao(
        Long pacienteId,
        String descricao
    ) {
        ProntuarioMedico prontuario =
            buscarOuCriarProntuario(pacienteId);

        prontuario.adicionarInternacao(descricao);

        return prontuarioMedicoRepositorio.save(prontuario);
    }

    public List<String> listarInternacoes(Long pacienteId) {
        ProntuarioMedico prontuario =
            buscarOuCriarProntuario(pacienteId);

        return new ArrayList<>(
            prontuario.getHistoricoInternacoes()
        );
    }

    public String gerarResumoHistorico(Long pacienteId) {
        ProntuarioMedico prontuario =
                buscarOuCriarProntuario(pacienteId);

        int totalConsultas = prontuario.getConsultas().size();
        int totalPrescricoes = listarPrescricoes(pacienteId).size();
        int totalExames = listarExames(pacienteId).size();
        int totalInternacoes =
                prontuario.getHistoricoInternacoes().size();
        int totalVacinas =
                prontuario.getHistoricoVacinacao().size();

        return """
                RESUMO DO PRONTUARIO
                ------------------------------------
                Total de consultas: %d
                Total de prescricoes: %d
                Total de exames: %d
                Total de internacoes registradas: %d
                Total de vacinas aplicadas: %d
                """.formatted(
                totalConsultas,
                totalPrescricoes,
                totalExames,
                totalInternacoes,
                totalVacinas
        );
    }

    private Paciente buscarPacientePorId(Long pacienteId) {
        return pacienteRepositorio
                .findById(pacienteId)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Paciente nao encontrada."
                        )
                );
    }

    private ProntuarioMedico criarProntuario(
            Paciente paciente
    ) {
        ProntuarioMedico prontuario =
                new ProntuarioMedico(paciente);

        return prontuarioMedicoRepositorio.save(prontuario);
    }
}