package com.controletcc.dto;

import com.controletcc.model.dto.AlunoDTO;
import com.controletcc.model.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SaveAlunoDTO implements Serializable {
    private AlunoDTO aluno;
    private UserDTO user;
}
