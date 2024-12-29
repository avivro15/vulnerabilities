package com.example.vulnerabilities.parser.packegeExtractor;

import java.util.Base64;

public class FileDecodeHelper {

    public static String decodeBase64File(String file) throws IllegalArgumentException {
        return new String(Base64.getDecoder().decode(file));
    }
}
