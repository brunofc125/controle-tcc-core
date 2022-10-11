package com.controletcc.facade;

import com.controletcc.dto.SaveProfessorDTO;
import com.controletcc.dto.base.ListResponseModel;
import com.controletcc.dto.options.ProfessorGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.dto.ProfessorDTO;
import com.controletcc.model.entity.Professor;
import com.controletcc.model.entity.User;
import com.controletcc.model.enums.UserType;
import com.controletcc.repository.projection.ProfessorProjection;
import com.controletcc.service.ProfessorService;
import com.controletcc.service.UserService;
import com.controletcc.util.ModelMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class ProfessorFacade {

    private final ProfessorService professorService;

    private final UserService userService;

    public ProfessorDTO getById(Long id) {
        var professor = professorService.getById(id);
        return ModelMapperUtil.map(professor, ProfessorDTO.class);
    }

    public ListResponseModel<ProfessorProjection> search(ProfessorGridOptions options) {
        return professorService.search(options);
    }

    public ProfessorDTO insert(SaveProfessorDTO saveProfessor) throws BusinessException {
        var professor = ModelMapperUtil.map(saveProfessor.getProfessor(), Professor.class);
        var usuario = ModelMapperUtil.map(saveProfessor.getUser(), User.class);
        usuario.setName(professor.getNome());
        usuario = userService.insert(usuario, professor.isSupervisorTcc() ? UserType.SUPERVISOR : UserType.PROFESSOR);
        professor.setUsuario(usuario);
        professor = professorService.insert(professor);
        return ModelMapperUtil.map(professor, ProfessorDTO.class);
    }

    public ProfessorDTO update(ProfessorDTO professorDTO) throws BusinessException {
        var professor = ModelMapperUtil.map(professorDTO, Professor.class);
        var professorBanco = professorService.getById(professor.getId());
        if (professor.isSupervisorTcc() != professorBanco.isSupervisorTcc()) {
            var userType = professor.isSupervisorTcc() ? UserType.SUPERVISOR : UserType.PROFESSOR;
            userService.updateRoles(professorBanco.getIdUsuario(), userType);
        }
        userService.updateName(professorBanco.getIdUsuario(), professor.getNome());
        professor = professorService.update(professor.getId(), professor);
        return ModelMapperUtil.map(professor, ProfessorDTO.class);
    }


}
