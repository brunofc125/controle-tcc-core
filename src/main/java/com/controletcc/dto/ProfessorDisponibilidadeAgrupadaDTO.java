package com.controletcc.dto;

import com.controletcc.repository.projection.ProfessorDisponibilidadeAgrupadaProjection;
import com.controletcc.util.LocalDateTimeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfessorDisponibilidadeAgrupadaDTO implements Serializable {
    private LocalDateTime dataHora;
    private String descricao;
    private Long qtdProfessores;
    private boolean todosProfessoresDisponiveis;

    public ProfessorDisponibilidadeAgrupadaDTO(ProfessorDisponibilidadeAgrupadaProjection projection, String nomesProfessores, boolean todosProfessoresDisponiveis) {
        this.dataHora = projection.getDataHora();
        this.descricao = nomesProfessores;
        this.qtdProfessores = projection.getQtdProfessores();
        this.todosProfessoresDisponiveis = todosProfessoresDisponiveis;
    }

    public String getDataHoraStr() {
        return LocalDateTimeUtil.localDateTimeToString(dataHora, "yyyy-MM-dd HH");
    }
}
