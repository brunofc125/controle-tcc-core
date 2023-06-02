package com.controletcc.repository;

import com.controletcc.model.entity.ProjetoTccNota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjetoTccNotaRepository extends JpaRepository<ProjetoTccNota, Long> {

    @Query(value = """
                SELECT ptn
                FROM ProjetoTccNota ptn
                JOIN ptn.projetoTcc pt
                JOIN pt.situacaoAtual sa
                WHERE pt.id = :idProjetoTcc
                    AND sa.tipoTcc = ptn.tipoTcc
            """)
    ProjetoTccNota getProjetoTccNotaByProjetoTccId(Long idProjetoTcc);

}
