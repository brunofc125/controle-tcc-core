package com.controletcc.service;

import com.controletcc.error.BusinessException;
import com.controletcc.model.entity.ProfessorDisponibilidade;
import com.controletcc.repository.ProfessorDisponibilidadeRepository;
import com.controletcc.repository.projection.ProfessorDisponibilidadeAgrupadaProjection;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class ProfessorDisponibilidadeService {

    private final ProfessorDisponibilidadeRepository professorDisponibilidadeRepository;

    public ProfessorDisponibilidade getById(@NonNull Long id) {
        return professorDisponibilidadeRepository.getReferenceById(id);
    }

    public List<ProfessorDisponibilidade> getAllByAnoPeriodoAndProfessor(Integer ano, Integer periodo, Long idProfessor) {
        return professorDisponibilidadeRepository.getAllByAnoAndPeriodoAndProfessorId(ano, periodo, idProfessor);
    }

    public ProfessorDisponibilidade save(@NonNull ProfessorDisponibilidade professorDisponibilidade) throws BusinessException {
        validate(professorDisponibilidade);
        return professorDisponibilidadeRepository.save(professorDisponibilidade);
    }

    public boolean existsIntersect(Long id, @NonNull Long idProfessor, @NonNull LocalDateTime dataInicial, @NonNull LocalDateTime dataFinal, @NonNull Integer ano, @NonNull Integer periodo) {
        return professorDisponibilidadeRepository.existsIntersect(id, idProfessor, dataInicial, dataFinal, ano, periodo);
    }

    private void validate(ProfessorDisponibilidade professorDisponibilidade) throws BusinessException {
        var errors = new ArrayList<String>();
        var dataAtual = LocalDateTime.now();

        if (professorDisponibilidade.getProfessor() == null) {
            errors.add("Professor não informado");
        }

        if (professorDisponibilidade.getAno() == null || professorDisponibilidade.getPeriodo() == null) {
            errors.add("Ano/Período não informado");
        } else if (professorDisponibilidade.getAno() != null && dataAtual.getYear() < professorDisponibilidade.getAno()) {
            errors.add("Ano/Período não pode ser futuro");
        }

        if (professorDisponibilidade.getDataInicial() == null) {
            errors.add("Data inicial não informada");
        } else if (professorDisponibilidade.getDataFinal() != null && professorDisponibilidade.getDataInicial().isAfter(professorDisponibilidade.getDataFinal())) {
            errors.add("Data inicial deve ser menor ou igual a data final");
        }

        if (professorDisponibilidade.getDataFinal() == null) {
            errors.add("Data final das apresentações não informada");
        } else if (dataAtual.isAfter(professorDisponibilidade.getDataFinal())) {
            errors.add("Data final deve ser maior ou igual a data atual");
        }

        if (errors.isEmpty() && existsIntersect(professorDisponibilidade.getId(), professorDisponibilidade.getIdProfessor(), professorDisponibilidade.getDataInicial(), professorDisponibilidade.getDataFinal(), professorDisponibilidade.getAno(), professorDisponibilidade.getPeriodo())) {
            errors.add("Existem cadastros de disponibilidades se interpolando");
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
    }

    public void delete(Long id) {
        professorDisponibilidadeRepository.deleteById(id);
    }

    public List<ProfessorDisponibilidadeAgrupadaProjection> getDisponibilidades(List<Long> idProfessores, Long idProjetoTcc, Long idAgendaApresentacao, LocalDateTime dataInicio, LocalDateTime dataFim) {
        return professorDisponibilidadeRepository.getDisponibilidades(idProfessores, idProjetoTcc, idAgendaApresentacao, dataInicio, dataFim);
    }

    public boolean existsByAnoAndPeriodoAndProfessorId(Integer ano, Integer periodo, Long idProfessor) {
        return professorDisponibilidadeRepository.existsByAnoAndPeriodoAndProfessorId(ano, periodo, idProfessor);
    }

}
