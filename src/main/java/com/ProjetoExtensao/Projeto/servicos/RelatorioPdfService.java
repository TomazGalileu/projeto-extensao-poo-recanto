package com.ProjetoExtensao.Projeto.servicos;

import com.ProjetoExtensao.Projeto.models.EventoSentinela;
import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.models.Vacina;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class RelatorioPdfService {

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void gerarRelatorioIndividual(
            File arquivo,
            Paciente paciente,
            LocalDate dataInicial,
            LocalDate dataFinal,
            List<Vacina> vacinas,
            List<EventoSentinela> eventos
    ) {
        Document documento = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(
                    documento,
                    new FileOutputStream(arquivo)
            );

            documento.open();

            adicionarTitulo(documento);
            adicionarPeriodo(
                    documento,
                    dataInicial,
                    dataFinal
            );

            adicionarDadosPaciente(documento, paciente);
            adicionarTabelaVacinas(documento, vacinas);
            adicionarTabelaEventos(documento, eventos);

        } catch (Exception ex) {
            throw new RuntimeException(
                    "Não foi possível gerar o PDF.",
                    ex
            );

        } finally {
            if (documento.isOpen()) {
                documento.close();
            }
        }
    }

    private void adicionarTitulo(Document documento)
            throws Exception {

        Font fonteTitulo = FontFactory.getFont(
                FontFactory.HELVETICA_BOLD,
                18,
                new Color(40, 60, 110)
        );

        Paragraph titulo = new Paragraph(
                "Recanto do Sagrado Coração\n"
                        + "Relatório Individual por Período",
                fonteTitulo
        );

        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(15);

        documento.add(titulo);
    }

    private void adicionarPeriodo(
            Document documento,
            LocalDate dataInicial,
            LocalDate dataFinal
    ) throws Exception {

        Font fonteNormal = FontFactory.getFont(
                FontFactory.HELVETICA,
                11
        );

        Paragraph periodo = new Paragraph(
                "Período: "
                        + dataInicial.format(formatter)
                        + " até "
                        + dataFinal.format(formatter),
                fonteNormal
        );

        periodo.setAlignment(Element.ALIGN_CENTER);
        periodo.setSpacingAfter(20);

        documento.add(periodo);
    }

    private void adicionarDadosPaciente(
            Document documento,
            Paciente paciente
    ) throws Exception {

        adicionarSubtitulo(
                documento,
                "Dados da Paciente"
        );

        PdfPTable tabela = new PdfPTable(2);
        tabela.setWidthPercentage(100);
        tabela.setWidths(new float[]{1, 1});
        tabela.setSpacingAfter(18);

        adicionarCelulaDado(
                tabela,
                "Nome",
                paciente.getNomeCompleto()
        );

        adicionarCelulaDado(
                tabela,
                "CPF",
                formatarCpf(paciente.getCpf())
        );

        adicionarCelulaDado(
                tabela,
                "Data de nascimento",
                paciente.getDataNascimento().format(formatter)
        );

        adicionarCelulaDado(
                tabela,
                "Cartão SUS",
                paciente.getCartaoSUS()
        );

        adicionarCelulaDado(
                tabela,
                "Data de entrada",
                paciente.getDataEntrada().format(formatter)
        );

        adicionarCelulaDado(
                tabela,
                "Situação",
                Boolean.TRUE.equals(paciente.getAtivo())
                        ? "Ativa"
                        : "Inativa"
        );

        documento.add(tabela);
    }

    private void adicionarTabelaVacinas(
            Document documento,
            List<Vacina> vacinas
    ) throws Exception {

        adicionarSubtitulo(
                documento,
                "Vacinas no Período"
        );

        PdfPTable tabela = new PdfPTable(3);
        tabela.setWidthPercentage(100);
        tabela.setWidths(new float[]{1, 4, 2});
        tabela.setSpacingAfter(18);

        adicionarCabecalho(tabela, "ID");
        adicionarCabecalho(tabela, "Vacina");
        adicionarCabecalho(
                tabela,
                "Data de Aplicação"
        );

        if (vacinas.isEmpty()) {
            PdfPCell celula = new PdfPCell(
                    new Phrase(
                            "Nenhuma vacina encontrada no período."
                    )
            );

            celula.setColspan(3);
            celula.setPadding(8);
            celula.setHorizontalAlignment(
                    Element.ALIGN_CENTER
            );

            tabela.addCell(celula);

        } else {
            for (Vacina vacina : vacinas) {
                adicionarCelulaComum(
                        tabela,
                        String.valueOf(vacina.getId())
                );

                adicionarCelulaComum(
                        tabela,
                        vacina.getNome()
                );

                adicionarCelulaComum(
                        tabela,
                        vacina.getDataAplicacao()
                                .format(formatter)
                );
            }
        }

        documento.add(tabela);
    }

    private void adicionarTabelaEventos(
            Document documento,
            List<EventoSentinela> eventos
    ) throws Exception {

        adicionarSubtitulo(
                documento,
                "Eventos Sentinelas no Período"
        );

        PdfPTable tabela = new PdfPTable(4);
        tabela.setWidthPercentage(100);
        tabela.setWidths(new float[]{1, 3, 5, 2});

        adicionarCabecalho(tabela, "ID");
        adicionarCabecalho(tabela, "Tipo");
        adicionarCabecalho(tabela, "Descrição");
        adicionarCabecalho(tabela, "Data");

        if (eventos.isEmpty()) {
            PdfPCell celula = new PdfPCell(
                    new Phrase(
                            "Nenhum evento encontrado no período."
                    )
            );

            celula.setColspan(4);
            celula.setPadding(8);
            celula.setHorizontalAlignment(
                    Element.ALIGN_CENTER
            );

            tabela.addCell(celula);

        } else {
            for (EventoSentinela evento : eventos) {
                adicionarCelulaComum(
                        tabela,
                        String.valueOf(evento.getId())
                );

                adicionarCelulaComum(
                        tabela,
                        formatarEvento(
                                evento.getEventosOcorridos().name()
                        )
                );

                adicionarCelulaComum(
                        tabela,
                        evento.getDescricao()
                );

                adicionarCelulaComum(
                        tabela,
                        evento.getDataEvento()
                                .format(formatter)
                );
            }
        }

        documento.add(tabela);
    }

    private void adicionarSubtitulo(
            Document documento,
            String texto
    ) throws Exception {

        Font fonteSubtitulo = FontFactory.getFont(
                FontFactory.HELVETICA_BOLD,
                13,
                new Color(40, 60, 110)
        );

        Paragraph subtitulo = new Paragraph(
                texto,
                fonteSubtitulo
        );

        subtitulo.setSpacingBefore(5);
        subtitulo.setSpacingAfter(8);

        documento.add(subtitulo);
    }

    private void adicionarCabecalho(
            PdfPTable tabela,
            String texto
    ) {
        Font fonte = FontFactory.getFont(
                FontFactory.HELVETICA_BOLD,
                10,
                Color.WHITE
        );

        PdfPCell celula = new PdfPCell(
                new Phrase(texto, fonte)
        );

        celula.setBackgroundColor(
                new Color(40, 60, 110)
        );

        celula.setPadding(7);
        celula.setHorizontalAlignment(
                Element.ALIGN_CENTER
        );

        tabela.addCell(celula);
    }

    private void adicionarCelulaComum(
            PdfPTable tabela,
            String texto
    ) {
        PdfPCell celula = new PdfPCell(
                new Phrase(
                        texto == null ? "" : texto
                )
        );

        celula.setPadding(6);
        celula.setVerticalAlignment(
                Element.ALIGN_MIDDLE
        );

        tabela.addCell(celula);
    }

    private void adicionarCelulaDado(
            PdfPTable tabela,
            String rotulo,
            String valor
    ) {
        Font fonteRotulo = FontFactory.getFont(
                FontFactory.HELVETICA_BOLD,
                10
        );

        Phrase conteudo = new Phrase();

        conteudo.add(
                new Phrase(
                        rotulo + ": ",
                        fonteRotulo
                )
        );

        conteudo.add(
                new Phrase(
                        valor == null ? "" : valor
                )
        );

        PdfPCell celula = new PdfPCell(conteudo);
        celula.setPadding(7);

        tabela.addCell(celula);
    }

    private String formatarEvento(String nomeEnum) {
        String texto = nomeEnum
                .replace("_", " ")
                .toLowerCase();

        return texto.substring(0, 1).toUpperCase()
                + texto.substring(1);
    }

    private String formatarCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return cpf == null ? "" : cpf;
        }

        return cpf.substring(0, 3)
                + "."
                + cpf.substring(3, 6)
                + "."
                + cpf.substring(6, 9)
                + "-"
                + cpf.substring(9);
    }
}
