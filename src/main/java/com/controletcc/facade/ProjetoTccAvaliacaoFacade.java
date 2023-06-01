package com.controletcc.facade;

import com.controletcc.error.BusinessException;
import com.controletcc.model.entity.ModeloItemAvaliacao;
import com.controletcc.model.entity.Professor;
import com.controletcc.model.entity.ProjetoTcc;
import com.controletcc.model.enums.TipoProfessor;
import com.controletcc.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class ProjetoTccAvaliacaoFacade {

    private final ProjetoTccAvaliacaoService projetoTccAvaliacaoService;

    private final ProjetoTccAspectoAvaliacaoService projetoTccAspectoAvaliacaoService;

    private final ProjetoTccService projetoTccService;

    private final ModeloItemAvaliacaoService modeloItemAvaliacaoService;

    private final MembroBancaService membroBancaService;

    public void iniciarEtapaAvaliacao(Long idProjetoTcc) throws Exception {
        if (!projetoTccService.existsApresentacaoAgendada(idProjetoTcc)) {
            throw new BusinessException("Não foi agendada a apresentação deste TCC");
        }
        if (!projetoTccAvaliacaoService.existsAvaliacaoIniciada(idProjetoTcc)) {
            var projetoTcc = projetoTccService.getById(idProjetoTcc);
            var itens = modeloItemAvaliacaoService.getItensByAreaTccAndTipoTcc(projetoTcc.getIdAreaTcc(), projetoTcc.getTipoTcc());
            if (itens == null || itens.isEmpty()) {
                throw new BusinessException("Não foi cadastrado nenhum modelo de avaliação para este TCC");
            }
            var orientador = projetoTcc.getProfessorOrientador();
            var supervisor = projetoTcc.getProfessorSupervisor();
            var membroBancaList = membroBancaService.getProfessoresByIdProjetoTcc(idProjetoTcc);
            if (itens.stream().allMatch(i -> i.getTipoProfessores().size() == 1 && i.getTipoProfessores().contains(TipoProfessor.MEMBRO_BANCA)) && membroBancaList.size() == 0) {
                throw new BusinessException("Foi encontrado apenas modelos de avaliação para membro de banca, porém o TCC selecionado não contém nenhum membro de banca");
            }
            generate(itens, projetoTcc, TipoProfessor.ORIENTADOR, orientador);
            generate(itens, projetoTcc, TipoProfessor.SUPERVISOR, supervisor);
            if (membroBancaList != null && !membroBancaList.isEmpty()) {
                for (var membroBanca : membroBancaList) {
                    generate(itens, projetoTcc, TipoProfessor.MEMBRO_BANCA, membroBanca);
                }
            }
        }
    }

    private void generate(List<ModeloItemAvaliacao> itens, ProjetoTcc projetoTcc, TipoProfessor tipoProfessor, Professor professor) {
        var item = itens.stream().filter(i -> i.getTipoProfessores().contains(tipoProfessor)).findFirst().orElse(null);
        if (item != null) {
            var projetoTccAvaliacao = projetoTccAvaliacaoService.generate(item, projetoTcc.getTipoTcc(), tipoProfessor, projetoTcc, professor);
            projetoTccAspectoAvaliacaoService.generateByList(projetoTccAvaliacao, item.getModeloAspectosAvaliacao());
        }
    }

}
