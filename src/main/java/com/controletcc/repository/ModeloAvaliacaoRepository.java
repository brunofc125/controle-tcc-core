package com.controletcc.repository;

import com.controletcc.model.entity.ModeloAvaliacao;
import com.controletcc.model.enums.TipoProfessor;
import com.controletcc.model.enums.TipoTcc;
import com.controletcc.repository.projection.ModeloAvaliacaoProjection;
import com.controletcc.repository.projection.ModeloAvaliacaoValidateProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface ModeloAvaliacaoRepository extends JpaRepository<ModeloAvaliacao, Long> {

    @Query(value = """  
            SELECT distinct
                ma.id as id,
                concat(at.faculdade, ' - ', at.curso) as descricaoAreaTcc,
                ma.tipoTccsNome as tipoTccsNome,
                ma.tipoProfessoresNome as tipoProfessoresNome
            FROM ModeloAvaliacao ma
            JOIN ma.tipoTccs tc
            JOIN ma.tipoProfessores tp
            JOIN ma.areaTcc at
                WHERE at.id in :idAreaTccList
                AND (:id is null or ma.id = :id)
                AND (:idAreaTcc is null or at.id = :idAreaTcc)
                AND (:tipoTcc is null or tc = :tipoTcc)
                AND (:tipoProfessor is null or tp = :tipoProfessor)
            """
    )
    Page<ModeloAvaliacaoProjection> search(List<Long> idAreaTccList,
                                           Long id,
                                           Long idAreaTcc,
                                           TipoTcc tipoTcc,
                                           TipoProfessor tipoProfessor,
                                           Pageable pageable);

    @Query(value = """
            SELECT distinct
                ma.id as id,
                tc as tipoTcc,
                tp as tipoProfessor
            FROM ModeloAvaliacao ma
            JOIN ma.tipoTccs tc
            JOIN ma.tipoProfessores tp
            WHERE (:id is null or ma.id != :id)
            AND ma.areaTcc.id = :idAreaTcc
            AND tc in :tipoTccs
            AND tp in :tipoProfessores
            """
    )
    List<ModeloAvaliacaoValidateProjection> getValidateByAreaTccAndTipoTccsAndTipoProfessoresAndNotId(Long idAreaTcc, Set<TipoTcc> tipoTccs, Set<TipoProfessor> tipoProfessores, Long id);

}
