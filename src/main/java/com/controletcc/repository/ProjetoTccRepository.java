package com.controletcc.repository;

import com.controletcc.model.entity.ProjetoTcc;
import com.controletcc.model.enums.SituacaoTcc;
import com.controletcc.model.enums.TipoTcc;
import com.controletcc.repository.projection.ProjetoTccProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjetoTccRepository extends JpaRepository<ProjetoTcc, Long> {

    @Query(value = """  
            SELECT distinct
                pt.id as id,
                pt.tema as tema,
                pt.alunosNome as alunos,
                po.nome as professorOrientador,
                ps.nome as professorSupervisor,
                pt.anoPeriodo as anoPeriodo,
                s.tipoTcc as tipoTcc,
                s.situacaoTcc as situacaoTcc
            FROM ProjetoTcc pt
            join pt.situacaoAtual s
            join pt.alunos a
            join pt.professorOrientador po
            join pt.professorSupervisor ps
                where (:id is null or pt.id = :id)
                and (:tema is null or lower(pt.tema) like concat('%', trim(lower(:tema)),'%') )
                and (:anoPeriodo is null or lower(pt.anoPeriodo) like concat('%', trim(lower(:anoPeriodo)),'%') )
                and (:tipoTcc is null or s.tipoTcc = :tipoTcc)
                and (:situacaoTcc is null or s.situacaoTcc = :situacaoTcc)
                and (:idProfessorOrientador is null or po.id = :idProfessorOrientador)
                and (:nomeProfessorOrientador is null or lower(po.nome) like concat('%', trim(lower(:nomeProfessorOrientador)),'%') )
                and (:idProfessorSupervisor is null or ps.id = :idProfessorSupervisor or po.id = :idProfessorSupervisor)
                and (:idAluno is null or a.id = :idAluno)
                and (:nomeAluno is null or lower(a.nome) like concat('%', trim(lower(:nomeAluno)),'%') )"""
    )
    Page<ProjetoTccProjection> search(Long id,
                                      String tema,
                                      String anoPeriodo,
                                      TipoTcc tipoTcc,
                                      SituacaoTcc situacaoTcc,
                                      Long idProfessorOrientador,
                                      String nomeProfessorOrientador,
                                      Long idProfessorSupervisor,
                                      Long idAluno,
                                      String nomeAluno,
                                      Pageable pageable);

    @Query(value = """  
            SELECT count(distinct pt.id) > 0
            FROM ProjetoTcc pt
            join pt.situacaoAtual s
            join pt.alunos a
                where a.id = :idAluno
                and (
                    (s.tipoTcc = 'QUALIFICACAO' and s.situacaoTcc in :situacaoTccsAtivosQualificacao)
                    or (s.tipoTcc = 'DEFESA' and s.situacaoTcc in :situacaoTccsAtivosDefesa)
                )
               """
    )
    boolean existsAtivoByIdAluno(Long idAluno, List<SituacaoTcc> situacaoTccsAtivosQualificacao, List<SituacaoTcc> situacaoTccsAtivosDefesa);

}
