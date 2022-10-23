package com.controletcc.repository;

import com.controletcc.model.entity.User;
import com.controletcc.model.enums.UserType;
import com.controletcc.repository.custom.RepositoryCustom;
import com.controletcc.repository.projection.UserProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long>, RepositoryCustom<User> {

    @Query(value = """  
            SELECT
                u.id as id,
                u.type as type,
                u.name as name,
                u.username as username,
                u.enabled as enabled
            FROM User u
                where (:id is null or u.id = :id)
                and (:type is null or u.type = :type)
                and (:name is null or lower(u.name) like concat('%', trim(lower(:name)),'%') )
                and (:username is null or lower(u.username) like concat('%', trim(lower(:username)),'%') )
                and (:enabled is null or u.enabled = :enabled)"""

    )
    Page<UserProjection> search(Long id,
                                UserType type,
                                String name,
                                String username,
                                Boolean enabled,
                                Pageable pageable);

    User findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByUsernameAndIdNot(String username, Long id);
}
