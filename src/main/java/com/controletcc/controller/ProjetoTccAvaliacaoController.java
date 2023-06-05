package com.controletcc.controller;

import com.controletcc.dto.ProjetoTccAvaliacaoInfoDTO;
import com.controletcc.dto.ProjetoTccAvaliacaoResumeDTO;
import com.controletcc.dto.base.ListResponse;
import com.controletcc.facade.ProjetoTccAvaliacaoFacade;
import com.controletcc.model.dto.ProjetoTccAspectoAvaliacaoDTO;
import com.controletcc.model.dto.ProjetoTccAvaliacaoDTO;
import com.controletcc.model.enums.TipoProfessor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/projeto-tcc-avaliacoes")
@RequiredArgsConstructor
public class ProjetoTccAvaliacaoController {

    private final ProjetoTccAvaliacaoFacade projetoTccAvaliacaoFacade;

    @PreAuthorize("hasAuthority('projeto-tcc-avaliacao.read')")
    @GetMapping("{id}")
    public ProjetoTccAvaliacaoDTO getById(@PathVariable Long id) throws Exception {
        return projetoTccAvaliacaoFacade.getById(id);
    }

    @PreAuthorize("hasAuthority('projeto-tcc-avaliacao.create')")
    @PatchMapping("iniciar-etapa-avaliacao/{idProjetoTcc}")
    public void iniciarEtapaAvaliacao(@PathVariable Long idProjetoTcc) throws Exception {
        projetoTccAvaliacaoFacade.iniciarEtapaAvaliacao(idProjetoTcc);
    }

    @PreAuthorize("hasAuthority('projeto-tcc-avaliacao.read')")
    @GetMapping("info-avaliacao/{idProjetoTcc}")
    public ProjetoTccAvaliacaoInfoDTO getInfoByProjetoTcc(@PathVariable Long idProjetoTcc) throws Exception {
        return projetoTccAvaliacaoFacade.getInfoByProjetoTcc(idProjetoTcc);
    }

    @PreAuthorize("hasAuthority('projeto-tcc-avaliacao.read')")
    @GetMapping("projeto-tcc/{idProjetoTcc}/tipo-professor/{tipoProfessor}/professor/{idProfessor}")
    public ProjetoTccAvaliacaoDTO getByProjetoTccAndTipoProfessorAndProfessor(@PathVariable Long idProjetoTcc, @PathVariable TipoProfessor tipoProfessor, @PathVariable Long idProfessor) throws Exception {
        return projetoTccAvaliacaoFacade.getByProjetoTccAndTipoProfessorAndProfessor(idProjetoTcc, tipoProfessor, idProfessor);
    }

    @PreAuthorize("hasAuthority('projeto-tcc-avaliacao.assess')")
    @PostMapping("save-aspectos-valor/{idProjetoTccAvaliacao}")
    public ProjetoTccAvaliacaoDTO saveAspectosValor(@PathVariable Long idProjetoTccAvaliacao, @RequestBody List<ProjetoTccAspectoAvaliacaoDTO> aspectos) throws Exception {
        return projetoTccAvaliacaoFacade.saveAspectosValor(idProjetoTccAvaliacao, aspectos);
    }

    @PreAuthorize("hasAuthority('projeto-tcc-avaliacao.create')")
    @PostMapping("save-aspectos/{idProjetoTccAvaliacao}")
    public ProjetoTccAvaliacaoDTO saveAspectos(@PathVariable Long idProjetoTccAvaliacao, @RequestBody List<ProjetoTccAspectoAvaliacaoDTO> aspectos) throws Exception {
        return projetoTccAvaliacaoFacade.saveAspectos(idProjetoTccAvaliacao, aspectos);
    }

    @PreAuthorize("hasAuthority('projeto-tcc-avaliacao.read')")
    @GetMapping("get-all-by-projeto-tcc/{idProjetoTcc}")
    public ListResponse<ProjetoTccAvaliacaoResumeDTO> getAllByProjetoTcc(@PathVariable Long idProjetoTcc) throws Exception {
        return projetoTccAvaliacaoFacade.getAllByProjetoTcc(idProjetoTcc);
    }

}
