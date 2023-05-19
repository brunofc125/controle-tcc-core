package com.controletcc.model.dto.base;

import com.controletcc.model.enums.Sexo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PessoaDTO extends BaseDTO {
    protected String nome;
    protected String email;
    protected String matricula;
    protected Sexo sexo;
    protected LocalDate dataNascimento;
}
