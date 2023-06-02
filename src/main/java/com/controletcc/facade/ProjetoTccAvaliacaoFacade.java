package com.controletcc.facade;

import com.controletcc.dto.DescriptionModelDTO;
import com.controletcc.dto.ProjetoTccAvaliacaoInfoDTO;
import com.controletcc.error.BusinessException;
import com.controletcc.model.dto.ProjetoTccAspectoAvaliacaoDTO;
import com.controletcc.model.dto.ProjetoTccAvaliacaoDTO;
import com.controletcc.model.entity.ModeloItemAvaliacao;
import com.controletcc.model.entity.Professor;
import com.controletcc.model.entity.ProjetoTcc;
import com.controletcc.model.enums.SituacaoTcc;
import com.controletcc.model.enums.TipoProfessor;
import com.controletcc.service.*;
import com.controletcc.util.ModelMapperUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class ProjetoTccAvaliacaoFacade {

    private final ProjetoTccAvaliacaoService projetoTccAvaliacaoService;

    private final ProjetoTccAspectoAvaliacaoService projetoTccAspectoAvaliacaoService;

    private final ProjetoTccService projetoTccService;

    private final ProjetoTccSituacaoService projetoTccSituacaoService;

    private final ModeloAvaliacaoService modeloAvaliacaoService;

    private final ModeloItemAvaliacaoService modeloItemAvaliacaoService;

    private final MembroBancaService membroBancaService;

    private final ProjetoTccNotaService projetoTccNotaService;

    public ProjetoTccAvaliacaoDTO getById(Long id) {
        return ModelMapperUtil.map(projetoTccAvaliacaoService.getById(id), ProjetoTccAvaliacaoDTO.class);
    }

    public void iniciarEtapaAvaliacao(Long idProjetoTcc) throws Exception {
        if (!projetoTccService.existsApresentacaoAgendada(idProjetoTcc)) {
            throw new BusinessException("Não foi agendada a apresentação deste TCC");
        }
        if (!projetoTccAvaliacaoService.existsAvaliacaoIniciada(idProjetoTcc)) {
            var projetoTcc = projetoTccService.getById(idProjetoTcc);
            var modeloAvaliacao = modeloAvaliacaoService.getByAreaTcc(projetoTcc.getIdAreaTcc());
            if (modeloAvaliacao == null) {
                throw new BusinessException("Não foi cadastrado nenhum modelo de avaliação para este TCC");
            }
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
            projetoTccNotaService.generate(projetoTcc, projetoTcc.getTipoTcc(), modeloAvaliacao);
            generate(itens, projetoTcc, TipoProfessor.ORIENTADOR, orientador);
            generate(itens, projetoTcc, TipoProfessor.SUPERVISOR, supervisor);
            if (membroBancaList != null && !membroBancaList.isEmpty()) {
                for (var membroBanca : membroBancaList) {
                    generate(itens, projetoTcc, TipoProfessor.MEMBRO_BANCA, membroBanca);
                }
            }
            var novaSituacao = projetoTccSituacaoService.nextStep(idProjetoTcc, SituacaoTcc.EM_AVALIACAO, "Enviado para etapa de avaliação");
            projetoTccService.updateSituacao(idProjetoTcc, novaSituacao);
        }
    }

    private void generate(List<ModeloItemAvaliacao> itens, ProjetoTcc projetoTcc, TipoProfessor tipoProfessor, Professor professor) {
        var item = itens.stream().filter(i -> i.getTipoProfessores().contains(tipoProfessor)).findFirst().orElse(null);
        if (item != null) {
            var projetoTccAvaliacao = projetoTccAvaliacaoService.generate(item, projetoTcc.getTipoTcc(), tipoProfessor, projetoTcc, professor);
            projetoTccAspectoAvaliacaoService.generateByList(projetoTccAvaliacao, item.getModeloAspectosAvaliacao());
        }
    }

    public ProjetoTccAvaliacaoInfoDTO getInfoByProjetoTcc(@NonNull Long idProjetoTcc) {
        var info = new ProjetoTccAvaliacaoInfoDTO();
        var projetoTcc = projetoTccService.getById(idProjetoTcc);
        var areaTcc = projetoTcc.getAreaTcc();
        var orientador = projetoTcc.getProfessorOrientador();
        var supervisor = projetoTcc.getProfessorSupervisor();
        var membroBancaList = membroBancaService.getProfessoresByIdProjetoTcc(idProjetoTcc);
        var membroBancaDescriptionList = membroBancaList != null && !membroBancaList.isEmpty() ?
                membroBancaList.stream().map(mb -> new DescriptionModelDTO(mb.getId(), mb.getNome())).toList()
                : new ArrayList<DescriptionModelDTO>();
        var alunoDescriptionList = projetoTcc.getAlunos().stream().map(a -> new DescriptionModelDTO(a.getId(), a.getNome())).toList();
        var projetoTccNota = projetoTccNotaService.getByProjetoTcc(idProjetoTcc);

        info.setIdProjetoTcc(projetoTcc.getId());
        info.setTema(projetoTcc.getTema());
        info.setTipoTcc(projetoTcc.getTipoTcc());
        info.setAnoPeriodo(projetoTcc.getAnoPeriodo());
        info.setAreaTcc(new DescriptionModelDTO(areaTcc.getId(), areaTcc.getDescricao()));
        info.setOrientador(new DescriptionModelDTO(orientador.getId(), orientador.getNome()));
        info.setSupervisor(new DescriptionModelDTO(supervisor.getId(), supervisor.getNome()));
        info.setMembroBancaList(membroBancaDescriptionList);
        info.setAlunos(alunoDescriptionList);
        info.setUpNotaFinalAndSituacaoAluno(projetoTccNota);

        return info;
    }

    public ProjetoTccAvaliacaoDTO getByProjetoTccAndTipoProfessorAndProfessor(Long idProjetoTcc, TipoProfessor tipoProfessor, Long idProfessor) {
        var projetoTcc = projetoTccService.getById(idProjetoTcc);
        return ModelMapperUtil.map(projetoTccAvaliacaoService.getByTipoTccAndTipoProfessorAndProjetoTccAndProfessor(projetoTcc.getTipoTcc(), tipoProfessor, projetoTcc.getId(), idProfessor), ProjetoTccAvaliacaoDTO.class);
    }

    public ProjetoTccAvaliacaoDTO saveAspectosValor(Long idProjetoTccAvaliacao, List<ProjetoTccAspectoAvaliacaoDTO> aspectos) throws Exception {
        var projetoTccAvaliacao = projetoTccAvaliacaoService.getById(idProjetoTccAvaliacao);
        var projetoTcc = projetoTccAvaliacao.getProjetoTcc();
        if (!SituacaoTcc.EM_AVALIACAO.equals(projetoTcc.getSituacaoAtual().getSituacaoTcc())) {
            throw new BusinessException("Este TCC não está em etapa de avaliação");
        }
        var idAspectoList = aspectos.stream().map(ProjetoTccAspectoAvaliacaoDTO::getId).toList();
        var aspectoMap = aspectos.stream().collect(Collectors.toMap(ProjetoTccAspectoAvaliacaoDTO::getId, ProjetoTccAspectoAvaliacaoDTO::getValor));
        var projetoTccAspectoAvaliacaoList = projetoTccAspectoAvaliacaoService.getAllByIdList(idAspectoList);
        for (var aspecto : projetoTccAspectoAvaliacaoList) {
            aspecto.setValor(aspectoMap.get(aspecto.getId()));
        }
        projetoTccAspectoAvaliacaoService.saveAll(idProjetoTccAvaliacao, projetoTccAspectoAvaliacaoList);
        return ModelMapperUtil.map(projetoTccAvaliacaoService.getById(idProjetoTccAvaliacao), ProjetoTccAvaliacaoDTO.class);
    }

    public ProjetoTccAvaliacaoDTO saveAspectos(Long idProjetoTccAvaliacao, List<ProjetoTccAspectoAvaliacaoDTO> aspectos) throws Exception {
        var idAspectoList = aspectos.stream().map(ProjetoTccAspectoAvaliacaoDTO::getId).toList();
        var aspectoMap = aspectos.stream().collect(Collectors.toMap(ProjetoTccAspectoAvaliacaoDTO::getId, Function.identity()));
        var projetoTccAspectoAvaliacaoList = projetoTccAspectoAvaliacaoService.getAllByIdList(idAspectoList);
        for (var aspecto : projetoTccAspectoAvaliacaoList) {
            var aspectoUpdate = aspectoMap.get(aspecto.getId());
            aspecto.setDescricao(aspectoUpdate.getDescricao());
            aspecto.setPeso(aspectoUpdate.getPeso());
        }
        projetoTccAspectoAvaliacaoService.saveAll(idProjetoTccAvaliacao, projetoTccAspectoAvaliacaoList);
        return ModelMapperUtil.map(projetoTccAvaliacaoService.getById(idProjetoTccAvaliacao), ProjetoTccAvaliacaoDTO.class);
    }


}
