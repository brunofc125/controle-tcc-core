package com.controletcc.service;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.enums.OrderByDirection;
import com.controletcc.dto.options.VersaoTccGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.entity.VersaoTcc;
import com.controletcc.repository.VersaoTccRepository;
import com.controletcc.repository.projection.VersaoTccProjection;
import com.controletcc.util.FileAppUtil;
import com.controletcc.util.LocalDateTimeUtil;
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
public class VersaoTccService {

    private final VersaoTccRepository versaoTccRepository;

    public VersaoTcc getById(@NonNull Long id) {
        return versaoTccRepository.getReferenceById(id);
    }

    public ListResponse<VersaoTccProjection> search(@NonNull Long idProjetoTcc, VersaoTccGridOptions options) {
        if (StringUtil.isNullOrBlank(options.getOrderByField())) {
            options.setOrderByField("versao");
            options.setOrderByDirection(OrderByDirection.DESC);
        }
        var page = versaoTccRepository.search(options.getVersao(), options.isUltimaVersao(), idProjetoTcc, options.getPageable());
        return new ListResponse<>(page.getContent(), page.getTotalElements());
    }

    public VersaoTcc insert(@NonNull VersaoTcc versaoTcc) throws BusinessException {
        versaoTcc.setId(null);
        var ultimaVersao = versaoTccRepository.getFirstByProjetoTccIdOrderByVersaoDesc(versaoTcc.getIdProjetoTcc());
        versaoTcc.setVersao(ultimaVersao != null ? ultimaVersao.getVersao() + 1 : 1);
        var dateStr = LocalDateTimeUtil.localDateTimeToString(LocalDateTime.now(), "_yyyy-MM-dd--hh-mm-ss");
        var extension = FileAppUtil.getExtension(versaoTcc.getNomeArquivo());
        versaoTcc.setNomeArquivo("TCC_" + versaoTcc.getVersao() + dateStr + "." + extension);
        validate(versaoTcc);
        return versaoTccRepository.save(versaoTcc);
    }

    private void validate(VersaoTcc versaoTcc) throws BusinessException {
        var errors = new ArrayList<String>();

        if (!StringUtil.isNullOrBlank(versaoTcc.getObservacao()) && versaoTcc.getObservacao().length() > 1000) {
            errors.add("A observação só pode conter até 1000 caracteres");
        }

        if (versaoTcc.getVersao() == null) {
            errors.add("Versão não informada");
        }

        if (versaoTcc.getProjetoTcc() == null) {
            errors.add("É necessário que a versão seja associado ao projeto de TCC correspondente");
        }

        if (versaoTcc.getProfessorOrientador() == null && versaoTcc.getAluno() == null) {
            errors.add("É necessário conter um responsável pela publicação da versão, seja o aluno ou professor orientador deste TCC");
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
    }

    public boolean existsByProjetoTccId(Long idProjetoTcc) {
        return versaoTccRepository.existsByProjetoTccId(idProjetoTcc);
    }

}
