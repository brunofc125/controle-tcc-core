package com.controletcc.service;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.ProfessorGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.entity.Professor;
import com.controletcc.repository.ProfessorRepository;
import com.controletcc.repository.projection.ProfessorProjection;
import com.controletcc.util.AuthUtil;
import com.controletcc.util.StringUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public Professor getById(@NonNull Long id) {
        return professorRepository.getReferenceById(id);
    }

    public ListResponse<ProfessorProjection> search(ProfessorGridOptions options) {
        var page = professorRepository.search(options.getId(), options.getNome(), options.getEmail(), options.isCategoriaSupervisor(), options.getPageable());
        return new ListResponse<>(page.getContent(), page.getTotalElements());
    }

    public Professor insert(@NonNull Professor professor) throws BusinessException {
        professor.setId(null);
        validate(professor);
        return professorRepository.save(professor);
    }

    public Professor update(@NonNull Long id, @NonNull Professor professor) throws BusinessException {
        var professorBanco = getById(id);
        professor.setId(id);
        professor.setUsuario(professorBanco.getUsuario());
        validate(professor);
        return professorRepository.save(professor);
    }

    private void validate(Professor professor) throws BusinessException {
        var errors = new ArrayList<String>();
        if (StringUtil.isNullOrBlank(professor.getNome())) {
            errors.add("Nome não informado");
        }

        errors.addAll(professor.getPessoaErrors());

        if (professor.getUsuario() == null) {
            errors.add("Professor sem usuário informado");
        } else if (professor.getId() != null) {
            var professorBanco = getById(professor.getId());
            if (professorBanco == null) {
                errors.add("Professor não foi encontrado com o ID: " + professor.getId());
            } else if (!professor.getIdUsuario().equals(professorBanco.getIdUsuario())) {
                errors.add("Não é possível alterar o usuário de um professor");
            }
        }

        if (professor.getAreas() == null || professor.getAreas().isEmpty()) {
            errors.add("Áreas de TCC não informadas");
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
    }

    public Professor getProfessorByUsuarioId(Long idUsuario) {
        return professorRepository.getProfessorByUsuarioId(idUsuario);
    }

    public List<Professor> getSupervisoresByIdAreaTcc(Long idAreaTcc) {
        return professorRepository.getSupervisoresByIdAreaTcc(idAreaTcc);
    }

    public List<Professor> getAllByAreaTccAndNotProfessores(Long idAreaTcc, List<Long> idProfessores) {
        return professorRepository.getAllByAreaTccAndNotProfessores(idAreaTcc, idProfessores);
    }

    public Professor getProfessorLogado() throws BusinessException {
        var professor = getProfessorByUsuarioId(AuthUtil.getUserIdLogged());
        if (professor == null) {
            throw new BusinessException("O usuário logado não é um professor");
        }
        return professor;
    }

    public Map<Long, String> getNomeMappedByIds(List<Long> idList) {
        var professores = professorRepository.getAllByIdIn(idList);
        return professores.stream().collect(toMap(Professor::getId, Professor::getNome));
    }

}
