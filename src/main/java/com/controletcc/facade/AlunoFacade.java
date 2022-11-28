package com.controletcc.facade;

import com.controletcc.dto.SaveAlunoDTO;
import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.AlunoGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.dto.AlunoDTO;
import com.controletcc.model.dto.UserDTO;
import com.controletcc.model.entity.Aluno;
import com.controletcc.model.entity.User;
import com.controletcc.model.enums.UserType;
import com.controletcc.repository.projection.AlunoProjection;
import com.controletcc.service.AlunoService;
import com.controletcc.service.UserService;
import com.controletcc.util.ModelMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class AlunoFacade {

    private final AlunoService alunoService;

    private final UserService userService;

    public AlunoDTO getById(Long id) {
        var aluno = alunoService.getById(id);
        return ModelMapperUtil.map(aluno, AlunoDTO.class);
    }

    public ListResponse<AlunoProjection> search(AlunoGridOptions options) {
        return alunoService.search(options);
    }

    public AlunoDTO insert(SaveAlunoDTO saveAluno) throws BusinessException {
        var aluno = ModelMapperUtil.map(saveAluno.getAluno(), Aluno.class);
        var usuario = ModelMapperUtil.map(saveAluno.getUser(), User.class);
        usuario.setName(aluno.getNome());
        usuario = userService.insert(usuario, UserType.ALUNO);
        aluno.setUsuario(usuario);
        aluno = alunoService.insert(aluno);
        return ModelMapperUtil.map(aluno, AlunoDTO.class);
    }

    public AlunoDTO update(AlunoDTO alunoDTO) throws BusinessException {
        var aluno = ModelMapperUtil.map(alunoDTO, Aluno.class);
        var alunoBanco = alunoService.getById(aluno.getId());
        userService.updateName(alunoBanco.getIdUsuario(), aluno.getNome());
        aluno = alunoService.update(aluno.getId(), aluno);
        return ModelMapperUtil.map(aluno, AlunoDTO.class);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = BusinessException.class)
    public void insertTransactional(AlunoDTO alunoDTO, UserDTO userDTO) throws BusinessException {
        var aluno = ModelMapperUtil.map(alunoDTO, Aluno.class);
        var usuario = ModelMapperUtil.map(userDTO, User.class);
        usuario.setName(aluno.getNome());
        usuario = userService.insert(usuario, UserType.ALUNO);
        aluno.setUsuario(usuario);
        alunoService.insert(aluno);
    }

    public List<AlunoDTO> getAllByIdAreaTcc(Long idAreaTcc) {
        return ModelMapperUtil.mapAll(alunoService.getAllByIdAreaTcc(idAreaTcc), AlunoDTO.class);
    }

}

