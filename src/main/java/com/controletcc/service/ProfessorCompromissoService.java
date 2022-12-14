package com.controletcc.service;

import com.controletcc.error.BusinessException;
import com.controletcc.model.entity.ProfessorCompromisso;
import com.controletcc.repository.ProfessorCompromissoRepository;
import com.controletcc.util.StringUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class ProfessorCompromissoService {

    private final ProfessorCompromissoRepository professorCompromissoRepository;

    public ProfessorCompromisso getById(@NonNull Long id) {
        return professorCompromissoRepository.getReferenceById(id);
    }

    public ProfessorCompromisso save(@NonNull ProfessorCompromisso professorCompromisso) throws BusinessException {
        validate(professorCompromisso);
        return professorCompromissoRepository.save(professorCompromisso);
    }

    public boolean existsIntersect(Long id, @NonNull Long idProfessor, @NonNull LocalDateTime dataInicial, @NonNull LocalDateTime dataFinal) {
        return professorCompromissoRepository.existsIntersect(id, idProfessor, dataInicial, dataFinal);
    }

    private void validate(ProfessorCompromisso professorCompromisso) throws BusinessException {
        var errors = new ArrayList<String>();
        var dataAtual = LocalDateTime.now();

        if (professorCompromisso.getProfessor() == null) {
            errors.add("Professor não informado");
        }

        if (StringUtil.isNullOrBlank(professorCompromisso.getDescricao())) {
            errors.add("Descrição não informada");
        }

        if (professorCompromisso.getDataInicial() == null) {
            errors.add("Data inicial não informada");
        } else if (professorCompromisso.getDataFinal() != null && professorCompromisso.getDataInicial().isAfter(professorCompromisso.getDataFinal())) {
            errors.add("Data inicial deve ser menor ou igual a data final");
        }

        if (professorCompromisso.getDataFinal() == null) {
            errors.add("Data final das apresentações não informada");
        } else if (dataAtual.isAfter(professorCompromisso.getDataFinal())) {
            errors.add("Data final deve ser maior ou igual a data atual");
        }

        if (errors.isEmpty() && existsIntersect(professorCompromisso.getId(), professorCompromisso.getIdProfessor(), professorCompromisso.getDataInicial(), professorCompromisso.getDataFinal())) {
            errors.add("O compromisso " + professorCompromisso.getDescricao() + " está interpolando com outro compromisso");
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
    }

    public void delete(Long id) {
        professorCompromissoRepository.deleteById(id);
    }

}
