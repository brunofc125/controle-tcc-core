package com.controletcc.model.entity.base;

import com.controletcc.model.enums.Sexo;
import com.controletcc.util.LocalDateUtil;
import com.controletcc.util.StringUtil;
import com.controletcc.util.ValidatorUtil;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@MappedSuperclass
public class Pessoa extends BaseEntity {
    @Column(name = "nome", nullable = false)
    protected String nome;

    @Column(name = "email", nullable = false)
    protected String email;

    @Column(name = "sexo", nullable = false)
    @Enumerated(EnumType.STRING)
    protected Sexo sexo;

    @Column(name = "data_nascimento", nullable = false)
    protected LocalDate dataNascimento;

    public List<String> getPessoaErrors() {
        var errors = new ArrayList<String>();

        if (StringUtil.isNullOrBlank(this.email)) {
            errors.add("E-mail não informado");
        } else if (!ValidatorUtil.isValidEmail(this.email)) {
            errors.add("E-mail inválido");
        }

        if (this.sexo == null) {
            errors.add("Sexo não informado");
        }

        if (this.dataNascimento == null) {
            errors.add("Data de nascimento não informada");
        } else if (LocalDateUtil.compare(this.dataNascimento, LocalDate.now()) > 0) {
            errors.add("Data de nascimento não pode ser uma data futura");
        }

        return errors;
    }

}
