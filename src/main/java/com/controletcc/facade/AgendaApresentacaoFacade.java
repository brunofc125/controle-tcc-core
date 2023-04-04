package com.controletcc.facade;

import com.controletcc.dto.AgendaParaApresentacaoDTO;
import com.controletcc.dto.AgendaPeriodoDTO;
import com.controletcc.dto.EventoDTO;
import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.AgendaApresentacaoGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.dto.AgendaApresentacaoDTO;
import com.controletcc.model.dto.AgendaApresentacaoRestricaoDTO;
import com.controletcc.model.entity.AgendaApresentacao;
import com.controletcc.model.entity.AgendaApresentacaoRestricao;
import com.controletcc.model.entity.MembroBanca;
import com.controletcc.model.enums.TipoCompromisso;
import com.controletcc.model.view.VwProfessorCompromisso;
import com.controletcc.repository.projection.AgendaApresentacaoProjection;
import com.controletcc.service.*;
import com.controletcc.util.LocalDateTimeUtil;
import com.controletcc.util.ModelMapperUtil;
import com.controletcc.util.StringUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

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

    private final VwProfessorCompromissoService vwProfessorCompromissoService;

    private final MembroBancaService membroBancaService;

    public AgendaApresentacaoDTO getById(Long id) {
        var agendaApresentacao = agendaApresentacaoService.getById(id);
        return ModelMapperUtil.map(agendaApresentacao, AgendaApresentacaoDTO.class);
    }

    public List<AgendaApresentacaoDTO> getAllByAnoPeriodo(String anoPeriodo) {
        if (StringUtil.isNullOrBlank(anoPeriodo) || !anoPeriodo.matches("\\d{4}-\\d")) {
            return Collections.emptyList();
        }
        var ano = Integer.valueOf(anoPeriodo.substring(0, 4));
        var periodo = Integer.valueOf(anoPeriodo.substring(5));
        var agendas = agendaApresentacaoService.getAllByAnoPeriodo(ano, periodo);
        return ModelMapperUtil.mapAll(agendas, AgendaApresentacaoDTO.class);
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
        agendaParaApresentacao.setAgendaRestricoes(agendaApresentacao.getAgendaApresentacaoRestricoes());
        agendaParaApresentacao.setOutrasApresentacoes(apresentacaoService.getAllByAgendaApresentacaoIdAndProjetoTccIdNot(idAgendaApresentacao, idProjetoTcc));

        var projetoTcc = projetoTccService.getById(idProjetoTcc);
        var idProfessorList = new ArrayList<>(Stream.of(projetoTcc.getIdProfessorSupervisor(), projetoTcc.getIdProfessorOrientador()).distinct().toList());
        var membrosBanca = membroBancaService.getConfirmadosByIdProjetoTcc(idProjetoTcc);
        var idProfessorBancaList = membrosBanca.stream().map(MembroBanca::getIdProfessor).distinct().toList();
        idProfessorList.addAll(idProfessorBancaList);
        var nomeProfessorMap = professorService.getNomeMappedByIds(idProfessorList);

        var compromissos = vwProfessorCompromissoService.getByProfessoresAndDataAndNotAgenda(idProfessorList, agendaApresentacao.getDataHoraInicial(), agendaApresentacao.getDataHoraFinal(), idAgendaApresentacao);
        var compromissosMap = compromissos.stream().collect(
                groupingBy(
                        c -> new VwProfessorCompromisso.Compromisso(c.getTipoCompromisso(), c.getTipoTcc(), c.getId(), c.getIdProfessor(), c.getIdProfessorSupervisor(), c.getIdProfessorOrientador(), c.getDataInicial(), c.getDataFinal()),
                        mapping(VwProfessorCompromisso::getIdProfessorBanca, toList())
                )
        );

        var eventos = new ArrayList<EventoDTO>();
        for (var compromissoSet : compromissosMap.entrySet()) {
            var compromisso = compromissoSet.getKey();
            StringBuilder descricao = new StringBuilder(LocalDateTimeUtil.getHoursTitle(compromisso.dataInicial(), compromisso.dataFinal()));
            if (TipoCompromisso.COMPROMISSO_PESSOAL.equals(compromisso.tipoCompromisso())) {
                descricao.append(": Professor Ocupado");
                descricao.append("<p>").append(nomeProfessorMap.get(compromisso.idProfessor())).append("</p>");
            } else {
                descricao.append(": Outra Apresentação");
                if (nomeProfessorMap.containsKey(compromisso.idProfessorSupervisor())) {
                    descricao.append("<p>Supervisor: ").append(nomeProfessorMap.get(compromisso.idProfessorSupervisor())).append("</p>");
                }
                if (nomeProfessorMap.containsKey(compromisso.idProfessorOrientador())) {
                    descricao.append("<p>Orientador: ").append(nomeProfessorMap.get(compromisso.idProfessorOrientador())).append("</p>");
                }
                var nomeProfessoresBanca = compromissoSet.getValue().stream().filter(nomeProfessorMap::containsKey).map(c -> "<p>" + nomeProfessorMap.get(c) + "</p>").collect(joining());
                if (!nomeProfessoresBanca.isBlank()) {
                    descricao.append("<p>Membro da Banca:</p>").append(nomeProfessoresBanca);
                }
            }
            var evento = new EventoDTO(compromisso.id(), descricao.toString(), compromisso.dataInicial(), compromisso.dataFinal());
            eventos.add(evento);
        }
        agendaParaApresentacao.setProfessorCompromisso(eventos);

        return agendaParaApresentacao;
    }

    public AgendaPeriodoDTO getAgendaPeriodo(String anoPeriodo) throws BusinessException {
        if (StringUtil.isNullOrBlank(anoPeriodo) || !anoPeriodo.matches("\\d{4}-\\d")) {
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
        var restricoes = agendaApresentacaoRestricaoService.getAllByAnoPeriodoAndAreasTcc(ano, periodo, professor.getIdAreaList());
        agendaPeriodo.setRestricoes(ModelMapperUtil.mapAll(restricoes, AgendaApresentacaoRestricaoDTO.class));
        return agendaPeriodo;
    }

}

