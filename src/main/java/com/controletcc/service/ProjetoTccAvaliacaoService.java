package com.controletcc.service;

import com.controletcc.error.BusinessException;
import com.controletcc.model.entity.ModeloItemAvaliacao;
import com.controletcc.model.entity.Professor;
import com.controletcc.model.entity.ProjetoTcc;
import com.controletcc.model.entity.ProjetoTccAvaliacao;
import com.controletcc.model.enums.TipoProfessor;
import com.controletcc.model.enums.TipoTcc;
import com.controletcc.repository.ProjetoTccAvaliacaoRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class ProjetoTccAvaliacaoService {

    private final ProjetoTccAvaliacaoRepository projetoTccAvaliacaoRepository;

    public ProjetoTccAvaliacao getById(@NonNull Long id) {
        return projetoTccAvaliacaoRepository.getReferenceById(id);
    }

    public boolean existsAvaliacaoIniciada(@NonNull Long idProjetoTcc) {
        return projetoTccAvaliacaoRepository.existsAvaliacaoIniciada(idProjetoTcc);
    }

    public ProjetoTccAvaliacao generate(
            @NonNull ModeloItemAvaliacao modeloItemAvaliacao,
            @NonNull TipoTcc tipoTcc,
            @NonNull TipoProfessor tipoProfessor,
            @NonNull ProjetoTcc projetoTcc,
            @NonNull Professor professor
    ) {
        var projetoTccAvaliacao = new ProjetoTccAvaliacao();
        projetoTccAvaliacao.setModeloItemAvaliacao(modeloItemAvaliacao);
        projetoTccAvaliacao.setTipoTcc(tipoTcc);
        projetoTccAvaliacao.setTipoProfessor(tipoProfessor);
        projetoTccAvaliacao.setProjetoTcc(projetoTcc);
        projetoTccAvaliacao.setProfessor(professor);
        return projetoTccAvaliacaoRepository.save(projetoTccAvaliacao);
    }

    public ProjetoTccAvaliacao getByTipoTccAndTipoProfessorAndProjetoTccAndProfessor(TipoTcc tipoTcc, TipoProfessor tipoProfessor, Long idProjetoTcc, Long idProfessor) {
        return projetoTccAvaliacaoRepository.getProjetoTccAvaliacaoByTipoTccAndTipoProfessorAndProjetoTccIdAndProfessorId(tipoTcc, tipoProfessor, idProjetoTcc, idProfessor);
    }

}
