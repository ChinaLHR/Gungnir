package io.github.chinalhr.gungnir.utils;

import java.util.UUID;

/**
 * @Author : ChinaLHR
 * @Date : Create in 16:34 2018/4/27
 * @Email : 13435500980@163.com
 */
public class GeneralUtils {

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
