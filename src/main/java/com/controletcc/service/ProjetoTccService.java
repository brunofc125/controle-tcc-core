package com.controletcc.service;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.ProjetoTccGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.repository.ProjetoTccRepository;
import com.controletcc.repository.projection.ProjetoTccProjection;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class ProjetoTccService {

    private final ProjetoTccRepository projetoTccRepository;

    public ListResponse<ProjetoTccProjection> searchByProfessorOrientador(@NonNull Long idProfessorOrientador, @NonNull ProjetoTccGridOptions options) {
        var page = projetoTccRepository.search(options.getId(), options.getTema(), options.getAnoPeriodo(), options.getTipoTcc(), options.getSituacaoTcc(), idProfessorOrientador, null, null, null, options.getNomeAluno(), options.getPageable());
        return new ListResponse<>(page.getContent(), page.getTotalElements());
    }

    public ListResponse<ProjetoTccProjection> searchByProfessorSupervisor(@NonNull Long idProfessorSupervisor, @NonNull ProjetoTccGridOptions options) {
        var page = projetoTccRepository.search(options.getId(), options.getTema(), options.getAnoPeriodo(), options.getTipoTcc(), options.getSituacaoTcc(), null, options.getNomeProfessorOrientador(), idProfessorSupervisor, null, options.getNomeAluno(), options.getPageable());
        return new ListResponse<>(page.getContent(), page.getTotalElements());
    }

    public ListResponse<ProjetoTccProjection> searchByAluno(@NonNull Long idAluno, @NonNull ProjetoTccGridOptions options) {
        var page = projetoTccRepository.search(options.getId(), options.getTema(), options.getAnoPeriodo(), options.getTipoTcc(), options.getSituacaoTcc(), null, null, null, idAluno, null, options.getPageable());
        return new ListResponse<>(page.getContent(), page.getTotalElements());
    }

}
