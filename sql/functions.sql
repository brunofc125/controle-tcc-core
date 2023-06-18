-- Função para consultar as disponibilidades dos professores
DROP FUNCTION public.get_disponibilidades;

CREATE OR REPLACE FUNCTION get_disponibilidades(id_professores BIGINT[], id_projeto_tcc BIGINT, data_inicio TIMESTAMP,
                                                data_fim TIMESTAMP, id_agenda BIGINT)
    RETURNS TABLE
            (
                date_event      TIMESTAMP,
                ids_professores TEXT,
                qtd_professor   BIGINT
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT data_event,
               string_agg(p.id::varchar, ',' order by p.nome) ids_professores,
               count(p.id)                                    qtd_professor
        FROM professor_disponibilidade pd
                 JOIN professor p ON p.id = pd.id_professor
                 JOIN generate_series(pd.data_inicial, pd.data_final, interval '1 hour') AS data_event
                      ON data_event >= pd.data_inicial AND data_event < pd.data_final
                 LEFT JOIN (SELECT DISTINCT a.id           AS id_apresentacao,
                                            a.data_inicial as data_inicial,
                                            a.data_final   as data_final,
                                            po.id          AS id_po,
                                            ps.id          AS id_ps,
                                            pmb.id         AS id_pmb
                            FROM apresentacao a
                                     JOIN projeto_tcc pt ON pt.id = a.id_projeto_tcc
                                     JOIN projeto_tcc_situacao pts ON pts.id = pt.id_situacao_atual
                                     JOIN professor po ON pt.id_professor_orientador = po.id
                                     JOIN professor ps ON pt.id_professor_supervisor = ps.id
                                     LEFT JOIN membro_banca mb
                                               ON mb.id_projeto_tcc = pt.id AND mb.tipo_tcc = pts.tipo_tcc
                                     LEFT JOIN professor pmb ON mb.id_professor = pmb.id
                            WHERE pt.id != $2) AS tb_apresentacao
                           ON data_event >= tb_apresentacao.data_inicial
                               AND data_event < tb_apresentacao.data_final
                               AND (tb_apresentacao.id_po = p.id OR tb_apresentacao.id_ps = p.id OR
                                    tb_apresentacao.id_pmb = p.id)
                 LEFT JOIN (SELECT DISTINCT r.id           AS id_restricao,
                                            r.data_inicial AS data_inicial,
                                            r.data_final   AS data_final
                            FROM agenda_apresentacao_restricao r
                            WHERE r.id_agenda_apresentacao = $5) AS tb_restricao
                           ON data_event >= tb_restricao.data_inicial
                               AND data_event < tb_restricao.data_final
        WHERE p.id = ANY ($1)
          AND tb_apresentacao.id_apresentacao IS NULL
          AND tb_restricao.id_restricao IS NULL
          AND data_event >= $3
          AND data_event < $4
          AND EXTRACT(HOUR FROM data_event) >= EXTRACT(HOUR FROM $3)
          AND EXTRACT(HOUR FROM data_event) < EXTRACT(HOUR FROM $4)
        group by data_event
        order by data_event;
END;
$$ LANGUAGE plpgsql;

-- Função para converter record em array de bigint
DROP FUNCTION public.to_bigint_array;

CREATE OR REPLACE FUNCTION to_bigint_array(variadic tupla bigint[])
    RETURNS bigint[]
AS
$$
DECLARE
    resultado bigint[];
BEGIN
    SELECT ARRAY(SELECT unnest(tupla)::bigint)
    INTO resultado;
    RETURN resultado;
END;
$$ LANGUAGE plpgsql;

-- Função para criar novo nome de usuário
DROP FUNCTION public.get_new_user_name;

CREATE OR REPLACE FUNCTION get_new_user_name(first_name varchar) RETURNS TEXT AS
$$
DECLARE
    new_username text;
BEGIN
    new_username := concat(first_name || '_', array_to_string(array(select case
                                                                               when tb.letter
                                                                                   then chr((random() * 25 + 97)::integer)
                                                                               else chr((random() * 9 + 48)::integer) end
                                                                    from (select *, ((random() * 9)::integer < 5) as letter
                                                                          from generate_series(1, 6)) as tb),
                                                              ''));
    WHILE EXISTS(SELECT 1 FROM "user" u WHERE u.user_name = new_username)
        LOOP
            new_username := concat(first_name || '_', array_to_string(array(select case
                                                                                       when tb.letter then
                                                                                           chr((random() * 25 + 97)::integer)
                                                                                       else
                                                                                           chr((random() * 9 + 48)::integer)
                                                                                       end
                                                                            from (select *, ((random() * 9)::integer < 5) as letter
                                                                                  from generate_series(1, 6)) as tb),
                                                                      ''));
        END LOOP;
    return new_username;
END;
$$ LANGUAGE PLPGSQL;
