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
public class AlunoGridOptions extends BaseGridOptions {

    private Long id;
    private String nome;
    private String email;
    private String matricula;
    private Long idAreaTcc;


}
