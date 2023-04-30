package com.controletcc.service;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.AgendaApresentacaoGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.entity.AgendaApresentacao;
import com.controletcc.model.enums.TipoTcc;
import com.controletcc.repository.AgendaApresentacaoRepository;
import com.controletcc.repository.projection.AgendaApresentacaoProjection;
import com.controletcc.repository.projection.AgendaPeriodoProjection;
import com.controletcc.util.StringUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class AgendaApresentacaoService {

    private final AgendaApresentacaoRepository agendaApresentacaoRepository;

    public AgendaApresentacao getById(@NonNull Long id) {
        return agendaApresentacaoRepository.getReferenceById(id);
    }

    public List<AgendaApresentacao> getAllByAnoPeriodo(Integer ano, Integer periodo) {
        return agendaApresentacaoRepository.getAllByAnoAndPeriodo(ano, periodo);
    }

    public ListResponse<AgendaApresentacaoProjection> search(@NonNull List<Long> idAreaTccList, AgendaApresentacaoGridOptions options) throws BusinessException {
        var page = agendaApresentacaoRepository.search(options.getId(), idAreaTccList, options.getDescricao(), options.getTipoTcc(), options.getIdAreaTcc(), options.getAnoPeriodo(), options.getDataInicial(), options.getDataFinal(), options.getPageable());
        return new ListResponse<>(page.getContent(), page.getTotalElements());
    }

    public AgendaApresentacao insert(@NonNull AgendaApresentacao agendaApresentacao) throws BusinessException {
        agendaApresentacao.setId(null);
        validate(agendaApresentacao);
        return agendaApresentacaoRepository.save(agendaApresentacao);
    }

    public AgendaApresentacao update(@NonNull Long id, @NonNull AgendaApresentacao agendaApresentacao) throws BusinessException {
        agendaApresentacao.setId(id);
        validate(agendaApresentacao);
        return agendaApresentacaoRepository.save(agendaApresentacao);
    }

    public boolean existsDuplicate(Long id, @NonNull Long idAreaTcc, @NonNull TipoTcc tipoTcc, @NonNull Integer ano, @NonNull Integer periodo) {
        return id != null ?
                agendaApresentacaoRepository.existsByAreaTccIdAndTipoTccAndAnoAndPeriodoAndIdNot(idAreaTcc, tipoTcc, ano, periodo, id)
                : agendaApresentacaoRepository.existsByAreaTccIdAndTipoTccAndAnoAndPeriodo(idAreaTcc, tipoTcc, ano, periodo);
    }

    private void validate(AgendaApresentacao agendaApresentacao) throws BusinessException {
        var errors = new ArrayList<String>();
        var dataAtual = LocalDate.now();

        if (StringUtil.isNullOrBlank(agendaApresentacao.getDescricao())) {
            errors.add("Descrição não informada");
        }

        if (agendaApresentacao.getTipoTcc() == null) {
            errors.add("Etapa do TCC não informada");
        }

        if (agendaApresentacao.getAreaTcc() == null) {
            errors.add("Área de TCC não informada");
        }

        if (agendaApresentacao.getAno() == null || agendaApresentacao.getPeriodo() == null) {
            errors.add("Ano/Período não informado");
        } else if (agendaApresentacao.getAno() != null && dataAtual.getYear() < agendaApresentacao.getAno()) {
            errors.add("Ano/Período não pode ser futuro");
        }

        if (agendaApresentacao.getDataInicial() == null) {
            errors.add("Data inicial das apresentações não informada");
        } else if (agendaApresentacao.getDataFinal() != null && agendaApresentacao.getDataInicial().isAfter(agendaApresentacao.getDataFinal())) {
            errors.add("Data inicial deve ser menor ou igual a data final");
        }

        if (agendaApresentacao.getDataFinal() == null) {
            errors.add("Data final das apresentações não informada");
        } else if (dataAtual.isAfter(agendaApresentacao.getDataFinal())) {
            errors.add("Data final deve ser maior ou igual a data atual");
        }

        if (agendaApresentacao.getHoraInicial() == null) {
            errors.add("Hora inicial das apresentações não informada");
        } else if (agendaApresentacao.getHoraFinal() != null && agendaApresentacao.getHoraFinal().compareTo(agendaApresentacao.getHoraInicial()) < 0) {
            errors.add("Hora inicial deve ser menor ou igual a hora final das apresentações");
        }

        if (agendaApresentacao.getHoraFinal() == null) {
            errors.add("Hora final das apresentações não informada");
        }

        if (errors.isEmpty() && existsDuplicate(agendaApresentacao.getId(), agendaApresentacao.getIdAreaTcc(), agendaApresentacao.getTipoTcc(), agendaApresentacao.getAno(), agendaApresentacao.getPeriodo())) {
            errors.add("Já existe uma agenda cadastrada neste ano/período");
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
    }

    public List<AgendaApresentacao> getAgendasAtivasByTipoTccAndAreaTcc(TipoTcc tipoTcc, Long idAreaTcc) {
        return agendaApresentacaoRepository.getAllByTipoTccAndAreaTccIdAndDataFinalGreaterThanEqual(tipoTcc, idAreaTcc, LocalDate.now());
    }

    public List<AgendaApresentacao> getAgendasAtivasByAreaTccIdIn(List<Long> idAreaTccList) {
        return agendaApresentacaoRepository.getAllByAreaTccIdInAndDataFinalGreaterThanEqual(idAreaTccList, LocalDate.now());
    }

    public AgendaPeriodoProjection getAgendaPeriodoByAnoPeriodoAndAreasTcc(Integer ano, Integer periodo, List<Long> idAreaTccList) {
        return agendaApresentacaoRepository.getAgendaByAnoPeriodoAndAreasTcc(ano, periodo, idAreaTccList);
    }

}
