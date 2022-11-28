package com.controletcc.repository;

import com.controletcc.model.entity.AnexoGeral;
import com.controletcc.model.enums.TipoTcc;
import com.controletcc.repository.projection.AnexoGeralProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AnexoGeralRepository extends JpaRepository<AnexoGeral, Long> {

    @Query(value = """  
            SELECT distinct
                ag.id as id,
                ag.descricao as descricao,
                md.tipoTccsNome as tipoTccsNome,
                md.descricao as modelo,
                p.nome as professor
            FROM AnexoGeral ag
            JOIN ag.professor p
            LEFT JOIN ag.modeloDocumento md
            LEFT JOIN md.tipoTccs tc
                where ag.projetoTcc.id = :idProjetoTcc
                and ag.dataExclusao is null
                and (:id is null or ag.id = :id)
                and (:descricao is null or lower(ag.descricao) like concat('%', trim(lower(:descricao)),'%')
                    or lower(md.descricao) like concat('%', trim(lower(:descricao)),'%')  )
                and (:tipoTcc is null or tc = :tipoTcc)"""
    )
    Page<AnexoGeralProjection> search(Long id,
                                      String descricao,
                                      TipoTcc tipoTcc,
                                      Long idProjetoTcc,
                                      Pageable pageable);

    boolean existsByProjetoTccIdAndDescricaoIgnoreCaseAndDataExclusaoIsNull(Long idProjetoTcc, String descricao);

}
