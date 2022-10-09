package com.controletcc.controller;

import com.controletcc.error.BusinessException;
import com.controletcc.facade.UserFacade;
import com.controletcc.model.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;

    @PreAuthorize("hasAuthority('usuario.create')")
    @PostMapping("admin")
    public UserDTO insert(@RequestBody UserDTO user) throws BusinessException {
        return userFacade.insertAdmin(user);
    }

    @PreAuthorize("hasAuthority('usuario.create')")
    @PutMapping("{id}")
    public UserDTO update(@PathVariable Long id, @RequestBody UserDTO user) throws BusinessException {
        user.setId(id);
        return userFacade.update(user);
    }

}
