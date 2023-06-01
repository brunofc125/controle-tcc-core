package com.controletcc.service;

import com.controletcc.error.BusinessException;
import com.controletcc.model.entity.ModeloAspectoAvaliacao;
import com.controletcc.model.entity.ProjetoTccAspectoAvaliacao;
import com.controletcc.model.entity.ProjetoTccAvaliacao;
import com.controletcc.repository.ProjetoTccAspectoAvaliacaoRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class ProjetoTccAspectoAvaliacaoService {

    private final ProjetoTccAspectoAvaliacaoRepository projetoTccAspectoAvaliacaoRepository;

    public List<ProjetoTccAspectoAvaliacao> generateByList(@NonNull ProjetoTccAvaliacao projetoTccAvaliacao, List<ModeloAspectoAvaliacao> aspectos) {
        return aspectos != null && !aspectos.isEmpty() ? aspectos.stream().map(a -> generate(projetoTccAvaliacao, a)).toList() : Collections.emptyList();
    }

    private ProjetoTccAspectoAvaliacao generate(@NonNull ProjetoTccAvaliacao projetoTccAvaliacao, @NonNull ModeloAspectoAvaliacao modeloAspectoAvaliacao) {
        var projetoTccAspectoAvaliacao = new ProjetoTccAspectoAvaliacao();
        projetoTccAspectoAvaliacao.setProjetoTccAvaliacao(projetoTccAvaliacao);
        projetoTccAspectoAvaliacao.setDescricao(modeloAspectoAvaliacao.getDescricao());
        projetoTccAspectoAvaliacao.setPeso(modeloAspectoAvaliacao.getPeso());
        return projetoTccAspectoAvaliacaoRepository.save(projetoTccAspectoAvaliacao);
    }

}
