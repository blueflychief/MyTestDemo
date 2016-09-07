package com.example.administrator.mytestdemo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 9/7/2016.
 */
public class StringUtils {
    private static final String CRLF_STR = "(\r\n|\r|\n|\n\r)";
    public static int findCtrl(String s) {
        int c = 0;
        Pattern pattern = Pattern.compile(CRLF_STR);
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            c++;
        }
        return c;
    }
}
