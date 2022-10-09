package com.controletcc.error;

import java.util.ArrayList;
import java.util.List;

public class BusinessException extends Exception {

    private final List<String> errors;

    public BusinessException(String msg) {
        super(msg);
        this.errors = new ArrayList<>();
    }

    public BusinessException(List<String> errors) {
        this.errors = errors;
    }

    @Override
    public String getMessage() {
        if (this.errors == null || this.errors.isEmpty()) {
            return super.getMessage();
        }
        var errorsBuilder = new StringBuilder();
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
