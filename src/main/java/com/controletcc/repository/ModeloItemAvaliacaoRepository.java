package com.controletcc.repository;

import com.controletcc.model.entity.ModeloItemAvaliacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModeloItemAvaliacaoRepository extends JpaRepository<ModeloItemAvaliacao, Long> {

    List<ModeloItemAvaliacao> getAllByModeloAvaliacaoId(Long idModeloAvaliacao);

    boolean existsByModeloAvaliacaoIdAndDescricaoIgnoreCase(Long idModeloAvaliacao, String descricao);

    boolean existsByModeloAvaliacaoIdAndDescricaoIgnoreCaseAndIdNot(Long idModeloAvaliacao, String descricao, Long id);

}
