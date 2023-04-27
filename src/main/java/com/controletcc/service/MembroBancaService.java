package com.controletcc.service;

import com.controletcc.error.BusinessException;
import com.controletcc.model.entity.MembroBanca;
import com.controletcc.model.entity.Professor;
import com.controletcc.repository.MembroBancaRepository;
import com.controletcc.repository.projection.MembroBancaProjection;
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
public class MembroBancaService {

    private final MembroBancaRepository membroBancaRepository;

    public MembroBanca getById(@NonNull Long id) {
        return membroBancaRepository.getReferenceById(id);
    }

    public List<MembroBancaProjection> getAllByIdProjetoTcc(@NonNull Long idProjetoTcc) throws BusinessException {
        return membroBancaRepository.getAllByIdProjetoTcc(idProjetoTcc);
    }

    public MembroBanca insert(@NonNull MembroBanca membroBanca) throws BusinessException {
        membroBanca.setId(null);
        validate(membroBanca);
        return membroBancaRepository.save(membroBanca);
    }

    public boolean existsByProjetoTccAndProfessor(Long idProjetoTcc, Long idProfessor) {
        return membroBancaRepository.existsByProjetoTccIdAndProfessorId(idProjetoTcc, idProfessor);
    }

    private void validate(MembroBanca membroBanca) throws BusinessException {
        var errors = new ArrayList<String>();

        if (membroBanca.getProjetoTcc() == null) {
            errors.add("Projeto TCC não informado");
        }

        if (membroBanca.getProfessor() == null) {
            errors.add("Professor não informado");
        }

        if (membroBanca.getProjetoTcc() != null && membroBanca.getProfessor() != null && existsByProjetoTccAndProfessor(membroBanca.getIdProjetoTcc(), membroBanca.getIdProfessor())) {
            errors.add("Este professor já foi solicitado como membro da banca deste TCC");
        }

        if (membroBanca.getDataSolicitacao() == null) {
            errors.add("Data da solicitação não informada");
        } else if (LocalDateTime.now().isBefore(membroBanca.getDataSolicitacao())) {
            errors.add("Data de solicitação não pode ser uma data futura");
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
    }

    public List<MembroBanca> getByIdProjetoTcc(Long idProjetoTcc) {
        return membroBancaRepository.getAllByProjetoTccId(idProjetoTcc);
    }

    public void delete(Long id) {
        membroBancaRepository.deleteById(id);
    }

    private MembroBanca getByProjetoTccIdAndProfessorId(@NonNull Long idProjetoTcc, @NonNull Long idProfessor) throws BusinessException {
        var membroBanca = membroBancaRepository.getByProjetoTccIdAndProfessorId(idProjetoTcc, idProfessor);
        if (membroBanca == null) {
            throw new BusinessException("Você não é membro da banca deste projeto de TCC");
        }
        return membroBanca;
    }

    public void confirmar(@NonNull Long idProjetoTcc, @NonNull Professor professor) throws BusinessException {
        var membroBanca = getByProjetoTccIdAndProfessorId(idProjetoTcc, professor.getId());
        if (membroBanca.getDataConfirmacao() != null) {
            throw new BusinessException("A confirmação já foi realizada");
        }
        membroBanca.setDataConfirmacao(LocalDateTime.now());
        membroBancaRepository.save(membroBanca);
    }

    public void desconfirmar(@NonNull Long idProjetoTcc, @NonNull Professor professor) throws BusinessException {
        var membroBanca = getByProjetoTccIdAndProfessorId(idProjetoTcc, professor.getId());
        if (membroBanca.getDataConfirmacao() == null) {
            throw new BusinessException("A confirmação não foi feita ainda");
        }
        membroBanca.setDataConfirmacao(null);
        membroBancaRepository.save(membroBanca);
    }

}
