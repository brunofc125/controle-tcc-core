package com.controletcc.error;

import com.controletcc.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BusinessException extends Exception {

    private final String title;
    private final List<String> errors;

    public BusinessException(String msg) {
        super(msg);
        this.title = null;
        this.errors = new ArrayList<>();
    }

    public BusinessException(List<String> errors) {
        this.title = null;
        this.errors = errors;
    }

    public BusinessException(String title, List<String> errors) {
        this.title = title;
        this.errors = errors;
    }

    @Override
    public String getMessage() {
        if (this.errors == null || this.errors.isEmpty()) {
            return super.getMessage();
        }
        var errorsBuilder = new StringBuilder();
        if (!StringUtil.isNullOrBlank(this.title)) {
            errorsBuilder.append("<p>");
            errorsBuilder.append(this.title);
            errorsBuilder.append("</p>");
        }
        errorsBuilder.append("<ul>");
        for (var error : this.errors) {
            errorsBuilder.append("<li style=\"text-align: left\">");
            errorsBuilder.append(error);
            errorsBuilder.append("</li>");
        }
        errorsBuilder.append("</ul>");
        return errorsBuilder.toString();
    }
}
