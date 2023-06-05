package com.controletcc.service;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.enums.OrderByDirection;
import com.controletcc.dto.options.base.BaseGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.entity.VersaoTcc;
import com.controletcc.model.entity.VersaoTccObservacao;
import com.controletcc.repository.VersaoTccObservacaoRepository;
import com.controletcc.repository.projection.VersaoTccObservacaoProjection;
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
public class VersaoTccObservacaoService {

    private final VersaoTccObservacaoRepository versaoTccObservacaoRepository;

    public VersaoTccObservacao getById(@NonNull Long id) {
        return versaoTccObservacaoRepository.getReferenceById(id);
    }

    public ListResponse<VersaoTccObservacaoProjection> search(@NonNull Long idVersaoTcc, boolean onlyAvaliacao, BaseGridOptions options) {
        if (StringUtil.isNullOrBlank(options.getOrderByField())) {
            options.setOrderByField("dataInclusao");
            options.setOrderByDirection(OrderByDirection.DESC);
        }
        var page = versaoTccObservacaoRepository.search(idVersaoTcc, onlyAvaliacao, options.getPageable());
        return new ListResponse<>(page.getContent(), page.getTotalElements());
    }

    public VersaoTccObservacao insert(@NonNull VersaoTccObservacao versaoTccObservacao, @NonNull VersaoTcc versaoTcc) throws BusinessException {
        versaoTccObservacao.setId(null);
        versaoTccObservacao.setVersaoTcc(versaoTcc);
        if (versaoTccObservacao.getConteudo() != null && versaoTccObservacao.getConteudo().length > 0) {
            var dateStr = LocalDateTimeUtil.localDateTimeToString(LocalDateTime.now(), "_yyyy-MM-dd--hh-mm-ss");
            var extension = FileAppUtil.getExtension(versaoTcc.getNomeArquivo());
            versaoTccObservacao.setNomeArquivo("TCC_OBS_" + versaoTcc.getVersao() + dateStr + "." + extension);
        }
        validate(versaoTccObservacao);
        return versaoTccObservacaoRepository.save(versaoTccObservacao);
    }

    private void validate(VersaoTccObservacao versaoTccObservacao) throws BusinessException {
        var errors = new ArrayList<String>();

        if (!StringUtil.isNullOrBlank(versaoTccObservacao.getObservacao()) && versaoTccObservacao.getObservacao().length() > 4000) {
            errors.add("A observação só pode conter até 4000 caracteres");
        }

        if (versaoTccObservacao.getVersaoTcc() == null) {
            errors.add("Versão não informada");
        }

        if (StringUtil.isNullOrBlank(versaoTccObservacao.getObservacao()) && !versaoTccObservacao.isFileValid()) {
            errors.add("É necessário informar a observação ou anexar um documento");
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
    }

}
