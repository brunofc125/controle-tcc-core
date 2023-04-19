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
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        agendaParaApresentacao.setOutrasApresentacoes(apresentacaoService.getAllByAgendaApresentacaoIdAndProjetoTccIdNot(idAgendaApresentacao, idProjetoTcc));

        var projetoTcc = projetoTccService.getById(idProjetoTcc);
        var idProfessorList = new ArrayList<>(Stream.of(projetoTcc.getIdProfessorSupervisor(), projetoTcc.getIdProfessorOrientador()).distinct().toList());
        var membrosBanca = membroBancaService.getConfirmadosByIdProjetoTcc(idProjetoTcc);
        var idProfessorBancaList = membrosBanca.stream().map(MembroBanca::getIdProfessor).distinct().toList();
        idProfessorList.addAll(idProfessorBancaList);

        var professorDisponibilidades = professorDisponibilidadeService.getAllByAnoPeriodoAndProfessorList(agendaApresentacao.getAno(), agendaApresentacao.getPeriodo(), idProfessorList);
        var qtdProfessor = idProfessorList.size();
        var dataHoraInicial = agendaApresentacao.getDataHoraInicial();
        var dataHoraFinal = agendaApresentacao.getDataHoraFinal();
        var disponibilidadeAgrupadaList = new ArrayList<ProfessorDisponibilidadeAgrupadaDTO>();

        if (LocalDateTimeUtil.compare(dataHoraInicial, dataHoraFinal) < 0) {
            for (var dataIni = dataHoraInicial; dataIni.isBefore(dataHoraFinal); dataIni = dataIni.plusHours(1)) {
                var dataIniFilter = dataIni;
                var nomeProfessorList = new ArrayList<>(professorDisponibilidades.stream().filter(pd -> pd.isEventOccurring(dataIniFilter)).map(pd -> pd.getProfessor().getNome()).distinct().toList());
                if (!nomeProfessorList.isEmpty()) {
                    var disponibilidadeAgrupada = new ProfessorDisponibilidadeAgrupadaDTO();
                    disponibilidadeAgrupada.setDataHora(dataIni);
                    disponibilidadeAgrupada.setTodosProfessoresDisponiveis(nomeProfessorList.size() == qtdProfessor);
                    nomeProfessorList.sort(String::compareTo);
                    disponibilidadeAgrupada.setDescricao(String.join("<br>", nomeProfessorList));
                    disponibilidadeAgrupadaList.add(disponibilidadeAgrupada);
                }
            }
        }

        agendaParaApresentacao.setDisponibilidades(disponibilidadeAgrupadaList.stream().collect(Collectors.toMap(ProfessorDisponibilidadeAgrupadaDTO::getDataHoraStr, d -> d)));
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
        var disponibilidades = professorDisponibilidadeService.getAllByAnoPeriodoAndProfessor(ano, periodo, professor.getId());
        agendaPeriodo.setDisponibilidades(ModelMapperUtil.mapAll(disponibilidades, ProfessorDisponibilidadeDTO.class));
        return agendaPeriodo;
    }

}

