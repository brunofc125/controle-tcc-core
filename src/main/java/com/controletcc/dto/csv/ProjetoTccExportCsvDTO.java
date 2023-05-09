package com.controletcc.dto.csv;

import com.controletcc.annotation.CsvColumn;
import com.controletcc.dto.enums.CsvType;
import com.controletcc.repository.projection.ProjetoTccExportProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjetoTccExportCsvDTO extends BaseImportCsvDTO {

    @CsvColumn(name = "ID", type = CsvType.LONG)
    private Long id;

    @CsvColumn(name = "Tema", type = CsvType.STRING)
    private String tema;

    @CsvColumn(name = "Alunos", type = CsvType.STRING)
    private String alunos;

    @CsvColumn(name = "Orientador", type = CsvType.STRING)
    private String professorOrientador;

    @CsvColumn(name = "Supervisor", type = CsvType.STRING)
    private String professorSupervisor;

    @CsvColumn(name = "Membros da Banca", type = CsvType.STRING)
    private String membrosBanca;

    @CsvColumn(name = "Ano/Período", type = CsvType.STRING)
    private String anoPeriodo;

    @CsvColumn(name = "Etapa de TCC", type = CsvType.STRING)
    private String tipoTcc;

    @CsvColumn(name = "Situação do TCC", type = CsvType.STRING)
    private String situacaoTcc;

    public ProjetoTccExportCsvDTO(ProjetoTccExportProjection projection) {
        this.id = projection.getId();
        this.tema = projection.getTema();
        this.alunos = projection.getAlunos();
        this.professorOrientador = projection.getProfessorOrientador();
        this.professorSupervisor = projection.getProfessorSupervisor();
        this.membrosBanca = projection.getMembrosBanca();
        this.anoPeriodo = projection.getAnoPeriodo();
        this.tipoTcc = projection.getTipoTcc() != null ? projection.getTipoTcc().getDescricao() : null;
        this.situacaoTcc = projection.getSituacaoTcc() != null ? projection.getSituacaoTcc().getDescricao() : null;
    }

}
