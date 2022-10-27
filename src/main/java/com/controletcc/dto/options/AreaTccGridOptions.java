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
public class AreaTccGridOptions extends BaseGridOptions {

    private Long id;
    private String faculdade;
    private String curso;

}
