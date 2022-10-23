package com.controletcc.model.entity.base;

import com.controletcc.model.enums.Sexo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;

@Getter
@Setter
@MappedSuperclass
public class Pessoa extends BaseEntity {
    @Column(name = "nome", nullable = false)
    protected String nome;

    @Column(name = "cpf", nullable = false, unique = true)
    protected String cpf;

    @Column(name = "rg")
    protected String rg;

    @Column(name = "email", nullable = false)
    protected String email;

    @Column(name = "sexo", nullable = false)
    @Enumerated(EnumType.STRING)
    protected Sexo sexo;

    @Column(name = "data_nascimento", nullable = false)
    protected LocalDate dataNascimento;

}
