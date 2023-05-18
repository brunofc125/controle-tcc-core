package com.controletcc.service;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.AlunoGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.entity.Aluno;
import com.controletcc.repository.AlunoRepository;
import com.controletcc.repository.projection.AlunoProjection;
import com.controletcc.util.AuthUtil;
import com.controletcc.util.StringUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class AlunoService {

    private final AlunoRepository alunoRepository;

    public Aluno getById(@NonNull Long id) {
        return alunoRepository.getReferenceById(id);
    }

    public ListResponse<AlunoProjection> search(AlunoGridOptions options) {
        var page = alunoRepository.search(options.getId(), options.getNome(), options.getEmail(), options.getMatricula(), options.getIdAreaTcc(), options.getPageable());
        return new ListResponse<>(page.getContent(), page.getTotalElements());
    }

    public Aluno insert(@NonNull Aluno aluno) throws BusinessException {
        aluno.setId(null);
        validate(aluno);
        return alunoRepository.save(aluno);
    }

    public Aluno update(@NonNull Long id, @NonNull Aluno aluno) throws BusinessException {
        var alunoBanco = getById(id);
        aluno.setId(id);
        aluno.setUsuario(alunoBanco.getUsuario());
        validate(aluno);
        return alunoRepository.save(aluno);
    }

    private void validate(Aluno aluno) throws BusinessException {
        var errors = new ArrayList<String>();
        if (StringUtil.isNullOrBlank(aluno.getNome())) {
            errors.add("Nome não informado");
        }

        if (aluno.getId() == null) {
            if (alunoRepository.existsByMatricula(aluno.getMatricula())) {
                errors.add("Já existe outro aluno com esta matrícula");
            }
        } else if (alunoRepository.existsByMatriculaAndIdNot(aluno.getMatricula(), aluno.getId())) {
            errors.add("Já existe outro aluno com esta matrícula");
        }

        errors.addAll(aluno.getPessoaErrors());

        if (aluno.getUsuario() == null) {
            errors.add("Aluno sem usuário informado");
        } else if (aluno.getId() != null) {
            var alunoBanco = getById(aluno.getId());
            if (alunoBanco == null) {
                errors.add("Aluno não foi encontrado com o ID: " + aluno.getId());
            } else if (!aluno.getIdUsuario().equals(alunoBanco.getIdUsuario())) {
                errors.add("Não é possível alterar o usuário de um aluno");
            }
        }

        if (StringUtil.isNullOrBlank(aluno.getMatricula())) {
            errors.add("Matrícula não informada");
        }

        if (aluno.getAreaTcc() == null) {
            errors.add("Área de TCC não informada");
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
    }

    public Aluno getAlunoLogado() throws BusinessException {
        var aluno = getAlunoByUsuarioId(AuthUtil.getUserIdLogged());
        if (aluno == null) {
            throw new BusinessException("O usuário logado não é um aluno");
        }
        return aluno;
    }

    public Aluno getAlunoByUsuarioId(Long idUsuario) {
        return alunoRepository.getAlunoByUsuarioId(idUsuario);
    }

    public List<Aluno> getAllByIdAreaTcc(Long idAreaTcc) {
        return alunoRepository.getAllByAreaTccId(idAreaTcc);
    }

}
