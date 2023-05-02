package com.controletcc.service;

import com.controletcc.config.security.CustomUserDetails;
import com.controletcc.dto.base.ListResponse;
import com.controletcc.dto.enums.OrderByDirection;
import com.controletcc.dto.options.UserGridOptions;
import com.controletcc.error.BusinessException;
import com.controletcc.model.entity.Role;
import com.controletcc.model.entity.User;
import com.controletcc.model.enums.UserType;
import com.controletcc.repository.RoleRepository;
import com.controletcc.repository.UserRepository;
import com.controletcc.repository.projection.UserProjection;
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
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = BusinessException.class)
    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
        var authorities = user.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getId())).collect(Collectors.toList());
        return new CustomUserDetails(user, authorities);
    }

    public void detach(User user) {
        userRepository.detach(user);
    }

    public ListResponse<UserProjection> search(UserGridOptions options) {
        if (StringUtil.isNullOrBlank(options.getOrderByField())) {
            options.setOrderByField("id");
            options.setOrderByDirection(OrderByDirection.ASC);
        }
        var page = userRepository.search(options.getId(), options.getType(), options.getName(), options.getUsername(), options.isEnabled(), options.getPageable());
        return new ListResponse<>(page.getContent(), page.getTotalElements());
    }

    public User getById(@NonNull Long id) throws BusinessException {
        return userRepository.findById(id).orElseThrow(() -> new BusinessException("Usuário não encontrado"));
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public User insert(@NonNull User user, @NonNull UserType userType) throws BusinessException {
        user.setId(null);
        user.setRoles(getRolesByUserType(userType));
        user.setType(userType);
        if (UserType.ADMIN.equals(userType)) {
            user.setName("Administrador");
        }
        return this.saveCryptUser(user);
    }

    public User insert(@NonNull String fullName, @NonNull UserType userType) throws BusinessException {
        var user = new User();
        var password = generatePassword();
        user.setRoles(getRolesByUserType(userType));
        user.setType(userType);
        user.setName(fullName.trim());
        user.setUsername(generateUsername(StringUtil.getUsernameByFullName(fullName)));
        user.setPassword(password);
        user.setEnabled(true);
        if (UserType.ADMIN.equals(userType)) {
            user.setName("Administrador");
        }
        var savedUser = this.saveCryptUser(user);
        return new User(savedUser, password);
    }

    public void updateName(@NonNull Long idUser, @NonNull String name) throws BusinessException {
        var user = getById(idUser);
        user.setName(name);
        userRepository.save(user);
    }

    public void updateRoles(@NonNull Long idUser, @NonNull UserType userType) throws BusinessException {
        var user = getById(idUser);
        user.setType(userType);
        user.setRoles(this.getRolesByUserType(userType));
        userRepository.save(user);
    }

    public User updateEnable(@NonNull Long idUser, boolean enable) throws BusinessException {
        var user = getById(idUser);
        user.setEnabled(enable);
        return userRepository.save(user);
    }

    private List<Role> getRolesByUserType(@NonNull UserType userType) {
        List<Role> roles = new ArrayList<>();
        switch (userType) {
            case ADMIN -> roles = roleRepository.getRolesByAdminIsTrue();
            case SUPERVISOR -> roles = roleRepository.getRolesBySupervisorIsTrue();
            case PROFESSOR -> roles = roleRepository.getRolesByProfessorIsTrue();
            case ALUNO -> roles = roleRepository.getRolesByAlunoIsTrue();
        }
        return roles;
    }

    private void validate(@NonNull User user) throws BusinessException {
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

        if (user.getType() == null) {
            errors.add("Tipo do usuário não informado");
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
    }

    public User saveCryptUser(User user) throws BusinessException {
        validate(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String generateUsername(String simpleName) {
        return userRepository.getNewUsername(simpleName);
    }

    public String generatePassword() {
        var passBuilder = new StringBuilder();
        var rand = new Random();
        var containsLetter = false;
        var containsNumber = false;
        for (int i = 0; i < 7; i++) {
            var addLetter = rand.nextBoolean();
            if (addLetter) {
                containsLetter = true;
                passBuilder.append((char) rand.nextInt(97, 123));
            } else {
                containsNumber = true;
                passBuilder.append((char) rand.nextInt(48, 58));
            }
        }
        if (containsLetter && containsNumber) {
            var addLetter = rand.nextBoolean();
            if (addLetter) {
                passBuilder.append((char) rand.nextInt(97, 123));
            } else {
                passBuilder.append((char) rand.nextInt(48, 58));
            }
        } else if (containsLetter) {
            passBuilder.append((char) rand.nextInt(48, 58));
        } else {
            passBuilder.append((char) rand.nextInt(97, 123));
        }
        return passBuilder.toString();
    }


}
