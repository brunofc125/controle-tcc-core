package com.controletcc.repository;

import com.controletcc.model.entity.Professor;
import com.controletcc.repository.projection.ProfessorProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    @Query(value = """  
            SELECT
                p.id as id,
                p.nome as nome,
                p.cpf as cpf,
                p.email as email,
                p.supervisorTcc as supervisorTcc,
                u.id as idUser,
                u.enabled as userEnabled
            FROM Professor p
            JOIN p.usuario u
                where (:id is null or p.id = :id)
                and (:nome is null or lower(p.nome) like concat('%', trim(lower(:nome)),'%') )
                and (:cpf is null or lower(p.cpf) like concat('%', trim(lower(:cpf)),'%') )
                and (:rg is null or lower(p.rg) like concat('%', trim(lower(:rg)),'%') )
                and (:email is null or lower(p.email) like concat('%', trim(lower(:email)),'%') )
                and (:supervisor is null or p.supervisorTcc = :supervisor)"""
    )
    Page<ProfessorProjection> search(Long id,
                                     String nome,
                                     String cpf,
                                     String rg,
                                     String email,
                                     Boolean supervisor,
                                     Pageable pageable);

    boolean existsByCpf(String cpf);

    boolean existsByCpfAndIdNot(String cpf, Long id);

    Professor getProfessorByUsuarioId(Long idUsuario);

    @Query(value = """
            SELECT p
            FROM Professor p
            JOIN p.areas at
            WHERE at.id = :idAreaTcc
              AND p.supervisorTcc = true
            ORDER BY p.nome
            """
    )
    List<Professor> getSupervisoresByIdAreaTcc(Long idAreaTcc);

    @Query(value = """
            SELECT p
            FROM Professor p
            JOIN p.areas at
            WHERE at.id = :idAreaTcc
              AND p.id not in :idProfessores
            ORDER BY p.nome
            """
    )
    List<Professor> getAllByAreaTccAndNotProfessores(Long idAreaTcc, List<Long> idProfessores);

}
