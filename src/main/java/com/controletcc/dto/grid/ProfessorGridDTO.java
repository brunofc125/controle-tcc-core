package com.controletcc.dto.grid;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfessorGridDTO {
    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private boolean supervisorTcc;
}
