package com.controletcc.service;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.ProjetoTccGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.entity.ProjetoTcc;
import com.controletcc.model.entity.ProjetoTccSituacao;
import com.controletcc.model.enums.SituacaoTcc;
import com.controletcc.repository.ProjetoTccRepository;
import com.controletcc.repository.projection.ProjetoTccExportProjection;
import com.controletcc.repository.projection.ProjetoTccProjection;
import com.controletcc.util.StringUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class ProjetoTccService {

    private final ProjetoTccRepository projetoTccRepository;

    public ProjetoTcc getById(@NonNull Long id) {
        return projetoTccRepository.getReferenceById(id);
    }

    public ListResponse<ProjetoTccProjection> searchSupervisor(@NonNull Long idProfessorSupervisor, @NonNull ProjetoTccGridOptions options) {
        var page = projetoTccRepository.searchSupervisor(idProfessorSupervisor, options.getId(), options.getTema(), options.getAnoPeriodo(), options.getTipoTcc(), options.getSituacaoTcc(), options.getNomeProfessorOrientador(), options.getNomeAluno(), options.getPageable());
        return new ListResponse<>(page.getContent(), page.getTotalElements());
    }

    public ListResponse<ProjetoTccProjection> searchOrientador(@NonNull Long idProfessorOrientador, @NonNull ProjetoTccGridOptions options) {
        var page = projetoTccRepository.searchOrientador(idProfessorOrientador, options.getId(), options.getTema(), options.getAnoPeriodo(), options.getTipoTcc(), options.getSituacaoTcc(), options.getNomeAluno(), options.getPageable());
        return new ListResponse<>(page.getContent(), page.getTotalElements());
    }

    public ListResponse<ProjetoTccProjection> searchMembroBanca(@NonNull Long idProfessorBanca, @NonNull ProjetoTccGridOptions options) {
        var page = projetoTccRepository.searchMembroBanca(idProfessorBanca, options.getId(), options.getTema(), options.getAnoPeriodo(), options.getTipoTcc(), options.getSituacaoTcc(), options.getNomeProfessorOrientador(), options.getNomeAluno(), options.getSituacaoSolicitacaoBancaName(), options.getPageable());
        return new ListResponse<>(page.getContent(), page.getTotalElements());
    }

    public ListResponse<ProjetoTccProjection> searchAluno(@NonNull Long idAluno, @NonNull ProjetoTccGridOptions options) {
        var page = projetoTccRepository.searchAluno(idAluno, options.getId(), options.getTema(), options.getAnoPeriodo(), options.getTipoTcc(), options.getSituacaoTcc(), options.getPageable());
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

    public ProjetoTcc updateSituacao(@NonNull Long idProjetoTcc, @NonNull ProjetoTccSituacao situacao) {
        var projetoTcc = getById(idProjetoTcc);
        projetoTcc.setSituacaoAtual(situacao);
        return projetoTccRepository.save(projetoTcc);
    }

    public boolean existsAtivoByIdAluno(@NonNull Long idAluno, Long idProjetoTcc) {
        return projetoTccRepository.existsAtivoByIdAluno(idAluno, idProjetoTcc, Arrays.asList(SituacaoTcc.EM_ANDAMENTO, SituacaoTcc.A_APRESENTAR, SituacaoTcc.APROVADO), Arrays.asList(SituacaoTcc.EM_ANDAMENTO, SituacaoTcc.A_APRESENTAR));
    }

    private void validate(ProjetoTcc projetoTcc) throws BusinessException {
        var errors = new ArrayList<String>();

        if (StringUtil.isNullOrBlank(projetoTcc.getTema())) {
            errors.add("Tema não informado");
        }

        if (StringUtil.isNullOrBlank(projetoTcc.getAnoPeriodo())) {
            errors.add("Ano/Período não informado");
        }

        if (projetoTcc.getProfessorOrientador() == null) {
            errors.add("Professor orientador não informado");
        }

        if (projetoTcc.getAreaTcc() == null) {
            errors.add("Área de TCC não informada");
        }

        if (projetoTcc.getProfessorSupervisor() == null) {
            errors.add("Professor supervisor não informado");
        }

        if (projetoTcc.getAlunos() == null || projetoTcc.getAlunos().isEmpty()) {
            errors.add("Aluno(s) não informado(s)");
        }

        if (errors.isEmpty()) {
            var idAlunoListError = new ArrayList<Long>();
            for (var idAluno : projetoTcc.getIdAlunoList()) {
                if (existsAtivoByIdAluno(idAluno, projetoTcc.getId())) {
                    idAlunoListError.add(idAluno);
                }
            }
            if (!idAlunoListError.isEmpty()) {
                if (idAlunoListError.size() > 1) {
                    var idAlunoListErrorStr = idAlunoListError.stream().map(Object::toString).collect(Collectors.joining(", "));
                    errors.add("Existem outros projetos de TCC ativos para os alunos(as) de ID: " + idAlunoListErrorStr);
                } else {
                    errors.add("Existe outro projeto de TCC ativo para o aluno(a) de ID: " + idAlunoListError.get(0));
                }
            }
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
    }

    public List<ProjetoTccExportProjection> exportSupervisor(@NonNull ProjetoTccGridOptions options, @NonNull Long idSupervisor) {
        return projetoTccRepository.export(options.getId(), options.getTema(), options.getAnoPeriodo(), options.getTipoTcc(), options.getSituacaoTcc(), options.getNomeProfessorOrientador(), options.getNomeAluno(), idSupervisor, null, null);
    }

    public List<ProjetoTccExportProjection> exportOrientador(@NonNull ProjetoTccGridOptions options, @NonNull Long idOrientador) {
        return projetoTccRepository.export(options.getId(), options.getTema(), options.getAnoPeriodo(), options.getTipoTcc(), options.getSituacaoTcc(), options.getNomeProfessorOrientador(), options.getNomeAluno(), null, idOrientador, null);
    }

    public List<ProjetoTccExportProjection> exportMembroBanca(@NonNull ProjetoTccGridOptions options, @NonNull Long idMembroBanca) {
        return projetoTccRepository.export(options.getId(), options.getTema(), options.getAnoPeriodo(), options.getTipoTcc(), options.getSituacaoTcc(), options.getNomeProfessorOrientador(), options.getNomeAluno(), null, null, idMembroBanca);
    }

    public boolean existsApresentacaoAgendada(@NonNull Long idProjetoTcc) {
        return projetoTccRepository.existsApresentacaoAgendada(idProjetoTcc);
    }

    public boolean existsEmAvaliacaoByModeloAvaliacao(Long idModeloAvaliacao) {
        return projetoTccRepository.existsEmAvaliacaoByModeloAvaliacao(idModeloAvaliacao);
    }

}
