package com.controletcc.util;

import org.aspectj.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;

public class FileAppUtil {

    private FileAppUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String fileToBase64(File file, Charset charset) throws IOException {
        var bytes = FileUtil.readAsByteArray(file);
        return byteToBase64(bytes, charset);
    }

    public static String byteToBase64(byte[] bytes, Charset charset) throws IOException {
        var base64 = Base64.getEncoder().encode(bytes);
        return new String(base64, charset);
    }

}