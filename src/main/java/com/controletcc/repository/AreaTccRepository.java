package com.controletcc.repository;

import com.controletcc.model.entity.AreaTcc;
import com.controletcc.repository.projection.AreaTccProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AreaTccRepository extends JpaRepository<AreaTcc, Long> {

    @Query(value = """  
            SELECT
                at.id as id,
                at.faculdade as faculdade,
                at.curso as curso
            FROM AreaTcc at
                where (:id is null or at.id = :id)
                and (:faculdade is null or lower(at.faculdade) like concat('%', trim(lower(:faculdade)),'%') )
                and (:curso is null or lower(at.curso) like concat('%', trim(lower(:curso)),'%') )"""
    )
    Page<AreaTccProjection> search(Long id,
                                   String faculdade,
                                   String curso,
                                   Pageable pageable);

    boolean existsByFaculdadeAndCurso(String faculdade, String curso);

    boolean existsByFaculdadeAndCursoAndIdNot(String faculdade, String curso, Long id);

}
