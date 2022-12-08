package com.controletcc.facade;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.error.BusinessException;
import com.controletcc.model.dto.MembroBancaDTO;
import com.controletcc.model.entity.MembroBanca;
import com.controletcc.model.enums.TipoTcc;
import com.controletcc.repository.projection.MembroBancaProjection;
import com.controletcc.service.EmailService;
import com.controletcc.service.MembroBancaService;
import com.controletcc.service.ProfessorService;
import com.controletcc.service.ProjetoTccService;
import com.controletcc.util.ModelMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class MembroBancaFacade {

    private final MembroBancaService membroBancaService;

    private final ProjetoTccService projetoTccService;

    private final ProfessorService professorService;

    private final EmailService emailService;

    public MembroBancaDTO getById(Long id) {
        var membroBanca = membroBancaService.getById(id);
        return ModelMapperUtil.map(membroBanca, MembroBancaDTO.class);
    }

    public ListResponse<MembroBancaProjection> getAllByIdProjetoTcc(Long idProjetoTcc) throws BusinessException {
        var list = membroBancaService.getAllByIdProjetoTcc(idProjetoTcc);
        return new ListResponse<>(list, (long) list.size());
    }

    public MembroBancaDTO solicitar(Long idProjetoTcc, Long idProfessor) throws BusinessException {
        var projetoTcc = projetoTccService.getById(idProjetoTcc);
        var professor = professorService.getById(idProfessor);
        if (!TipoTcc.DEFESA.equals(projetoTcc.getTipoTcc())) {
            throw new BusinessException("A solicitação de membros à banca são apenas para TCCs em etapa de Defesa");
        }
        if (projetoTcc.getIdProfessorOrientador().equals(idProfessor)) {
            throw new BusinessException("O professor orientador não pode ser solicitado como membro de banca");
        }
        if (projetoTcc.getIdProfessorSupervisor().equals(idProfessor)) {
            throw new BusinessException("O professor supervisor não pode ser solicitado como membro de banca");
        }
        var membroBanca = new MembroBanca();
        membroBanca.setProfessor(professor);
        membroBanca.setProjetoTcc(projetoTcc);
        membroBanca.setDataSolicitacao(LocalDateTime.now());
        membroBanca = membroBancaService.insert(membroBanca);
        emailService.sendSolicitacaoMembroBanca(professor, projetoTcc);
        return ModelMapperUtil.map(membroBanca, MembroBancaDTO.class);
    }

    public void delete(Long id) throws BusinessException {
        var membroBanca = membroBancaService.getById(id);
        emailService.sendSolicitacaoMembroBancaRemovida(membroBanca.getProfessor(), membroBanca.getProjetoTcc());
        membroBancaService.delete(id);
    }
}

