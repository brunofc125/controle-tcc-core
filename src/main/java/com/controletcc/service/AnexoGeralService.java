package com.controletcc.service;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.enums.OrderByDirection;
import com.controletcc.dto.options.AnexoGeralGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.entity.AnexoGeral;
import com.controletcc.repository.AnexoGeralRepository;
import com.controletcc.repository.projection.AnexoGeralProjection;
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
public class AnexoGeralService {

    private final AnexoGeralRepository anexoGeralRepository;

    public AnexoGeral getById(@NonNull Long id) {
        return anexoGeralRepository.getReferenceById(id);
    }

    public ListResponse<AnexoGeralProjection> search(@NonNull Long idProjetoTcc, boolean onlyAvaliacao, AnexoGeralGridOptions options) {
        if (StringUtil.isNullOrBlank(options.getOrderByField())) {
            options.setOrderByField("id");
            options.setOrderByDirection(OrderByDirection.DESC);
        }
        var page = anexoGeralRepository.search(options.getId(), options.getDescricao(), options.getTipoTcc(), idProjetoTcc, onlyAvaliacao, options.getPageable());
        return new ListResponse<>(page.getContent(), page.getTotalElements());
    }

    public AnexoGeral insert(@NonNull AnexoGeral anexoGeral) throws BusinessException {
        anexoGeral.setId(null);
        anexoGeral.setDataExclusao(null);
        validate(anexoGeral);
        return anexoGeralRepository.save(anexoGeral);
    }

    public void delete(@NonNull Long id) throws BusinessException {
        var anexoGeral = anexoGeralRepository.getReferenceById(id);
        anexoGeral.setDataExclusao(LocalDateTime.now());
        anexoGeralRepository.save(anexoGeral);
    }

    private void validate(AnexoGeral anexoGeral) throws BusinessException {
        var errors = new ArrayList<String>();

        if (StringUtil.isNullOrBlank(anexoGeral.getDescricao())) {
            errors.add("Descrição do anexo não informada");
        } else if (anexoGeral.getProjetoTcc() != null && anexoGeralRepository.existsByProjetoTccIdAndDescricaoIgnoreCaseAndDataExclusaoIsNullAndAvaliacao(anexoGeral.getIdProjetoTcc(), anexoGeral.getDescricao().trim().toLowerCase(), anexoGeral.isAvaliacao())) {
            errors.add("Já existe outro anexo com esta descrição");
        }

        if (anexoGeral.getProfessor() == null) {
            errors.add("É necessário que o anexo seja associado ao professor responsável");
        }

        if (anexoGeral.getProjetoTcc() == null) {
            errors.add("É necessário que o anexo seja associado ao projeto de TCC correspondente");
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
    }

}
