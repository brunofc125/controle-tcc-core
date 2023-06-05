package com.controletcc.facade;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.csv.ProjetoTccExportCsvDTO;
import com.controletcc.dto.csv.ReturnExportCsvDTO;
import com.controletcc.dto.enums.TccRoute;
import com.controletcc.dto.options.ProjetoTccGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.dto.ProjetoTccDTO;
import com.controletcc.model.entity.ProjetoTcc;
import com.controletcc.model.enums.SituacaoTcc;
import com.controletcc.model.enums.TipoTcc;
import com.controletcc.repository.projection.ProjetoTccExportProjection;
import com.controletcc.repository.projection.ProjetoTccProjection;
import com.controletcc.service.*;
import com.controletcc.util.AuthUtil;
import com.controletcc.util.LocalDateTimeUtil;
import com.controletcc.util.ModelMapperUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class ProjetoTccFacade {

    private final ProjetoTccService projetoTccService;

    private final ProjetoTccSituacaoService projetoTccSituacaoService;

    private final AlunoService alunoService;

    private final ProfessorService professorService;

    private final ProjetoTccSituacaoFacade projetoTccSituacaoFacade;

    private final CsvService csvService;

    private final MembroBancaService membroBancaService;

    private final ProfessorDisponibilidadeService professorDisponibilidadeService;

    private final ApresentacaoService apresentacaoService;

    public ProjetoTccDTO getById(Long id) {
        return ModelMapperUtil.map(projetoTccService.getById(id), ProjetoTccDTO.class);
    }

    public ListResponse<ProjetoTccProjection> search(@NonNull TccRoute tccRoute, @NonNull ProjetoTccGridOptions options) throws BusinessException {
        var userType = AuthUtil.getUserTypeLogged();
        if (userType == null || !tccRoute.userTypeMatches(userType)) {
            throw new BusinessException("Tipo de usuário inválido");
        }
        switch (tccRoute) {
            case SUPERVISOR -> {
                var professor = professorService.getProfessorLogado();
                return projetoTccService.searchSupervisor(professor.getId(), options);
            }
            case ORIENTADOR -> {
                var professor = professorService.getProfessorLogado();
                return projetoTccService.searchOrientador(professor.getId(), options);
            }
            case MEMBRO_BANCA -> {
                var professor = professorService.getProfessorLogado();
                return projetoTccService.searchMembroBanca(professor.getId(), options);
            }
            case ALUNO -> {
                var aluno = alunoService.getAlunoLogado();
                return projetoTccService.searchAluno(aluno.getId(), options);
            }
        }
        throw new BusinessException("Tipo de usuário inválido para acessar esta consulta");
    }

    public ReturnExportCsvDTO export(@NonNull TccRoute tccRoute, @NonNull ProjetoTccGridOptions options) throws Exception {
        var userType = AuthUtil.getUserTypeLogged();
        if (userType == null || !tccRoute.userTypeMatches(userType)) {
            throw new BusinessException("Tipo de usuário inválido");
        }
        List<ProjetoTccExportProjection> projetosTcc = null;
        var professor = professorService.getProfessorLogado();
        switch (tccRoute) {
            case SUPERVISOR -> projetosTcc = projetoTccService.exportSupervisor(options, professor.getId());
            case ORIENTADOR -> projetosTcc = projetoTccService.exportOrientador(options, professor.getId());
            case MEMBRO_BANCA -> projetosTcc = projetoTccService.exportMembroBanca(options, professor.getId());
            case default -> projetosTcc = Collections.emptyList();
        }
        var records = projetosTcc.stream().map(ProjetoTccExportCsvDTO::new).toList();
        var dataAtualStr = LocalDateTimeUtil.localDateTimeToString(LocalDateTime.now(), "dd-MM-yyyy_HH-mm-ss");
        var fileName = "projeto_tcc_" + dataAtualStr;
        return csvService.getExportedCsv(fileName, records, ProjetoTccExportCsvDTO.class);
    }

    public ProjetoTccDTO insert(ProjetoTccDTO projetoTccDTO) throws BusinessException {
        var projetoTcc = ModelMapperUtil.map(projetoTccDTO, ProjetoTcc.class);
        projetoTcc = projetoTccService.insert(projetoTcc);
        var situacao = projetoTccSituacaoService.insert(projetoTcc.getId(), projetoTccDTO.getTipoTcc());
        return ModelMapperUtil.map(projetoTccService.updateSituacao(projetoTcc.getId(), situacao), ProjetoTccDTO.class);
    }

    public ProjetoTccDTO update(ProjetoTccDTO projetoTccDTO) throws BusinessException {
        var projetoTcc = projetoTccService.getById(projetoTccDTO.getId());
        projetoTcc.setTema(projetoTcc.getTema());
        projetoTcc = projetoTccService.update(projetoTcc.getId(), projetoTcc);
        return ModelMapperUtil.map(projetoTcc, ProjetoTccDTO.class);
    }

    public void cancelar(Long id, String motivo) throws BusinessException {
        var projetoTcc = projetoTccService.getById(id);
        apresentacaoService.deleteIfExistsByProjetoTccIdAndTipoTcc(id, projetoTcc.getTipoTcc());
        projetoTccSituacaoFacade.nextStep(id, SituacaoTcc.CANCELADO, motivo);
    }

    public void reprovar(Long id, String motivo) throws BusinessException {
        var projetoTcc = projetoTccService.getById(id);
        apresentacaoService.deleteIfExistsByProjetoTccIdAndTipoTcc(id, projetoTcc.getTipoTcc());
        projetoTccSituacaoFacade.nextStep(id, SituacaoTcc.REPROVADO, motivo);
    }

    public void validoAgendarApresentacao(@NonNull Long idProjetoTcc) throws BusinessException {
        var errors = new ArrayList<String>();
        var projetoTcc = projetoTccService.getById(idProjetoTcc);
        var membrosBanca = membroBancaService.getByIdProjetoTcc(idProjetoTcc);

        if (membrosBanca.size() > 0) {
            var membrosBancaNaoConfirmados = membrosBanca.stream().filter(mb -> mb.getDataConfirmacao() == null).toList();
            var membrosBancaConfirmados = membrosBanca.stream().filter(mb -> mb.getDataConfirmacao() != null).toList();
            for (var naoConfirmado : membrosBancaNaoConfirmados) {
                var professor = naoConfirmado.getProfessor();
                errors.add("O professor membro de banca " + professor.getNome() + " ainda não confirmou sua participação.");
            }
            for (var confirmado : membrosBancaConfirmados) {
                var professor = confirmado.getProfessor();
                if (!professorDisponibilidadeService.existsByAnoAndPeriodoAndProfessorId(projetoTcc.getAno(), projetoTcc.getPeriodo(), professor.getId())) {
                    errors.add("O professor membro de banca " + professor.getNome() + " confirmou sua participação, porém não cadastrou suas disponibilidades.");
                }
            }
            if (membrosBanca.size() != 2) {
                errors.add("É necessário a definição de exatamente 2 membros de banca");
            }
        } else if (TipoTcc.DEFESA.equals(projetoTcc.getTipoTcc())) {
            errors.add("É necessário a definição de 2 membros de banca antes do agendamento da apresentação.");
        }

        var professorOrientador = projetoTcc.getProfessorOrientador();
        if (!professorDisponibilidadeService.existsByAnoAndPeriodoAndProfessorId(projetoTcc.getAno(), projetoTcc.getPeriodo(), professorOrientador.getId())) {
            errors.add("O professor orientador " + professorOrientador.getNome() + " não cadastrou suas disponibilidades.");
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
    }

    public void avancarParaDefesa(@NonNull Long idProjetoTcc) throws BusinessException {
        var projetoTcc = projetoTccService.getById(idProjetoTcc);
        var situacaoAtual = projetoTcc.getSituacaoAtual();
        if (TipoTcc.DEFESA.equals(situacaoAtual.getTipoTcc())) {
            throw new BusinessException("Este TCC já se encontra em defesa");
        }
        if (!SituacaoTcc.APROVADO.equals(situacaoAtual.getSituacaoTcc())) {
            throw new BusinessException("Este TCC está " + situacaoAtual.getSituacaoTcc().getDescricao() + ", é necessário que esteja aprovado");
        }
        projetoTccSituacaoFacade.toDefesa(idProjetoTcc);
    }

}
