package com.example.bestme.util;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public abstract class StringUtils {

    public static List<String> convertStringToList(String values) {
        if (org.springframework.util.StringUtils.hasText(values)) {
            String decodedValues = URLDecoder.decode(values, StandardCharsets.UTF_8);
            return Arrays.asList(decodedValues.split(","));
        }
        return null;
    }
}
