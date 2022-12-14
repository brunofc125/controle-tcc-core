DROP VIEW IF EXISTS vw_professor_compromissos;

CREATE OR REPLACE VIEW vw_professor_compromissos AS
select distinct ('COMPROMISSO_PESSOAL:' || pc.id) as identifier,
                pc.id                             as id,
                'COMPROMISSO_PESSOAL'             as tipo_compromisso,
                cast(null as bigint)              as id_projeto_tcc,
                cast(null as bigint)              as id_agenda_apresentacao,
                null                              as tipo_tcc,
                pc.descricao                      as descricao,
                null                              as alunos_nome,
                pc.id_professor                   as id_professor,
                cast(null as bigint)              as id_professor_supervisor,
                cast(null as bigint)              as id_professor_orientador,
                cast(null as bigint)              as id_professor_banca,
                pc.data_inicial                   as data_inicial,
                pc.data_final                     as data_final
from professor_compromisso pc
union
select distinct ('APRESENTACAO:' || a.id || ':' || coalesce(pb.id, 0)) as identifier,
                a.id                                                   as id,
                'APRESENTACAO'                                         as tipo_compromisso,
                a.id_projeto_tcc                                       as id_projeto_tcc,
                a.id_agenda_apresentacao                               as id_agenda_apresentacao,
                a.tipo_tcc                                             as tipo_tcc,
                pt.tema                                                as descricao,
                (select string_agg(a_.nome, ', ')
                 from aluno a_
                          join projeto_tcc_aluno pta on pta.id_aluno = a_.id
                 where pta.id_projeto_tcc = a.id_projeto_tcc)          as alunos_nome,
                cast(null as bigint)                                   as id_professor,
                ps.id                                                  as id_professor_supervisor,
                po.id                                                  as id_professor_orientador,
                pb.id                                                  as id_professor_banca,
                a.data_inicial                                         as data_inicial,
                a.data_final                                           as data_final
from apresentacao a
         join projeto_tcc pt on
    pt.id = a.id_projeto_tcc
         join professor po on
    po.id = pt.id_professor_orientador
         join professor ps on
    ps.id = pt.id_professor_supervisor
         left join membro_banca mb on
            mb.id_projeto_tcc = pt.id
        and mb.data_confirmacao is not null
         left join professor pb on
    pb.id = mb.id_professor;