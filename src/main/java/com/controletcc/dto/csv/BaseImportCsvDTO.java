package com.controletcc.dto.csv;

import com.controletcc.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BaseImportCsvDTO {

    private String error;

    public void addError(String error) {
        if (!StringUtil.isNullOrBlank(this.error)) {
            this.error = this.error.concat('\n' + error);
        } else {
            this.error = error;
        }
    }

    public boolean isValid() {
        return StringUtil.isNullOrBlank(error);
    }

}
