-- atualizar o user_roles
insert into user_role (id_user, id_role)
select u.id,
       r.id
from "user" u
         join "role" r on
        (u."type" = 'ADMIN'
            and r."admin" = true)
        or (u."type" = 'SUPERVISOR'
        and r.supervisor = true)
        or (u."type" = 'PROFESSOR'
        and r.professor = true)
        or (u."type" = 'ALUNO'
        and r.aluno = true)
         left join user_role ur on
            ur.id_user = u.id
        and ur.id_role = r.id
where ur is null
order by 2, 1;

-- roles
INSERT INTO public."role" (id, "admin", supervisor, professor, aluno)
VALUES ('usuario.create', true, true, false, false);
INSERT INTO public."role" (id, "admin", supervisor, professor, aluno)
VALUES ('usuario.read', true, true, false, false);
INSERT INTO public."role" (id, "admin", supervisor, professor, aluno)
VALUES ('professor.create', true, true, false, false);
INSERT INTO public."role" (id, "admin", supervisor, professor, aluno)
VALUES ('professor.read', true, true, false, false);
INSERT INTO public."role" (id, "admin", supervisor, professor, aluno)
VALUES ('professor.import', true, true, false, false);
INSERT INTO public."role" (id, "admin", supervisor, professor, aluno)
VALUES ('professor.perfil', false, true, true, false);
INSERT INTO public."role" (id, "admin", supervisor, professor, aluno)
VALUES ('aluno.create', false, true, false, false);
INSERT INTO public."role" (id, "admin", supervisor, professor, aluno)
VALUES ('aluno.read', true, true, true, false);
INSERT INTO public."role" (id, "admin", supervisor, professor, aluno)
VALUES ('aluno.import', false, true, false, false);
INSERT INTO public."role" (id, "admin", supervisor, professor, aluno)
VALUES ('aluno.perfil', false, false, false, true);
INSERT INTO public."role" (id, "admin", supervisor, professor, aluno)
VALUES ('modelo-documento.create', false, true, false, false);
INSERT INTO public."role" (id, "admin", supervisor, professor, aluno)
VALUES ('modelo-documento.read', false, true, true, false);
INSERT INTO public."role" (id, "admin", supervisor, professor, aluno)
VALUES ('area-tcc.create', false, true, false, false);
INSERT INTO public."role" (id, "admin", supervisor, professor, aluno)
VALUES ('area-tcc.read', true, true, true, false);
INSERT INTO public."role" (id, "admin", supervisor, professor, aluno)
VALUES ('projeto-tcc.read', false, true, true, true);
INSERT INTO public."role" (id, "admin", supervisor, professor, aluno)
VALUES ('projeto-tcc.create', false, true, true, false);
INSERT INTO public."role" (id, "admin", supervisor, professor, aluno)
VALUES ('anexo-geral.create', false, true, true, false);
INSERT INTO public."role" (id, "admin", supervisor, professor, aluno)
VALUES ('anexo-geral.delete', false, true, true, false);
INSERT INTO public."role" (id, "admin", supervisor, professor, aluno)
VALUES ('anexo-geral.read', false, true, true, true);
INSERT INTO public."role" (id, "admin", supervisor, professor, aluno)
VALUES ('versao-tcc.create', false, true, true, true);
INSERT INTO public."role" (id, "admin", supervisor, professor, aluno)
VALUES ('versao-tcc.read', false, true, true, true);
INSERT INTO public."role" (id, "admin", supervisor, professor, aluno)
VALUES ('versao-tcc-observacao.create', false, true, true, false);
INSERT INTO public."role" (id, "admin", supervisor, professor, aluno)
VALUES ('versao-tcc-observacao.read', false, true, true, true);
INSERT INTO public."role" (id, "admin", supervisor, professor, aluno)
VALUES ('agenda-apresentacao.create', false, true, false, false);
INSERT INTO public."role" (id, "admin", supervisor, professor, aluno)
VALUES ('agenda-apresentacao.read', false, true, true, false);

