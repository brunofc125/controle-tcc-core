package com.controletcc.repository;

import com.controletcc.model.entity.ModeloItemAvaliacao;
import com.controletcc.model.enums.TipoProfessor;
import com.controletcc.model.enums.TipoTcc;
import com.controletcc.repository.projection.ModeloAvaliacaoValidateProjection;
import com.controletcc.repository.projection.ModeloItemAvaliacaoProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface ModeloItemAvaliacaoRepository extends JpaRepository<ModeloItemAvaliacao, Long> {

    @Query(value = """  
            SELECT distinct
                mia.id as id,
                mia.tipoTccsNome as tipoTccsNome,
                mia.tipoProfessoresNome as tipoProfessoresNome
            FROM ModeloItemAvaliacao mia
            JOIN mia.tipoTccs tc
            JOIN mia.tipoProfessores tp
            WHERE mia.dataExclusao is null
                AND mia.modeloAvaliacao.id in :idModeloAvaliacao
                AND (:tipoTcc is null or tc = :tipoTcc)
                AND (:tipoProfessor is null or tp = :tipoProfessor)
            """
    )
    Page<ModeloItemAvaliacaoProjection> search(Long idModeloAvaliacao,
                                               TipoTcc tipoTcc,
                                               TipoProfessor tipoProfessor,
                                               Pageable pageable);

    @Query(value = """
            SELECT distinct
                mia.id as id,
                tc as tipoTcc,
                tp as tipoProfessor
            FROM ModeloItemAvaliacao mia
            JOIN mia.tipoTccs tc
            JOIN mia.tipoProfessores tp
            WHERE mia.dataExclusao is null
                AND (:id is null or mia.id != :id)
                AND tc in :tipoTccs
                AND tp in :tipoProfessores
            """
    )
    List<ModeloAvaliacaoValidateProjection> getValidateByTipoTccsAndTipoProfessoresAndNotId(Set<TipoTcc> tipoTccs, Set<TipoProfessor> tipoProfessores, Long id);

    @Query(value = """
            SELECT distinct mia
            FROM ModeloItemAvaliacao mia
            JOIN mia.tipoTccs tc
            JOIN mia.modeloAvaliacao ma
            WHERE mia.dataExclusao is null
                AND ma.dataExclusao is null
                AND ma.areaTcc.id = :idAreaTcc
                AND tc = :tipoTcc
            """
    )
    List<ModeloItemAvaliacao> getItensByAreaTccAndTipoTcc(Long idAreaTcc, TipoTcc tipoTcc);
}
