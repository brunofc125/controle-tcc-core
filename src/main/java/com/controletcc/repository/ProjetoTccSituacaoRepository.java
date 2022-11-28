package com.controletcc.repository;

import com.controletcc.model.entity.ProjetoTccSituacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjetoTccSituacaoRepository extends JpaRepository<ProjetoTccSituacao, Long> {

    @Query(value = """
            SELECT pt.situacaoAtual
            FROM ProjetoTcc pt
            WHERE pt.id = :idProjetoTcc""")
    public ProjetoTccSituacao getSituacaoAtualByIdProjetoTcc(Long idProjetoTcc);

}
