package com.controletcc.service;

import com.controletcc.config.security.CustomUserDetails;
import com.controletcc.dto.enums.UserType;
import com.controletcc.error.BusinessException;
import com.controletcc.model.entity.User;
import com.controletcc.repository.RoleRepository;
import com.controletcc.repository.UserRepository;
import com.controletcc.util.StringUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
        var authorities = user.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getId())).collect(Collectors.toList());
        return new CustomUserDetails(user, authorities);
    }

    public User insert(@NonNull User user, @NonNull UserType userType) throws BusinessException {
        user.setId(null);
        switch (userType) {
            case ADMIN -> {
                user.setRoles(roleRepository.getRolesByAdminIsTrue());
                user.setName("Administrador");
            }
            case SUPERVISOR -> user.setRoles(roleRepository.getRolesByAdminIsTrueOrProfessorIsTrue());
            case PROFESSOR -> user.setRoles(roleRepository.getRolesByProfessorIsTrue());
            case ALUNO -> user.setRoles(roleRepository.getRolesByAlunoIsTrue());
        }
        return this.saveUser(user);
    }

    public User update(@NonNull Long idUser, @NonNull User user) throws BusinessException {
        var userEntity = userRepository.getReferenceById(idUser);
        userEntity.setName(user.getName());
        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(user.getPassword());
        userEntity.setEnabled(user.isEnabled());
        return this.saveUser(userEntity);
    }

    private void validate(User user) throws BusinessException {
        var errors = new ArrayList<String>();

        if (StringUtil.isNullOrBlank(user.getUsername())) {
            errors.add("Nome de usuário não informado");
        } else {
            if (user.getId() == null) {
                if (userRepository.existsByUsername(user.getUsername())) {
                    errors.add("Já existe outro usuário com este nome");
                }
            } else if (userRepository.existsByUsernameAndIdNot(user.getUsername(), user.getId())) {
                errors.add("Já existe outro usuário com este nome");
            }
        }

        if (StringUtil.isNullOrBlank(user.getPassword())) {
            errors.add("Senha não informada");
        } else if (!user.getPassword().matches("(?=.*[a-z])(?=.*[0-9]).{8,}")) {
            errors.add("Senha deve conter no mínimo 8 caracteres, com pelo menos uma letra e um número");
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
    }

    private User saveUser(User user) throws BusinessException {
        validate(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


}
