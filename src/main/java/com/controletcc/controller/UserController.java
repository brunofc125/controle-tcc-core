package com.controletcc.controller;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.UserGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.facade.UserFacade;
import com.controletcc.model.dto.UserDTO;
import com.controletcc.repository.projection.UserProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;

    @PreAuthorize("hasAuthority('usuario.read')")
    @GetMapping("{id}")
    public UserDTO getById(@PathVariable Long id) throws BusinessException {
        return userFacade.getById(id);
    }

    @PreAuthorize("hasAuthority('usuario.read')")
    @PostMapping("search")
    public ListResponse<UserProjection> search(@RequestBody UserGridOptions options) throws BusinessException {
        return userFacade.search(options);
    }

    @PreAuthorize("hasAuthority('usuario.create')")
    @PostMapping("admin")
    public UserDTO insert(@RequestBody UserDTO user) throws BusinessException {
        return userFacade.insertAdmin(user);
    }

    @PreAuthorize("hasAuthority('usuario.create')")
    @PutMapping("{id}")
    public UserDTO update(@PathVariable Long id, @RequestBody UserDTO user) throws BusinessException {
        return userFacade.update(id, user);
    }

    @PreAuthorize("hasAuthority('usuario.create')")
    @PatchMapping("enable/{id}")
    public UserDTO updateEnable(@PathVariable Long id, @RequestBody boolean enable) throws BusinessException {
        return userFacade.updateEnable(id, enable);
    }
}
