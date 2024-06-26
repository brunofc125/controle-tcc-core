package com.controletcc.model.entity;

import com.controletcc.model.entity.base.BaseEntity;
import com.controletcc.model.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"user\"")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType type;

    @Column(name = "name")
    private String name;

    @Column(name = "user_name", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "enabled")
    private boolean enabled;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "id_user"), inverseJoinColumns = @JoinColumn(name = "id_role"))
    private List<Role> roles = new ArrayList<>();

    public User(User user, String password) {
        this.id = user.getId();
        this.type = user.getType();
        this.name = user.getName();
        this.username = user.getUsername();
        this.enabled = user.isEnabled();
        this.roles = user.getRoles();
        this.password = password;
    }

}
