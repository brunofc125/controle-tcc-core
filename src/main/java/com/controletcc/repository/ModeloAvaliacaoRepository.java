package com.controletcc.repository;

import com.controletcc.model.entity.ModeloAvaliacao;
import com.controletcc.repository.projection.ModeloAvaliacaoProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ModeloAvaliacaoRepository extends JpaRepository<ModeloAvaliacao, Long> {

    @Query(value = """  
            SELECT distinct
                ma.id as id,
                concat(at.faculdade, ' - ', at.curso) as descricaoAreaTcc
            FROM ModeloAvaliacao ma
            JOIN ma.areaTcc at
                WHERE ma.dataExclusao is null
                AND at.id in :idAreaTccList
                AND (:id is null or ma.id = :id)
                AND (:idAreaTcc is null or at.id = :idAreaTcc)
            """
    )
    Page<ModeloAvaliacaoProjection> search(List<Long> idAreaTccList,
                                           Long id,
                                           Long idAreaTcc,
                                           Pageable pageable);

    boolean existsByAreaTccIdAndDataExclusaoNullAndIdNot(Long idAreaTcc, Long id);

    boolean existsByAreaTccIdAndDataExclusaoNull(Long idAreaTcc);

}
