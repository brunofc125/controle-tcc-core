package com.controletcc.repository;

import com.controletcc.model.entity.ModeloAspectoAvaliacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModeloAspectoAvaliacaoRepository extends JpaRepository<ModeloAspectoAvaliacao, Long> {

    List<ModeloAspectoAvaliacao> getAllByModeloItemAvaliacaoId(Long idModeloItemAvaliacao);

}
