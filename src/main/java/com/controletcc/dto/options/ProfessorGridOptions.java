package com.controletcc.dto.options;

import com.controletcc.dto.options.base.BaseGridOptions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfessorGridOptions extends BaseGridOptions {

    public static final String SUPERVISOR = "supervisor";
    public static final String NAO_SUPERVISOR = "naoSupervisor";

    private Long id;
    private String nome;
    private String email;
    private String matricula;
    private String categoria;

    public Boolean isCategoriaSupervisor() {
        return this.categoria != null ? SUPERVISOR.equalsIgnoreCase(this.categoria) : null;
    }


}
