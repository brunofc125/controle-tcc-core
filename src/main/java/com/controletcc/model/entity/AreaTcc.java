package com.controletcc.model.entity;

import com.controletcc.model.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "area_tcc", uniqueConstraints = {@UniqueConstraint(columnNames = {"faculdade", "curso"})})
public class AreaTcc extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "faculdade", nullable = false)
    private String faculdade;

    @Column(name = "curso", nullable = false)
    private String curso;

    public String getDescricao() {
        return faculdade + " - " + curso;
    }
}
