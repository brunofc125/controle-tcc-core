package com.controletcc.util;

import java.util.regex.Pattern;

public class ValidatorUtil {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", Pattern.CASE_INSENSITIVE);

    private ValidatorUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isValidCPF(String cpf) {
        if (StringUtil.isNullOrBlank(cpf) || cpf.length() != 11 || cpf.matches("00000000000|11111111111|22222222222|33333333333|44444444444|55555555555|66666666666|77777777777|88888888888|99999999999")) {
            return false;
        }

        int acumulador1 = 0;
        int acumulador2 = 0;
        int resto = 0;

        StringBuilder resultado = new StringBuilder();

        String digitoVerificadorCpf = cpf.substring((cpf.length() - 2), cpf.length());
        String digito;

        for (int i = 1; i < (cpf.length() - 1); i++) {
            digito = cpf.substring((i - 1), i);
            acumulador1 += (11 - i) * Integer.parseInt(digito);
        }

        resto = acumulador1 % 11;

        int dv1 = 0;
        int dv2 = 0;
        if (resto >= 2) {
            dv1 = (11 - resto);
        }

        acumulador2 = acumulador1 + (2 * dv1);
        for (int i = 1; i < (cpf.length() - 1); i++) {
            digito = cpf.substring((i - 1), i);
            acumulador2 += Integer.parseInt(digito);
        }

        resto = acumulador2 % 11;
        if (resto >= 2) {
            dv2 = (11 - resto);
        }

        resultado.append(dv1);
        resultado.append(dv2);

        return resultado.toString().equals(digitoVerificadorCpf);
    }

    public static boolean isValidCNPJ(String cnpj) {
        if (StringUtil.isNullOrBlank(cnpj) || cnpj.length() != 14 || cnpj.matches("00000000000000|11111111111111|22222222222222|33333333333333|44444444444444|55555555555555|66666666666666|77777777777777|88888888888888|99999999999999")) {
            return false;
        }

        int acumulador = 0;
        int digito = 0;
        StringBuilder cnpjStringBuffer = new StringBuilder();
        char[] cnpjCharArray = cnpj.toCharArray();

        cnpjStringBuffer.append(cnpj.substring(0, 12));

        for (int i = 0; i < 4; i++) {
            if (((cnpjCharArray[i] - 48) >= 0) && ((cnpjCharArray[i] - 48) <= 9)) {
                acumulador += (cnpjCharArray[i] - 48) * (6 - (i + 1));
            }
        }

        for (int i = 0; i < 8; i++) {
            if (((cnpjCharArray[i + 4] - 48) >= 0) && ((cnpjCharArray[i + 4] - 48) <= 9)) {
                acumulador += (cnpjCharArray[i + 4] - 48) * (10 - (i + 1));
            }
        }

        digito = 11 - (acumulador % 11);

        cnpjStringBuffer.append((digito == 10 || digito == 11) ? "0" : digito);

        acumulador = 0;

        for (int i = 0; i < 5; i++) {
            if (((cnpjCharArray[i] - 48) >= 0) && ((cnpjCharArray[i] - 48) <= 9)) {
                acumulador += (cnpjCharArray[i] - 48) * (7 - (i + 1));
            }
        }

        for (int i = 0; i < 8; i++) {
            if (((cnpjCharArray[i + 5] - 48) >= 0) && ((cnpjCharArray[i + 5] - 48) <= 9)) {
                acumulador += (cnpjCharArray[i + 5] - 48) * (10 - (i + 1));
            }
        }

        digito = 11 - (acumulador % 11);

        cnpjStringBuffer.append((digito == 10 || digito == 11) ? "0" : digito);

        return cnpjStringBuffer.toString().equals(cnpj);
    }


    public static boolean isValidEmail(String field) {
        if (StringUtil.isNullOrBlank(field)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(field).matches();
    }

}
