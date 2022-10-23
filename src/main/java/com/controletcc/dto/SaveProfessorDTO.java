package com.controletcc.dto;

import com.controletcc.model.dto.ProfessorDTO;
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
public class SaveProfessorDTO implements Serializable {
    private ProfessorDTO professor;
    private UserDTO user;
}
