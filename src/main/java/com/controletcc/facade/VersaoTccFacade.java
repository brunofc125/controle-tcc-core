package com.controletcc.facade;

import com.controletcc.config.security.UserLogged;
import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.VersaoTccGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.dto.VersaoTccDTO;
import com.controletcc.model.dto.base.ArquivoDTO;
import com.controletcc.model.entity.ProjetoTcc;
import com.controletcc.model.entity.VersaoTcc;
import com.controletcc.model.entity.base.Arquivo;
import com.controletcc.model.enums.UserType;
import com.controletcc.repository.projection.VersaoTccProjection;
import com.controletcc.service.AlunoService;
import com.controletcc.service.ProfessorService;
import com.controletcc.service.ProjetoTccService;
import com.controletcc.service.VersaoTccService;
import com.controletcc.util.AuthUtil;
import com.controletcc.util.ModelMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class VersaoTccFacade {

    private final VersaoTccService versaoTccService;

    private final ProjetoTccService projetoTccService;

    private final ProfessorService professorService;

    private final AlunoService alunoService;

    public VersaoTccDTO getById(Long id) {
        var versaoTcc = versaoTccService.getById(id);
        return ModelMapperUtil.map(versaoTcc, VersaoTccDTO.class);
    }

    public ListResponse<VersaoTccProjection> search(Long idProjetoTcc, VersaoTccGridOptions options) {
        return versaoTccService.search(idProjetoTcc, options);
    }

    public VersaoTccDTO insert(VersaoTccDTO versaoTccDTO) throws Exception {
        var versaoTcc = ModelMapperUtil.map(versaoTccDTO, VersaoTcc.class);
        var projetoTcc = projetoTccService.getById(versaoTccDTO.getIdProjetoTcc());
        var userLogged = getUserLogged();
        setResponsavelPublicacao(versaoTcc, projetoTcc, userLogged);
        versaoTcc = versaoTccService.insert(versaoTcc);
        projetoTccService.updateVisualizadoPor(projetoTcc.getId(), AuthUtil.getUserIdLogged(), true);
        return ModelMapperUtil.map(versaoTcc, VersaoTccDTO.class);
    }

    private void setResponsavelPublicacao(VersaoTcc versaoTcc, ProjetoTcc projetoTcc, UserLogged userLogged) throws BusinessException {
        switch (userLogged.getType()) {
            case SUPERVISOR, PROFESSOR -> {
                var professor = professorService.getProfessorByUsuarioId(userLogged.getId());
                if (!projetoTcc.getIdProfessorOrientador().equals(professor.getId())) {
                    throw new BusinessException("O usuário logado não é o professor orientador deste TCC");
                }
                versaoTcc.setIdProfessorOrientador(professor.getId());
            }
            case ALUNO -> {
                var aluno = alunoService.getAlunoByUsuarioId(userLogged.getId());
                if (!projetoTcc.getIdAlunoList().contains(aluno.getId())) {
                    throw new BusinessException("O usuário logado não é um aluno deste TCC");
                }
                versaoTcc.setIdAluno(aluno.getId());
            }
        }
    }

    private UserLogged getUserLogged() throws Exception {
        var userLogged = AuthUtil.getPrincipal();
        if (userLogged == null || (!UserType.SUPERVISOR.equals(userLogged.getType()) && !UserType.PROFESSOR.equals(userLogged.getType()) && !UserType.ALUNO.equals(userLogged.getType()))) {
            throw new BusinessException("Usuário inválido para publicação de uma nova versão de TCC");
        }
        return userLogged;
    }

    public ArquivoDTO download(Long id) {
        Arquivo anexoGeral = versaoTccService.getById(id);
        return ModelMapperUtil.map(anexoGeral, ArquivoDTO.class);
    }

}

