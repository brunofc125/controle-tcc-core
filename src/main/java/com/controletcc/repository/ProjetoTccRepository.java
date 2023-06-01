package com.controletcc.repository;

import com.controletcc.model.entity.ProjetoTcc;
import com.controletcc.model.enums.SituacaoTcc;
import com.controletcc.model.enums.TipoTcc;
import com.controletcc.repository.projection.ProjetoTccExportProjection;
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
                s.situacaoTcc as situacaoTcc,
                (ap.id is not null) as apresentacaoAgendada
            FROM ProjetoTcc pt
            join pt.situacaoAtual s
            join pt.alunos a
            join pt.professorOrientador po
            join pt.professorSupervisor ps
            left join Apresentacao ap on ap.projetoTcc.id = pt.id and ap.tipoTcc = s.tipoTcc
                where ps.id = :idProfessorSupervisor
                and (:id is null or pt.id = :id)
                and (:tema is null or lower(pt.tema) like concat('%', trim(lower(:tema)),'%') )
                and (:anoPeriodo is null or lower(pt.anoPeriodo) like concat('%', trim(lower(:anoPeriodo)),'%') )
                and (:tipoTcc is null or s.tipoTcc = :tipoTcc)
                and (:situacaoTcc is null or s.situacaoTcc = :situacaoTcc)
                and (:nomeProfessorOrientador is null or lower(po.nome) like concat('%', trim(lower(:nomeProfessorOrientador)),'%') )
                and (:nomeAluno is null or lower(a.nome) like concat('%', trim(lower(:nomeAluno)),'%') )"""
    )
    Page<ProjetoTccProjection> searchSupervisor(Long idProfessorSupervisor,
                                                Long id,
                                                String tema,
                                                String anoPeriodo,
                                                TipoTcc tipoTcc,
                                                SituacaoTcc situacaoTcc,
                                                String nomeProfessorOrientador,
                                                String nomeAluno,
                                                Pageable pageable);

    @Query(value = """  
            SELECT distinct
                pt.id as id,
                pt.tema as tema,
                pt.alunosNome as alunos,
                po.nome as professorOrientador,
                ps.nome as professorSupervisor,
                pt.anoPeriodo as anoPeriodo,
                s.tipoTcc as tipoTcc,
                s.situacaoTcc as situacaoTcc,
                (ap.id is not null) as apresentacaoAgendada
            FROM ProjetoTcc pt
            join pt.situacaoAtual s
            join pt.alunos a
            join pt.professorOrientador po
            join pt.professorSupervisor ps
            left join Apresentacao ap on ap.projetoTcc.id = pt.id and ap.tipoTcc = s.tipoTcc
                where po.id = :idProfessorOrientador
                and (:id is null or pt.id = :id)
                and (:tema is null or lower(pt.tema) like concat('%', trim(lower(:tema)),'%') )
                and (:anoPeriodo is null or lower(pt.anoPeriodo) like concat('%', trim(lower(:anoPeriodo)),'%') )
                and (:tipoTcc is null or s.tipoTcc = :tipoTcc)
                and (:situacaoTcc is null or s.situacaoTcc = :situacaoTcc)
                and (:nomeAluno is null or lower(a.nome) like concat('%', trim(lower(:nomeAluno)),'%') )"""
    )
    Page<ProjetoTccProjection> searchOrientador(Long idProfessorOrientador,
                                                Long id,
                                                String tema,
                                                String anoPeriodo,
                                                TipoTcc tipoTcc,
                                                SituacaoTcc situacaoTcc,
                                                String nomeAluno,
                                                Pageable pageable);

    @Query(value = """  
            SELECT distinct
                pt.id as id,
                pt.tema as tema,
                pt.alunosNome as alunos,
                po.nome as professorOrientador,
                ps.nome as professorSupervisor,
                pt.anoPeriodo as anoPeriodo,
                s.tipoTcc as tipoTcc,
                mb.dataSolicitacao as dataSolicitacaoBanca,
                mb.dataConfirmacao as dataConfirmacaoBanca,
                s.situacaoTcc as situacaoTcc,
                (ap.id is not null) as apresentacaoAgendada
            FROM ProjetoTcc pt
            join pt.situacaoAtual s
            join pt.alunos a
            join pt.professorOrientador po
            join pt.professorSupervisor ps
            join MembroBanca mb on mb.projetoTcc.id = pt.id and mb.tipoTcc = s.tipoTcc
            join mb.professor pmb
            left join Apresentacao ap on ap.projetoTcc.id = pt.id and ap.tipoTcc = s.tipoTcc
                where pmb.id = :idProfessorMembroBanca
                and (:id is null or pt.id = :id)
                and (:tema is null or lower(pt.tema) like concat('%', trim(lower(:tema)),'%') )
                and (:anoPeriodo is null or lower(pt.anoPeriodo) like concat('%', trim(lower(:anoPeriodo)),'%') )
                and (:tipoTcc is null or s.tipoTcc = :tipoTcc)
                and (:situacaoTcc is null or s.situacaoTcc = :situacaoTcc)
                and (:nomeProfessorOrientador is null or lower(po.nome) like concat('%', trim(lower(:nomeProfessorOrientador)),'%') )
                and (:nomeAluno is null or lower(a.nome) like concat('%', trim(lower(:nomeAluno)),'%') )
                and (:situacaoSolicitacaoBanca is null or (:situacaoSolicitacaoBanca = 'CONFIRMADO' and mb.dataConfirmacao is not null) or (:situacaoSolicitacaoBanca = 'NAO_CONFIRMADO' and mb.dataConfirmacao is null))"""
    )
    Page<ProjetoTccProjection> searchMembroBanca(Long idProfessorMembroBanca,
                                                 Long id,
                                                 String tema,
                                                 String anoPeriodo,
                                                 TipoTcc tipoTcc,
                                                 SituacaoTcc situacaoTcc,
                                                 String nomeProfessorOrientador,
                                                 String nomeAluno,
                                                 String situacaoSolicitacaoBanca,
                                                 Pageable pageable);

    @Query(value = """  
            SELECT distinct
                pt.id as id,
                pt.tema as tema,
                pt.alunosNome as alunos,
                po.nome as professorOrientador,
                ps.nome as professorSupervisor,
                pt.anoPeriodo as anoPeriodo,
                s.tipoTcc as tipoTcc,
                s.situacaoTcc as situacaoTcc,
                (ap.id is not null) as apresentacaoAgendada
            FROM ProjetoTcc pt
            join pt.situacaoAtual s
            join pt.alunos a
            join pt.professorOrientador po
            join pt.professorSupervisor ps
            left join Apresentacao ap on ap.projetoTcc.id = pt.id and ap.tipoTcc = s.tipoTcc
                where a.id = :idAluno
                and (:id is null or pt.id = :id)
                and (:tema is null or lower(pt.tema) like concat('%', trim(lower(:tema)),'%') )
                and (:anoPeriodo is null or lower(pt.anoPeriodo) like concat('%', trim(lower(:anoPeriodo)),'%') )
                and (:tipoTcc is null or s.tipoTcc = :tipoTcc)
                and (:situacaoTcc is null or s.situacaoTcc = :situacaoTcc)"""
    )
    Page<ProjetoTccProjection> searchAluno(Long idAluno,
                                           Long id,
                                           String tema,
                                           String anoPeriodo,
                                           TipoTcc tipoTcc,
                                           SituacaoTcc situacaoTcc,
                                           Pageable pageable);

    @Query(value = """  
            SELECT distinct
                pt.id as id,
                pt.tema as tema,
                pt.alunosNome as alunos,
                po.nome as professorOrientador,
                ps.nome as professorSupervisor,
                pt.membrosBancaNome as membrosBanca,
                pt.anoPeriodo as anoPeriodo,
                s.tipoTcc as tipoTcc,
                s.situacaoTcc as situacaoTcc
            FROM ProjetoTcc pt
            join pt.situacaoAtual s
            join pt.alunos a
            join pt.professorOrientador po
            join pt.professorSupervisor ps
            left join MembroBanca mb on mb.projetoTcc.id = pt.id and mb.tipoTcc = s.tipoTcc
                where 1 = 1
                and (:idSupervisor is null or ps.id = :idSupervisor)
                and (:idOrientador is null or po.id = :idOrientador)
                and (:idMembroBanca is null or mb.professor.id = :idMembroBanca)
                and (:id is null or pt.id = :id)
                and (:tema is null or lower(pt.tema) like concat('%', trim(lower(:tema)),'%') )
                and (:anoPeriodo is null or lower(pt.anoPeriodo) like concat('%', trim(lower(:anoPeriodo)),'%') )
                and (:tipoTcc is null or s.tipoTcc = :tipoTcc)
                and (:situacaoTcc is null or s.situacaoTcc = :situacaoTcc)
                and (:nomeProfessorOrientador is null or lower(po.nome) like concat('%', trim(lower(:nomeProfessorOrientador)),'%') )
                and (:nomeAluno is null or lower(a.nome) like concat('%', trim(lower(:nomeAluno)),'%') )"""
    )
    List<ProjetoTccExportProjection> export(Long id,
                                            String tema,
                                            String anoPeriodo,
                                            TipoTcc tipoTcc,
                                            SituacaoTcc situacaoTcc,
                                            String nomeProfessorOrientador,
                                            String nomeAluno,
                                            Long idSupervisor,
                                            Long idOrientador,
                                            Long idMembroBanca);

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

    @Query(value = """  
            SELECT count(distinct a.id) > 0
            FROM ProjetoTcc pt
            JOIN pt.situacaoAtual s
            LEFT JOIN Apresentacao a on a.projetoTcc.id = pt.id and a.tipoTcc = s.tipoTcc
                WHERE pt.id = :idProjetoTcc
            """
    )
    boolean existsApresentacaoAgendada(Long idProjetoTcc);

}
