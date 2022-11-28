package com.controletcc.repository;

import com.controletcc.model.entity.VersaoTcc;
import com.controletcc.repository.projection.VersaoTccProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VersaoTccRepository extends JpaRepository<VersaoTcc, Long> {

    @Query(value = """  
            SELECT distinct
                vt.id as id,
                vt.observacao as observacao,
                vt.versao as versao,
                ( case when vt.professorOrientador is not null then 'Orientador'
                 when vt.aluno is not null then 'Aluno'
                 else '-' end ) as publicadoPelo,
                vt.dataInclusao as dataInclusao
            FROM VersaoTcc vt
                where vt.projetoTcc.id = :idProjetoTcc
                and (:versao is null or vt.versao = :versao)
                and (:ultimaVersao is false or vt.versao = (select max(vt_.versao) from VersaoTcc vt_ where vt_.projetoTcc.id = vt.projetoTcc.id))"""
    )
    Page<VersaoTccProjection> search(Long versao,
                                     boolean ultimaVersao,
                                     Long idProjetoTcc,
                                     Pageable pageable);

    VersaoTcc getFirstByProjetoTccIdOrderByVersaoDesc(Long idProjetoTcc);

}
