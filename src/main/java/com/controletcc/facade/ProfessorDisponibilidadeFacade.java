package com.controletcc.facade;

import com.controletcc.error.BusinessException;
import com.controletcc.model.dto.ProfessorDisponibilidadeDTO;
import com.controletcc.model.entity.ProfessorDisponibilidade;
import com.controletcc.service.ProfessorDisponibilidadeService;
import com.controletcc.service.ProfessorService;
import com.controletcc.util.ModelMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class ProfessorDisponibilidadeFacade {

    private final ProfessorDisponibilidadeService professorDisponibilidadeService;

    private final ProfessorService professorService;

    public ProfessorDisponibilidadeDTO getById(Long id) {
        return ModelMapperUtil.map(professorDisponibilidadeService.getById(id), ProfessorDisponibilidadeDTO.class);
    }

    public List<ProfessorDisponibilidadeDTO> save(List<ProfessorDisponibilidadeDTO> professorDisponibilidadeDTOS) throws BusinessException {
        if (professorDisponibilidadeDTOS == null || professorDisponibilidadeDTOS.isEmpty()) {
            return Collections.emptyList();
        }
        var professorDisponibilidades = ModelMapperUtil.mapAll(professorDisponibilidadeDTOS, ProfessorDisponibilidade.class);
        var professor = professorService.getProfessorLogado();
        var professorDisponibilidadesSaved = new ArrayList<ProfessorDisponibilidade>();
        for (var professorDisponibilidade : professorDisponibilidades) {
            professorDisponibilidade.setProfessor(professor);
            professorDisponibilidadesSaved.add(professorDisponibilidadeService.save(professorDisponibilidade));
        }
        return ModelMapperUtil.mapAll(professorDisponibilidadesSaved, ProfessorDisponibilidadeDTO.class);
    }

    public void delete(Long id) {
        professorDisponibilidadeService.delete(id);
    }

}

