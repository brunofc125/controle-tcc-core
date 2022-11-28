package com.controletcc.facade;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.ProjetoTccGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.dto.ProjetoTccDTO;
import com.controletcc.model.entity.ProjetoTcc;
import com.controletcc.repository.projection.ProjetoTccProjection;
import com.controletcc.service.AlunoService;
import com.controletcc.service.ProfessorService;
import com.controletcc.service.ProjetoTccService;
import com.controletcc.service.ProjetoTccSituacaoService;
import com.controletcc.util.AuthUtil;
import com.controletcc.util.ModelMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class ProjetoTccFacade {

    private final ProjetoTccService projetoTccService;

    private final ProjetoTccSituacaoService projetoTccSituacaoService;

    private final AlunoService alunoService;

    private final ProfessorService professorService;

    public ProjetoTccDTO getById(Long id) {
        return ModelMapperUtil.map(projetoTccService.getById(id), ProjetoTccDTO.class);
    }

    public ListResponse<ProjetoTccProjection> search(ProjetoTccGridOptions options) throws BusinessException {
        var idUser = AuthUtil.getUserIdLogged();
        switch (Objects.requireNonNull(AuthUtil.getUserTypeLogged())) {
            case PROFESSOR -> {
                var professor = professorService.getProfessorByUsuarioId(idUser);
                return projetoTccService.searchByProfessorOrientador(professor.getId(), options);
            }
            case SUPERVISOR -> {
                var professor = professorService.getProfessorByUsuarioId(idUser);
                return projetoTccService.searchByProfessorSupervisor(professor.getId(), options);
            }
            case ALUNO -> {
                var aluno = alunoService.getAlunoByUsuarioId(idUser);
                return projetoTccService.searchByAluno(aluno.getId(), options);
            }
        }
        throw new BusinessException("Tipo de usuário inválido para acessar esta consulta");
    }

    public ProjetoTccDTO insert(ProjetoTccDTO projetoTccDTO) throws BusinessException {
        var projetoTcc = ModelMapperUtil.map(projetoTccDTO, ProjetoTcc.class);
        projetoTcc = projetoTccService.insert(projetoTcc);
        projetoTcc.setSituacaoAtual(projetoTccSituacaoService.insert(projetoTcc.getId(), projetoTccDTO.getTipoTcc()));
        return ModelMapperUtil.map(projetoTccService.update(projetoTcc.getId(), projetoTcc), ProjetoTccDTO.class);
    }

    public ProjetoTccDTO update(ProjetoTccDTO projetoTccDTO) throws BusinessException {
        var projetoTcc = projetoTccService.getById(projetoTccDTO.getId());
        projetoTcc.setTema(projetoTcc.getTema());
        projetoTcc = projetoTccService.update(projetoTcc.getId(), projetoTcc);
        return ModelMapperUtil.map(projetoTccService.update(projetoTcc.getId(), projetoTcc), ProjetoTccDTO.class);
    }

}

