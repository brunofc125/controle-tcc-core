package com.controletcc.service;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.ProjetoTccGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.entity.ProjetoTcc;
import com.controletcc.repository.ProjetoTccRepository;
import com.controletcc.repository.projection.ProjetoTccProjection;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class ProjetoTccService {

    private final ProjetoTccRepository projetoTccRepository;

    public ProjetoTcc getById(@NonNull Long id) {
        return projetoTccRepository.getReferenceById(id);
    }

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

    public ProjetoTcc insert(@NonNull ProjetoTcc projetoTcc) throws BusinessException {
        projetoTcc.setId(null);
        validate(projetoTcc);
        return projetoTccRepository.save(projetoTcc);
    }

    public ProjetoTcc update(@NonNull Long idProjetoTcc, @NonNull ProjetoTcc projetoTcc) throws BusinessException {
        projetoTcc.setId(idProjetoTcc);
        validate(projetoTcc);
        return projetoTccRepository.save(projetoTcc);
    }

    private void validate(ProjetoTcc projetoTcc) throws BusinessException {
        var errors = new ArrayList<String>();

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
    }
}
