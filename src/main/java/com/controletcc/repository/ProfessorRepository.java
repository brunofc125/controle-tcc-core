package com.controletcc.repository;

import com.controletcc.dto.grid.ProfessorGridDTO;
import com.controletcc.model.entity.Professor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    @Query(value = """  
            SELECT new com.controletcc.dto.grid.ProfessorGridDTO(
                p.id,
                p.nome,
                p.cpf,
                p.email,
                p.supervisorTcc
            ) FROM Professor p
                where (:id is null or p.id = :id)
                and (:nome is null or lower(p.nome) like concat('%', lower(:nome),'%') )
                and (:cpf is null or lower(p.cpf) like concat('%', lower(:cpf),'%') )
                and (:rg is null or lower(p.rg) like concat('%', lower(:rg),'%') )
                and (:email is null or lower(p.email) like concat('%', lower(:email),'%') )
                and (:supervisor is null or p.supervisorTcc = :supervisor)"""
    )
    Page<ProfessorGridDTO> search(@Param("id") Long id,
                                  @Param("nome") String nome,
                                  @Param("cpf") String cpf,
                                  @Param("rg") String rg,
                                  @Param("email") String email,
                                  @Param("supervisor") Boolean supervisor,
                                  Pageable pageable);

}
/**/