package com.controletcc.service;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.AreaTccGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.entity.AreaTcc;
import com.controletcc.repository.AreaTccRepository;
import com.controletcc.repository.projection.AreaTccProjection;
import com.controletcc.util.StringUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class AreaTccService {

    private final AreaTccRepository areaTccRepository;

    public AreaTcc getById(@NonNull Long id) {
        return areaTccRepository.getReferenceById(id);
    }

    public List<AreaTcc> getAll() {
        return areaTccRepository.findAll(Sort.by("id"));
    }

    public ListResponse<AreaTccProjection> search(AreaTccGridOptions options) {
        var page = areaTccRepository.search(options.getId(), options.getFaculdade(), options.getCurso(), options.getPageable());
        return new ListResponse<>(page.getContent(), page.getTotalElements());
    }

    public AreaTcc insert(@NonNull AreaTcc areaTcc) throws BusinessException {
        areaTcc.setId(null);
        validate(areaTcc);
        return areaTccRepository.save(areaTcc);
    }

    public AreaTcc update(@NonNull Long id, @NonNull AreaTcc areaTcc) throws BusinessException {
        areaTcc.setId(id);
        validate(areaTcc);
        return areaTccRepository.save(areaTcc);
    }

    private void validate(AreaTcc areaTcc) throws BusinessException {
        var errors = new ArrayList<String>();
        var faculdadeValid = true;
        var cursoValid = true;

        if (StringUtil.isNullOrBlank(areaTcc.getFaculdade())) {
            errors.add("Faculdade não informada");
            faculdadeValid = false;
        }

        if (StringUtil.isNullOrBlank(areaTcc.getCurso())) {
            errors.add("Curso não informado");
            cursoValid = false;
        }

        if (faculdadeValid && cursoValid) {
            if (areaTcc.getId() == null) {
                if (areaTccRepository.existsByFaculdadeAndCurso(areaTcc.getFaculdade(), areaTcc.getCurso())) {
                    errors.add("Já existe uma área com esta faculdade e este curso");
                }
            } else if (areaTccRepository.existsByFaculdadeAndCursoAndIdNot(areaTcc.getFaculdade(), areaTcc.getCurso(), areaTcc.getId())) {
                errors.add("Já existe uma área com esta faculdade e este curso");
            }
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
    }

    public List<AreaTcc> getAllByIdProfessor(Long idProfessor) {
        return areaTccRepository.getAllByIdProfessor(idProfessor);
    }

}
