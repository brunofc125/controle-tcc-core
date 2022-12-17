package com.controletcc.facade;

import com.controletcc.dto.ProfessorCompromissosDTO;
import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.ProfessorDisponibilidadeGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.dto.ProfessorDisponibilidadeDTO;
import com.controletcc.model.entity.ProfessorDisponibilidade;
import com.controletcc.model.enums.TipoCompromisso;
import com.controletcc.repository.projection.ProfessorDisponibilidadeProjection;
import com.controletcc.service.ProfessorDisponibilidadeService;
import com.controletcc.service.ProfessorService;
import com.controletcc.service.VwProfessorCompromissoService;
import com.controletcc.util.ModelMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class ProfessorDisponibilidadeFacade {

    private final ProfessorDisponibilidadeService professorDisponibilidadeService;

    private final VwProfessorCompromissoService vwProfessorCompromissoService;

    private final ProfessorService professorService;

    public ProfessorDisponibilidadeDTO getById(Long id) {
        return ModelMapperUtil.map(professorDisponibilidadeService.getById(id), ProfessorDisponibilidadeDTO.class);
    }

    public ListResponse<ProfessorDisponibilidadeProjection> search(ProfessorDisponibilidadeGridOptions options) throws BusinessException {
        var professor = professorService.getProfessorLogado();
        return vwProfessorCompromissoService.search(professor.getId(), options);
    }

    public List<ProfessorDisponibilidadeDTO> save(List<ProfessorDisponibilidadeDTO> professorDisponibilidadeDTOS) throws BusinessException {
        if (professorDisponibilidadeDTOS == null || professorDisponibilidadeDTOS.isEmpty()) {
            return Collections.emptyList();
        }
        var professorCompromissos = ModelMapperUtil.mapAll(professorDisponibilidadeDTOS, ProfessorDisponibilidade.class);
        var professor = professorService.getProfessorLogado();
        var professorCompromissosSaved = new ArrayList<ProfessorDisponibilidade>();
        for (var professorCompromisso : professorCompromissos) {
            professorCompromisso.setProfessor(professor);
            professorCompromissosSaved.add(professorDisponibilidadeService.save(professorCompromisso));
        }
        return ModelMapperUtil.mapAll(professorCompromissosSaved, ProfessorDisponibilidadeDTO.class);
    }

    public void delete(Long id) {
        professorDisponibilidadeService.delete(id);
    }

    public ProfessorCompromissosDTO getCompromissosByProfessorLogadoAndData(LocalDate dataInicial) throws BusinessException {
        var professorLogado = professorService.getProfessorLogado();
        var compromissos = vwProfessorCompromissoService.getByProfessorAndData(professorLogado.getId(), dataInicial);
        var outrosCompromissos = compromissos.stream().filter(c -> TipoCompromisso.COMPROMISSO_PESSOAL.equals(c.getTipoCompromisso())).toList();
        var apresentacoes = compromissos.stream().filter(c -> TipoCompromisso.APRESENTACAO.equals(c.getTipoCompromisso())).toList();
        var compromissosDTO = new ProfessorCompromissosDTO();
        compromissosDTO.setOutrosCompromissos(outrosCompromissos);
        compromissosDTO.setApresentacoes(apresentacoes);
        return compromissosDTO;
    }
}

