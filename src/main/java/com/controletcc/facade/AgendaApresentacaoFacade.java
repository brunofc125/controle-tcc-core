package com.controletcc.facade;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.AgendaApresentacaoGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.dto.AgendaApresentacaoDTO;
import com.controletcc.model.entity.AgendaApresentacao;
import com.controletcc.model.entity.AgendaApresentacaoRestricao;
import com.controletcc.repository.projection.AgendaApresentacaoProjection;
import com.controletcc.service.AgendaApresentacaoRestricaoService;
import com.controletcc.service.AgendaApresentacaoService;
import com.controletcc.service.ProfessorService;
import com.controletcc.util.AuthUtil;
import com.controletcc.util.ModelMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class AgendaApresentacaoFacade {

    private final AgendaApresentacaoService agendaApresentacaoService;

    private final AgendaApresentacaoRestricaoService agendaApresentacaoRestricaoService;

    private final ProfessorService professorService;

    public AgendaApresentacaoDTO getById(Long id) {
        var agendaApresentacao = agendaApresentacaoService.getById(id);
        return ModelMapperUtil.map(agendaApresentacao, AgendaApresentacaoDTO.class);
    }

    public ListResponse<AgendaApresentacaoProjection> search(AgendaApresentacaoGridOptions options) throws BusinessException {
        var professor = professorService.getProfessorByUsuarioId(AuthUtil.getUserIdLogged());
        if (professor == null) {
            throw new BusinessException("O usuário logado não é um professor, apenas professores podem visualizar a agenda de apresentações");
        }
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

    private List<AgendaApresentacaoRestricao> saveRestricoes(AgendaApresentacao agendaApresentacao, List<AgendaApresentacaoRestricao> restricoes) {
        for (var restricao : restricoes) {
            restricao.setAgendaApresentacao(agendaApresentacao);
        }
        return agendaApresentacaoRestricaoService.saveAll(agendaApresentacao.getId(), restricoes);
    }
}

