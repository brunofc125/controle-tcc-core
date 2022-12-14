package com.controletcc.service;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.enums.OrderByDirection;
import com.controletcc.dto.options.ProfessorCompromissoGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.view.VwProfessorCompromisso;
import com.controletcc.repository.VwProfessorCompromissoRepository;
import com.controletcc.repository.projection.ProfessorCompromissoProjection;
import com.controletcc.util.StringUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class VwProfessorCompromissoService {

    private final VwProfessorCompromissoRepository vwProfessorCompromissoRepository;

    public ListResponse<ProfessorCompromissoProjection> search(@NonNull Long idProfessor, ProfessorCompromissoGridOptions options) throws BusinessException {
        if (StringUtil.isNullOrBlank(options.getOrderByField())) {
            options.setOrderByField("tipoCompromisso");
            options.setOrderByDirection(OrderByDirection.ASC);
        }
        var page = vwProfessorCompromissoRepository.search(idProfessor, options.getId(), options.getDescricao(), options.getTipoCompromisso(), options.getTipoTcc(), options.getIdAgenda(), options.getDataInicial(), options.getDataFinal(), options.getPageable());
        return new ListResponse<>(page.getContent(), page.getTotalElements());
    }

    public List<ProfessorCompromissoProjection> getByProfessorAndData(@NonNull Long idProfessor, @NonNull LocalDate data) {
        return vwProfessorCompromissoRepository.getByProfessorAndData(idProfessor, data, data.plusDays(6));
    }

    public List<VwProfessorCompromisso> getByProfessoresAndDataAndNotAgenda(List<Long> idProfessorList, LocalDateTime dataInicial, LocalDateTime dataFinal, Long idAgenda) {
        return vwProfessorCompromissoRepository.getByProfessoresAndDataAndNotAgenda(idProfessorList, dataInicial, dataFinal, idAgenda);
    }

}
