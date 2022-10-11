package com.controletcc.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    private String id;

    @Column(name = "admin")
    private boolean admin;

    @Column(name = "supervisor")
    private boolean supervisor;

    @Column(name = "professor")
    private boolean professor;

    @Column(name = "aluno")
    private boolean aluno;
}
