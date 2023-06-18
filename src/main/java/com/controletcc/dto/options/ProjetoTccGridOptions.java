package com.controletcc.dto.options;

import com.controletcc.dto.enums.SituacaoSolicitacaoBanca;
import com.controletcc.dto.options.base.BaseGridOptions;
import com.controletcc.model.enums.SituacaoTcc;
import com.controletcc.model.enums.TipoTcc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjetoTccGridOptions extends BaseGridOptions {

    private Long id;
    private String tema;
    private String anoPeriodo;
    private TipoTcc tipoTcc;
    private SituacaoTcc situacaoTcc;
    private String nomeProfessorOrientador;
    private String nomeAluno;
    private SituacaoSolicitacaoBanca situacaoSolicitacaoBanca;

    public String getSituacaoSolicitacaoBancaName() {
        return situacaoSolicitacaoBanca != null ? situacaoSolicitacaoBanca.name() : null;
    }

    @Override
    public Pageable getPageable() {
        if (this.getPage() == null || this.getPageSize() == null) {
            return null;
        }

        if (this.getOrderByDirection() != null && this.getOrderByField() != null && !this.getOrderByField().isBlank()) {
            return PageRequest.of(this.getPage().intValue(), this.getPageSize().intValue(), this.getOrderByDirection().getDirection(), this.getOrderByField());
        }

        var sort = Sort.by(Sort.Order.desc("anoPeriodo"), Sort.Order.desc("tipoTcc"), Sort.Order.asc("alunos"));
        return PageRequest.of(this.getPage().intValue(), this.getPageSize().intValue(), sort);
    }

}
