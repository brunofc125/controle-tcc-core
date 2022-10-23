package com.controletcc.dto.options;

import com.controletcc.dto.options.base.BaseGridOptions;
import com.controletcc.model.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserGridOptions extends BaseGridOptions {

    public static final String ENABLED = "enabled";
    public static final String DISABLED = "disabled";

    private Long id;
    private UserType type;
    private String name;
    private String username;
    private String enabled;

    public Boolean isEnabled() {
        return this.enabled != null ? ENABLED.equalsIgnoreCase(this.enabled) : null;
    }


}
