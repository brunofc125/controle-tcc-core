package com.controletcc.dto;

import com.controletcc.model.dto.AgendaApresentacaoRestricaoDTO;
import com.controletcc.model.dto.ProfessorDisponibilidadeDTO;
import com.controletcc.repository.projection.AgendaPeriodoProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AgendaPeriodoDTO implements Serializable {

    private Integer ano;
    private Integer periodo;
    private LocalDate minDataInicial;
    private LocalDate maxDataFinal;
    private Integer minHoraInicial;
    private Integer maxHoraFinal;
    private List<AgendaApresentacaoRestricaoDTO> restricoes;
    private List<ProfessorDisponibilidadeDTO> disponibilidades;

    public AgendaPeriodoDTO(AgendaPeriodoProjection projection) {
        this.ano = projection.getAno();
        this.periodo = projection.getPeriodo();
        this.minDataInicial = projection.getMinDataInicial();
        this.maxDataFinal = projection.getMaxDataFinal();
        this.minHoraInicial = projection.getMinHoraInicial();
        this.maxHoraFinal = projection.getMaxHoraFinal();
    }

}
