package com.controletcc.facade;

import com.controletcc.error.BusinessException;
import com.controletcc.model.dto.UserDTO;
import com.controletcc.model.entity.User;
import com.controletcc.model.enums.UserType;
import com.controletcc.service.UserService;
import com.controletcc.util.ModelMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class UserFacade {
    private final UserService userService;

    public UserDTO getById(Long id) {
        var user = userService.getById(id);
        var userDTO = ModelMapperUtil.map(user, UserDTO.class);
        userDTO.setPassword(null);
        return userDTO;
    }

    public UserDTO insertAdmin(UserDTO userDTO) throws BusinessException {
        var user = ModelMapperUtil.map(userDTO, User.class);
        user = userService.insert(user, UserType.ADMIN);
        return ModelMapperUtil.map(user, UserDTO.class);
    }

    public UserDTO update(UserDTO userDTO) throws BusinessException {
        var user = ModelMapperUtil.map(userDTO, User.class);
        user = userService.update(user.getId(), user);
        userDTO = ModelMapperUtil.map(user, UserDTO.class);
        userDTO.setPassword(null);
        return userDTO;
    }
}
