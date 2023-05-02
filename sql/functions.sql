-- Função para consultar as disponibilidades dos professores
DROP FUNCTION public.get_disponibilidades;

CREATE OR REPLACE FUNCTION get_disponibilidades(id_professores BIGINT[], id_projeto_tcc BIGINT, data_inicio TIMESTAMP,
                                                data_fim TIMESTAMP)
    RETURNS TABLE
            (
                date_event        TIMESTAMP,
                nomes_professores TEXT,
                qtd_professor     BIGINT
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT data_event,
               string_agg(p.nome, '<br>' order by p.nome) nomes_professores,
               count(p.id)                                qtd_professor
        FROM professor_disponibilidade pd
                 JOIN professor p ON p.id = pd.id_professor
                 JOIN generate_series(pd.data_inicial, pd.data_final, interval '1 hour') AS data_event
                      ON data_event >= pd.data_inicial AND data_event < pd.data_final
        WHERE p.id = ANY ($1)
          AND NOT EXISTS(
                SELECT a.id
                FROM apresentacao a
                         JOIN projeto_tcc pt ON pt.id = a.id_projeto_tcc
                         JOIN professor po ON pt.id_professor_orientador = po.id
                         JOIN professor ps ON pt.id_professor_supervisor = ps.id
                         LEFT JOIN membro_banca mb ON mb.id_projeto_tcc = pt.id
                         LEFT JOIN professor pmb ON mb.id_professor = pmb.id
                WHERE pt.id != $2
                  AND data_event >= a.data_inicial
                  AND data_event < a.data_final
                  AND (
                            po.id = p.id
                        OR ps.id = p.id
                        OR pmb.id = p.id
                    )
            )
          AND data_event >= $3
          AND data_event < $4
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
