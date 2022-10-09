package com.controletcc.facade;

import com.controletcc.dto.enums.UserType;
import com.controletcc.error.BusinessException;
import com.controletcc.model.dto.UserDTO;
import com.controletcc.model.entity.User;
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

    public UserDTO insertAdmin(UserDTO userDTO) throws BusinessException {
        var user = ModelMapperUtil.map(userDTO, User.class);
        user = userService.insert(user, UserType.ADMIN);
        return ModelMapperUtil.map(user, UserDTO.class);
    }

    public UserDTO update(UserDTO userDTO) throws BusinessException {
        var user = ModelMapperUtil.map(userDTO, User.class);
        user = userService.update(user.getId(), user);
        return ModelMapperUtil.map(user, UserDTO.class);
    }
}
