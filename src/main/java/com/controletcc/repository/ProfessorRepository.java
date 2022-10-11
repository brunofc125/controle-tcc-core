package com.controletcc.repository;

import com.controletcc.model.entity.Professor;
import com.controletcc.repository.projection.ProfessorProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    @Query(value = """  
            SELECT
                p.id as id,
                p.nome as nome,
                p.cpf as cpf,
                p.email as email,
                p.supervisorTcc as supervisorTcc
            FROM Professor p
                where (:id is null or p.id = :id)
                and (:nome is null or lower(p.nome) like concat('%', trim(lower(:nome)),'%') )
                and (:cpf is null or lower(p.cpf) like concat('%', trim(lower(:cpf)),'%') )
                and (:rg is null or lower(p.rg) like concat('%', trim(lower(:rg)),'%') )
                and (:email is null or lower(p.email) like concat('%', trim(lower(:email)),'%') )
                and (:supervisor is null or p.supervisorTcc = :supervisor)"""

    )
    Page<ProfessorProjection> search(@Param("id") Long id,
                                     @Param("nome") String nome,
                                     @Param("cpf") String cpf,
                                     @Param("rg") String rg,
                                     @Param("email") String email,
                                     @Param("supervisor") Boolean supervisor,
                                     Pageable pageable);

    boolean existsByCpf(String cpf);

    boolean existsByCpfAndIdNot(String cpf, Long id);

}
/**/