package com.controletcc.repository;

import com.controletcc.model.entity.VersaoTccObservacao;
import com.controletcc.repository.projection.VersaoTccObservacaoProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VersaoTccObservacaoRepository extends JpaRepository<VersaoTccObservacao, Long> {

    @Query(value = """  
            SELECT
                vto.id as id,
                u.name as professor,
                vto.dataInclusao as dataInclusao,
                vto.nomeArquivo as nomeArquivo,
                vto.observacao as observacao,
                vto.avaliacao as avaliacao
            FROM VersaoTccObservacao vto
            JOIN User u on u.id = vto.idUserInclusao
                where vto.versaoTcc.id = :idVersaoTcc
                and (:onlyAvaliacao = false or vto.avaliacao = true)"""
    )
    Page<VersaoTccObservacaoProjection> search(Long idVersaoTcc,
                                               boolean onlyAvaliacao,
                                               Pageable pageable);

}
