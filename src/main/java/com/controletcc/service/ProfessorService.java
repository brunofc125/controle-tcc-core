package com.controletcc.service;

import com.controletcc.dto.base.ListResponseModel;
import com.controletcc.dto.grid.ProfessorGridDTO;
import com.controletcc.dto.options.ProfessorGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public ListResponseModel<ProfessorGridDTO> search(ProfessorGridOptions options) throws BusinessException {
        var page = professorRepository.search(options.getId(), options.getNome(), options.getCpf(), options.getRg(), options.getEmail(), options.isCategoriaSupervisor(), options.getPageable());
        return new ListResponseModel<ProfessorGridDTO>(page.getContent(), page.getTotalElements());
    }

}
