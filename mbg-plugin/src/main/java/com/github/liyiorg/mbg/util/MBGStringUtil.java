package com.github.liyiorg.mbg.util;

/**
 * @author LiYi
 */
public abstract class MBGStringUtil {

    /**
     * 获取类simple name
     *
     * @param fullClassName
     * @return String
     */
    public static String shortClassName(String fullClassName) {
        if (fullClassName != null) {
            return fullClassName.replaceAll("(.*\\.)+(.*)", "$2");
        }
        return fullClassName;
    }
}
