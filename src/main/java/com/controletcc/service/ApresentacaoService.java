package com.controletcc.service;

import com.controletcc.error.BusinessException;
import com.controletcc.model.entity.Apresentacao;
import com.controletcc.model.enums.TipoTcc;
import com.controletcc.repository.ApresentacaoRepository;
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
public class ApresentacaoService {

    private final ApresentacaoRepository apresentacaoRepository;

    public Apresentacao getById(@NonNull Long id) {
        return apresentacaoRepository.getReferenceById(id);
    }

    public Apresentacao getFirstByProjetoTccIdAndTipoTcc(Long idProjetoTcc, TipoTcc tipoTcc) {
        return apresentacaoRepository.getFirstByProjetoTccIdAndTipoTcc(idProjetoTcc, tipoTcc);
    }

    public Apresentacao insert(@NonNull Apresentacao apresentacao) throws BusinessException {
        apresentacao.setId(null);
        validate(apresentacao);
        return apresentacaoRepository.save(apresentacao);
    }

    public Apresentacao update(@NonNull Long id, @NonNull Apresentacao apresentacao) throws BusinessException {
        apresentacao.setId(id);
        validate(apresentacao);
        return apresentacaoRepository.save(apresentacao);
    }

    public boolean existsIntersect(Long id, @NonNull Long idAgendaApresentacao, @NonNull LocalDateTime dataInicial, @NonNull LocalDateTime dataFinal) {
        return apresentacaoRepository.existsIntersect(id, idAgendaApresentacao, dataInicial, dataFinal);
    }

    private void validate(Apresentacao apresentacao) throws BusinessException {
        var errors = new ArrayList<String>();
        var projetoTcc = apresentacao.getProjetoTcc();
        var agendaApresentacao = apresentacao.getAgendaApresentacao();
        var dataAtual = LocalDateTime.now();

        if (agendaApresentacao != null && projetoTcc != null && apresentacao.getTipoTcc() != null) {
            if (apresentacao.getId() == null && apresentacaoRepository.existsByProjetoTccIdAndTipoTcc(projetoTcc.getId(), apresentacao.getTipoTcc())) {
                errors.add("Já existe uma apresentação cadastrada para a etapa de " + apresentacao.getTipoTcc().getDescricao());
            }

            if (!agendaApresentacao.getTipoTcc().equals(projetoTcc.getTipoTcc())) {
                errors.add("A agenda de apresentação tem que ser da mesma etapa em que se encontra o TCC");
            } else if (!apresentacao.getTipoTcc().equals(projetoTcc.getTipoTcc())) {
                errors.add("A apresentação tem que ser da mesma etapa em que se encontra o TCC");
            }
        }

        if (apresentacao.getAgendaApresentacao() == null) {
            errors.add("Agenda não informada");
        }

        if (apresentacao.getProjetoTcc() == null) {
            errors.add("Projeto TCC não informado");
        }

        if (apresentacao.getTipoTcc() == null) {
            errors.add("Etapa do TCC não informada");
        }

        if (apresentacao.getDataInicial() == null) {
            errors.add("Data inicial da apresentação não informada");
        } else if (apresentacao.getDataFinal() != null && apresentacao.getDataInicial().isAfter(apresentacao.getDataFinal())) {
            errors.add("Data inicial deve ser menor ou igual a data final");
        }

        if (apresentacao.getDataFinal() == null) {
            errors.add("Data final da apresentação não informada");
        } else if (dataAtual.isAfter(apresentacao.getDataFinal())) {
            errors.add("Data final deve ser maior ou igual a data atual");
        }

        if (errors.isEmpty() && existsIntersect(apresentacao.getId(), apresentacao.getIdAgendaApresentacao(), apresentacao.getDataInicial(), apresentacao.getDataFinal())) {
            errors.add("Já existe uma apresentação que interpola com as datas informadas");
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
    }

    public List<Apresentacao> getAllByAgendaApresentacaoIdAndProjetoTccIdNot(Long idAgendaApresentacao, Long idProjetoTcc) {
        return apresentacaoRepository.getAllByAgendaApresentacaoIdAndProjetoTccIdNot(idAgendaApresentacao, idProjetoTcc);
    }

}
