package com.controletcc.facade;

import com.controletcc.dto.ProfessorCompromissosDTO;
import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.ProfessorCompromissoGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.dto.ProfessorCompromissoDTO;
import com.controletcc.model.entity.ProfessorCompromisso;
import com.controletcc.model.enums.TipoCompromisso;
import com.controletcc.repository.projection.ProfessorCompromissoProjection;
import com.controletcc.service.ProfessorCompromissoService;
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
public class ProfessorCompromissoFacade {

    private final ProfessorCompromissoService professorCompromissoService;

    private final VwProfessorCompromissoService vwProfessorCompromissoService;

    private final ProfessorService professorService;

    public ProfessorCompromissoDTO getById(Long id) {
        return ModelMapperUtil.map(professorCompromissoService.getById(id), ProfessorCompromissoDTO.class);
    }

    public ListResponse<ProfessorCompromissoProjection> search(ProfessorCompromissoGridOptions options) throws BusinessException {
        var professor = professorService.getProfessorLogado();
        return vwProfessorCompromissoService.search(professor.getId(), options);
    }

    public List<ProfessorCompromissoDTO> save(List<ProfessorCompromissoDTO> professorCompromissoDTOs) throws BusinessException {
        if (professorCompromissoDTOs == null || professorCompromissoDTOs.isEmpty()) {
            return Collections.emptyList();
        }
        var professorCompromissos = ModelMapperUtil.mapAll(professorCompromissoDTOs, ProfessorCompromisso.class);
        var professor = professorService.getProfessorLogado();
        var professorCompromissosSaved = new ArrayList<ProfessorCompromisso>();
        for (var professorCompromisso : professorCompromissos) {
            professorCompromisso.setProfessor(professor);
            professorCompromissosSaved.add(professorCompromissoService.save(professorCompromisso));
        }
        return ModelMapperUtil.mapAll(professorCompromissosSaved, ProfessorCompromissoDTO.class);
    }

    public void delete(Long id) {
        professorCompromissoService.delete(id);
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

