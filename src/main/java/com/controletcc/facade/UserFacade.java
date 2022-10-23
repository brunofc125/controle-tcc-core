package com.controletcc.facade;

import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.options.UserGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.dto.UserDTO;
import com.controletcc.model.entity.User;
import com.controletcc.model.enums.UserType;
import com.controletcc.repository.projection.UserProjection;
import com.controletcc.service.UserService;
import com.controletcc.util.ModelMapperUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = BusinessException.class)
@Slf4j
public class UserFacade {
    private final UserService userService;

    public UserDTO getById(Long id) throws BusinessException {
        var user = userService.getById(id);
        var userDTO = ModelMapperUtil.map(user, UserDTO.class);
        userDTO.setPassword(null);
        return userDTO;
    }

    public ListResponse<UserProjection> search(UserGridOptions options) {
        return userService.search(options);
    }

    public UserDTO insertAdmin(UserDTO userDTO) throws BusinessException {
        var user = ModelMapperUtil.map(userDTO, User.class);
        user = userService.insert(user, UserType.ADMIN);
        return ModelMapperUtil.map(user, UserDTO.class);
    }

    public UserDTO update(@NonNull Long idUser, @NonNull UserDTO userDTO) throws BusinessException {
        var user = userService.getById(idUser);
        userService.detach(user);
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEnabled(userDTO.isEnabled());
        user = userService.saveCryptUser(user);
        userDTO = ModelMapperUtil.map(user, UserDTO.class);
        userDTO.setPassword(null);
        return userDTO;
    }

    public UserDTO updateEnable(@NonNull Long idUser, boolean enable) throws BusinessException {
        return ModelMapperUtil.map(userService.updateEnable(idUser, enable), UserDTO.class);
    }
}
