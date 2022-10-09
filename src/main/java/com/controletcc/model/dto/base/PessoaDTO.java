package com.controletcc.model.dto.base;

import com.controletcc.model.enums.Sexo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PessoaDTO extends BaseDTO {
    private String nome;
    private String cpf;
    private String rg;
    private String email;
    private Sexo sexo;
    private LocalDate dataNascimento;
}
