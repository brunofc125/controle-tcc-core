package com.controletcc.model.dto;

import com.controletcc.model.dto.base.ArquivoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnexoGeralDTO extends ArquivoDTO {
    private Long id;
    private String descricao;
    private Long idModeloDocumento;
    private Long idProfessor;
    private Long idProjetoTcc;
    private LocalDateTime dataExclusao;
}
