package com.controletcc.util;

import com.controletcc.error.BusinessException;
import org.slf4j.Logger;

import java.util.UUID;

public class ErrorUtil {
    private ErrorUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static void error(Logger log, Exception ex, String msgError) throws BusinessException {
        var tag = UUID.randomUUID();
        msgError = msgError.concat(" Código do erro: " + tag.toString());
        log.error(msgError, ex);
        throw new BusinessException(msgError);
    }
}
