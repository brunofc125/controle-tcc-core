package com.controletcc.facade;

import com.controletcc.dto.AgendaParaApresentacaoDTO;
import com.controletcc.dto.AgendaPeriodoDTO;
import com.controletcc.dto.ProfessorDisponibilidadeAgrupadaDTO;
import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.AgendaApresentacaoGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.dto.AgendaApresentacaoDTO;
import com.controletcc.model.dto.AgendaApresentacaoRestricaoDTO;
import com.controletcc.model.dto.ProfessorDisponibilidadeDTO;
import com.controletcc.model.entity.AgendaApresentacao;
import com.controletcc.model.entity.AgendaApresentacaoRestricao;
import com.controletcc.model.entity.MembroBanca;
import com.controletcc.model.enums.TipoTcc;
import com.controletcc.repository.projection.AgendaApresentacaoProjection;
import com.controletcc.service.*;
import com.controletcc.util.ModelMapperUtil;
import com.controletcc.util.StringUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class AgendaApresentacaoFacade {

    private final AgendaApresentacaoService agendaApresentacaoService;

    private final AgendaApresentacaoRestricaoService agendaApresentacaoRestricaoService;

    private final ProfessorService professorService;

    private final ProjetoTccService projetoTccService;

    private final ApresentacaoService apresentacaoService;

    private final ProfessorDisponibilidadeService professorDisponibilidadeService;

    private final MembroBancaService membroBancaService;

    public AgendaApresentacaoDTO getById(Long id) {
        var agendaApresentacao = agendaApresentacaoService.getById(id);
        return ModelMapperUtil.map(agendaApresentacao, AgendaApresentacaoDTO.class);
    }

    public ListResponse<AgendaApresentacaoProjection> search(AgendaApresentacaoGridOptions options) throws BusinessException {
        var professor = professorService.getProfessorLogado();
        return agendaApresentacaoService.search(professor.getIdAreaList(), options);
    }

    public AgendaApresentacaoDTO insert(AgendaApresentacaoDTO agendaApresentacaoDTO) throws BusinessException {
        var agendaApresentacao = ModelMapperUtil.map(agendaApresentacaoDTO, AgendaApresentacao.class);
        var agendaApresentacaoRestricoes = agendaApresentacao.getAgendaApresentacaoRestricoes();
        agendaApresentacao = agendaApresentacaoService.insert(agendaApresentacao);
        agendaApresentacao.setAgendaApresentacaoRestricoes(saveRestricoes(agendaApresentacao, agendaApresentacaoRestricoes));
        return ModelMapperUtil.map(agendaApresentacao, AgendaApresentacaoDTO.class);
    }

    public AgendaApresentacaoDTO update(AgendaApresentacaoDTO agendaApresentacaoDTO) throws BusinessException {
        var agendaApresentacao = ModelMapperUtil.map(agendaApresentacaoDTO, AgendaApresentacao.class);
        var agendaApresentacaoRestricoes = agendaApresentacao.getAgendaApresentacaoRestricoes();
        agendaApresentacao = agendaApresentacaoService.update(agendaApresentacao.getId(), agendaApresentacao);
        agendaApresentacao.setAgendaApresentacaoRestricoes(saveRestricoes(agendaApresentacao, agendaApresentacaoRestricoes));
        return ModelMapperUtil.map(agendaApresentacao, AgendaApresentacaoDTO.class);
    }

    private List<AgendaApresentacaoRestricao> saveRestricoes(AgendaApresentacao agendaApresentacao, List<AgendaApresentacaoRestricao> restricoes) throws BusinessException {
        for (var restricao : restricoes) {
            restricao.setAgendaApresentacao(agendaApresentacao);
        }
        return agendaApresentacaoRestricaoService.saveAll(agendaApresentacao, restricoes);
    }

    public List<AgendaApresentacaoDTO> getAgendasAtivasByIdProjetoTcc(Long idProjetoTcc) {
        var projetoTcc = projetoTccService.getById(idProjetoTcc);
        return ModelMapperUtil.mapAll(agendaApresentacaoService.getAgendasAtivasByTipoTccAndAreaTcc(projetoTcc.getTipoTcc(), projetoTcc.getIdAreaTcc()), AgendaApresentacaoDTO.class);
    }

    public List<AgendaApresentacaoDTO> getAgendasAtivasByProfessorLogado() throws BusinessException {
        var professorLogado = professorService.getProfessorLogado();
        return ModelMapperUtil.mapAll(agendaApresentacaoService.getAgendasAtivasByAreaTccIdIn(professorLogado.getIdAreaList()), AgendaApresentacaoDTO.class);
    }

    public AgendaParaApresentacaoDTO getAgendaParaApresentacao(@NonNull Long idAgendaApresentacao, @NonNull Long idProjetoTcc) {
        var agendaApresentacao = agendaApresentacaoService.getById(idAgendaApresentacao);
        var agendaParaApresentacao = new AgendaParaApresentacaoDTO();
        agendaParaApresentacao.setId(agendaApresentacao.getId());
        agendaParaApresentacao.setDescricao(agendaApresentacao.getDescricao());
        agendaParaApresentacao.setOutrasApresentacoes(apresentacaoService.getAllByAgendaApresentacaoIdAndProjetoTccIdNot(idAgendaApresentacao, idProjetoTcc));

        var projetoTcc = projetoTccService.getById(idProjetoTcc);
        var membrosBanca = membroBancaService.getByIdProjetoTcc(idProjetoTcc);
        var idProfessorList = new java.util.ArrayList<>(membrosBanca.stream().map(MembroBanca::getIdProfessor).distinct().toList());
        idProfessorList.add(projetoTcc.getIdProfessorOrientador());

        var qtdProfessor = idProfessorList.size();
        var dataHoraInicial = agendaApresentacao.getDataHoraInicial();
        var dataHoraFinal = agendaApresentacao.getDataHoraFinal();
        var disponibilidadeAgrupadaList = professorDisponibilidadeService.getDisponibilidades(idProfessorList, idProjetoTcc, idAgendaApresentacao, dataHoraInicial, dataHoraFinal);

        var disponibilidadeAgrupadaMap = disponibilidadeAgrupadaList.stream()
                .map(d -> new ProfessorDisponibilidadeAgrupadaDTO(d, d.getQtdProfessores() != null && d.getQtdProfessores().intValue() == qtdProfessor))
                .collect(Collectors.toMap(ProfessorDisponibilidadeAgrupadaDTO::getDataHoraStr, d -> d));

        agendaParaApresentacao.setDisponibilidades(disponibilidadeAgrupadaMap);
        return agendaParaApresentacao;
    }

    public AgendaPeriodoDTO getAgendaPeriodo(String anoPeriodo, List<TipoTcc> tipoTccList) throws BusinessException {
        if (StringUtil.isNullOrBlank(anoPeriodo) || !anoPeriodo.matches("\\d{4}-\\d") || tipoTccList == null || tipoTccList.isEmpty()) {
            return null;
        }
        var ano = Integer.valueOf(anoPeriodo.substring(0, 4));
        var periodo = Integer.valueOf(anoPeriodo.substring(5));
        var professor = professorService.getProfessorLogado();
        var agendaPeriodoProjection = agendaApresentacaoService.getAgendaPeriodoByAnoPeriodoAndAreasTcc(ano, periodo, professor.getIdAreaList());
        if (agendaPeriodoProjection == null) {
            return null;
        }
        var agendaPeriodo = new AgendaPeriodoDTO(agendaPeriodoProjection);
        var restricoes = agendaApresentacaoRestricaoService.getAllByAnoPeriodoAndAreasTcc(ano, periodo, tipoTccList, professor.getIdAreaList());
        agendaPeriodo.setRestricoes(ModelMapperUtil.mapAll(restricoes, AgendaApresentacaoRestricaoDTO.class));
        var disponibilidades = professorDisponibilidadeService.getAllByAnoPeriodoAndProfessor(ano, periodo, professor.getId());
        agendaPeriodo.setDisponibilidades(ModelMapperUtil.mapAll(disponibilidades, ProfessorDisponibilidadeDTO.class));
        return agendaPeriodo;
    }

}
