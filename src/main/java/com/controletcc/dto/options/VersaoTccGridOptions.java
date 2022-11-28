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
public class VersaoTccGridOptions extends BaseGridOptions {

    private Long versao;
    private boolean ultimaVersao;

}
