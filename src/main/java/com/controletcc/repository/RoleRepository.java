package com.controletcc.repository;

import com.controletcc.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, String> {

    List<Role> getRolesByAdminIsTrue();

    List<Role> getRolesByAdminIsTrueOrProfessorIsTrue();

    List<Role> getRolesByProfessorIsTrue();

    List<Role> getRolesByAlunoIsTrue();
}
