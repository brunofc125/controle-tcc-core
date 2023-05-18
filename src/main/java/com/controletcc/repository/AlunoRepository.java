package com.controletcc.repository;

import com.controletcc.model.entity.Aluno;
import com.controletcc.repository.projection.AlunoProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    @Query(value = """  
            SELECT
                a.id as id,
                a.nome as nome,
                a.email as email,
                a.matricula as matricula,
                concat(at.faculdade, ' - ', at.curso) as descricaoAreaTcc,
                u.id as idUser,
                u.enabled as userEnabled
            FROM Aluno a
            JOIN a.usuario u
            JOIN a.areaTcc at
                where (:id is null or a.id = :id)
                and (:nome is null or lower(a.nome) like concat('%', trim(lower(:nome)),'%') )
                and (:email is null or lower(a.email) like concat('%', trim(lower(:email)),'%') )
                and (:matricula is null or lower(a.matricula) like concat('%', trim(lower(:matricula)),'%') )
                and (:idAreaTcc is null or at.id = :idAreaTcc)"""
    )
    Page<AlunoProjection> search(Long id,
                                 String nome,
                                 String email,
                                 String matricula,
                                 Long idAreaTcc,
                                 Pageable pageable);

    boolean existsByMatricula(String matricula);

    boolean existsByMatriculaAndIdNot(String matricula, Long id);

    Aluno getAlunoByUsuarioId(Long idUsuario);

    List<Aluno> getAllByAreaTccId(Long idAreaTcc);

}
