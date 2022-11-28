package com.controletcc.util;

import org.aspectj.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class FileAppUtil {

    private FileAppUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String fileToBase64(File file) throws IOException {
        var bytes = FileUtil.readAsByteArray(file);
        return byteToBase64(bytes);
    }

    public static String byteToBase64(byte[] bytes) throws IOException {
        var base64 = Base64.getEncoder().encode(bytes);
        return new String(base64, StandardCharsets.UTF_8);
    }

    public static byte[] base64toByte(String base64) throws IOException {
        return Base64.getDecoder().decode(base64);
    }

    public static String getExtension(String fileName) {
        return !StringUtil.isNullOrBlank(fileName) ? fileName.substring(fileName.lastIndexOf('.') + 1) : "";
    }

}
