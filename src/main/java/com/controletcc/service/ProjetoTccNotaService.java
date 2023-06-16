package com.controletcc.service;

import com.controletcc.error.BusinessException;
import com.controletcc.model.entity.ModeloAvaliacao;
import com.controletcc.model.entity.ProjetoTcc;
import com.controletcc.model.entity.ProjetoTccNota;
import com.controletcc.model.enums.TipoTcc;
import com.controletcc.repository.ProjetoTccNotaRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class ProjetoTccNotaService {

    private final ProjetoTccNotaRepository projetoTccNotaRepository;

    public ProjetoTccNota generate(@NonNull ProjetoTcc projetoTcc, @NonNull TipoTcc tipoTcc, @NonNull ModeloAvaliacao modeloAvaliacao) {
        var projetoTccNota = new ProjetoTccNota();
        projetoTccNota.setProjetoTcc(projetoTcc);
        projetoTccNota.setTipoTcc(tipoTcc);
        projetoTccNota.setNotaMedia(modeloAvaliacao.getNotaMedia());
        projetoTccNota.setNotaMaxima(modeloAvaliacao.getNotaMaxima());
        return projetoTccNotaRepository.save(projetoTccNota);
    }

    public void updateByModelo(ModeloAvaliacao modeloAvaliacao) {
        var notas = projetoTccNotaRepository.getAllEmAvaliacaoByAreaTcc(modeloAvaliacao.getIdAreaTcc());
        if (!notas.isEmpty()) {
            for (var nota : notas) {
                if (!nota.isEqualModelo(modeloAvaliacao)) {
                    nota.setNotaMedia(modeloAvaliacao.getNotaMedia());
                    nota.setNotaMaxima(modeloAvaliacao.getNotaMaxima());
                    projetoTccNotaRepository.save(nota);
                }
            }
        }
    }

    public ProjetoTccNota getByProjetoTcc(Long idProjetoTcc) {
        return projetoTccNotaRepository.getProjetoTccNotaByProjetoTccId(idProjetoTcc);
    }

    public ProjetoTccNota updateNotaFinal(Long idProjetoTcc, Double notaFinal) {
        var projetoTccNota = getByProjetoTcc(idProjetoTcc);
        projetoTccNota.setNotaFinal(notaFinal);
        return projetoTccNotaRepository.save(projetoTccNota);
    }

}
