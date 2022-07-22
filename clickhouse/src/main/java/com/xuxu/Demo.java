package com.xuxu;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

public class Demo {
    public static void main(String[] args) {
        String str = "aaa.bbb";
        String[] split = str.split("\\.");
        for (String s : split) {
            System.out.println(s);
        }

        LocalDateTime now = LocalDateTime.now();

        long timestamp = 1657681307280L;


        String s = timestampToString(timestamp);
        System.out.println(s);
    }

    private static String timestampToString(Long timestamp){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return simpleDateFormat.format(new Date(timestamp));
    }
}
