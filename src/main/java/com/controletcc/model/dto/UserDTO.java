package com.controletcc.model.dto;

import com.controletcc.dto.csv.AlunoImportCsvDTO;
import com.controletcc.dto.csv.ProfessorImportCsvDTO;
import com.controletcc.model.dto.base.BaseDTO;
import com.controletcc.model.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO extends BaseDTO {
    private Long id;
    private UserType type;
    private String name;
    private String username;
    private String password;
    private boolean enabled;

    public UserDTO(ProfessorImportCsvDTO csv) {
        this.type = csv.isSupervisorTcc() ? UserType.SUPERVISOR : UserType.PROFESSOR;
        this.name = csv.getNome();
        this.username = csv.getUsername();
        this.password = csv.getPassword();
        this.enabled = true;
    }

    public UserDTO(AlunoImportCsvDTO csv) {
        this.type = UserType.ALUNO;
        this.name = csv.getNome();
        this.username = csv.getUsername();
        this.password = csv.getPassword();
        this.enabled = true;
    }
}
