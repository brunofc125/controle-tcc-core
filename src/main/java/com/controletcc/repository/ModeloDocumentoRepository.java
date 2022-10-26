package com.controletcc.repository;

import com.controletcc.model.entity.ModeloDocumento;
import com.controletcc.model.enums.TipoTcc;
import com.controletcc.repository.projection.ModeloDocumentoProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ModeloDocumentoRepository extends JpaRepository<ModeloDocumento, Long> {

    @Query(value = """  
            SELECT
                md.id as id,
                md.nome as nome,
                md.descricao as descricao,
                md.tipoTcc as tipoTcc
            FROM ModeloDocumento md
                where (:id is null or md.id = :id)
                and (:nome is null or lower(md.nome) like concat('%', trim(lower(:nome)),'%') )
                and (:descricao is null or lower(md.descricao) like concat('%', trim(lower(:descricao)),'%') )
                and (:tipoTcc is null or md.tipoTcc = :tipoTcc)"""
    )
    Page<ModeloDocumentoProjection> search(Long id,
                                           String nome,
                                           String descricao,
                                           TipoTcc tipoTcc,
                                           Pageable pageable);

    boolean existsByNome(String nome);

    boolean existsByNomeAndIdNot(String nome, Long id);

}
