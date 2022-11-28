package com.controletcc.dto.options;

import com.controletcc.dto.options.base.BaseGridOptions;
import com.controletcc.model.enums.TipoTcc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnexoGeralGridOptions extends BaseGridOptions {

    private Long id;
    private String descricao;
    private TipoTcc tipoTcc;
    
}
