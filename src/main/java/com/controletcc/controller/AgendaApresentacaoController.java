package com.controletcc.controller;

import com.controletcc.dto.AgendaParaApresentacaoDTO;
import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.AgendaApresentacaoGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.facade.AgendaApresentacaoFacade;
import com.controletcc.model.dto.AgendaApresentacaoDTO;
import com.controletcc.repository.projection.AgendaApresentacaoProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/agendas-apresentacoes")
@RequiredArgsConstructor
public class AgendaApresentacaoController {

    private final AgendaApresentacaoFacade agendaApresentacaoFacade;

    @PreAuthorize("hasAuthority('agenda-apresentacao.read')")
    @GetMapping("{id}")
    public AgendaApresentacaoDTO getById(@PathVariable Long id) {
        return agendaApresentacaoFacade.getById(id);
    }

    @PreAuthorize("hasAuthority('agenda-apresentacao.read')")
    @PostMapping("search")
    public ListResponse<AgendaApresentacaoProjection> search(@RequestBody AgendaApresentacaoGridOptions options) throws BusinessException {
        return agendaApresentacaoFacade.search(options);
    }

    @PreAuthorize("hasAuthority('agenda-apresentacao.create')")
    @PostMapping
    public AgendaApresentacaoDTO insert(@RequestBody AgendaApresentacaoDTO agendaApresentacao) throws BusinessException {
        return agendaApresentacaoFacade.insert(agendaApresentacao);
    }

    @PreAuthorize("hasAuthority('agenda-apresentacao.create')")
    @PutMapping("{id}")
    public AgendaApresentacaoDTO update(@PathVariable Long id, @RequestBody AgendaApresentacaoDTO agendaApresentacao) throws BusinessException {
        agendaApresentacao.setId(id);
        return agendaApresentacaoFacade.update(agendaApresentacao);
    }

    @PreAuthorize("hasAnyAuthority('agenda-apresentacao.read', 'apresentacao.read')")
    @GetMapping("ativas-by-projeto-tcc/{idProjetoTcc}")
    public List<AgendaApresentacaoDTO> getAgendasAtivasByIdProjetoTcc(@PathVariable Long idProjetoTcc) {
        return agendaApresentacaoFacade.getAgendasAtivasByIdProjetoTcc(idProjetoTcc);
    }

    @PreAuthorize("hasAnyAuthority('agenda-apresentacao.read', 'apresentacao.read', 'professor-compromisso.read')")
    @GetMapping("ativas-by-professor-logado")
    public List<AgendaApresentacaoDTO> getAgendasAtivasByProfessorLogado() throws BusinessException {
        return agendaApresentacaoFacade.getAgendasAtivasByProfessorLogado();
    }

    @PreAuthorize("hasAnyAuthority('agenda-apresentacao.read', 'apresentacao.read')")
    @GetMapping("agenda-para-apresentacao/{idAgendaApresentacao}/{idProjetoTcc}")
    public AgendaParaApresentacaoDTO getAgendaParaApresentacao(@PathVariable Long idAgendaApresentacao, @PathVariable Long idProjetoTcc) {
        return agendaApresentacaoFacade.getAgendaParaApresentacao(idAgendaApresentacao, idProjetoTcc);
    }
}
