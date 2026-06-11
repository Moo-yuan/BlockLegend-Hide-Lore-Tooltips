package com.mooyuan.blocklegend.hidelore;

import java.util.Arrays;
import java.util.List;

public class ElementDatabase {
    public static final List<String> ELEMENT_PREFIXES = Arrays.asList("鐮", "鐨", "噕", "鍏", "☽", "曞");
    public static final List<String> ELEMENT_TYPES = Arrays.asList("冰", "电", "水", "风", "暗黑", "火");
    public static final List<String> ELEMENT_KEYWORDS = Arrays.asList("元素伤害", "元素防御");

    public static boolean isElementLine(String stripped) {
        if (stripped == null || stripped.isEmpty()) return false;
        boolean hasPrefix = false;
        for (String prefix : ELEMENT_PREFIXES) {
            if (stripped.contains(prefix)) { hasPrefix = true; break; }
        }
        if (!hasPrefix) return false;
        for (String type : ELEMENT_TYPES) {
            if (stripped.contains(type)) {
                for (String keyword : ELEMENT_KEYWORDS) {
                    if (stripped.contains(keyword)) return true;
                }
            }
        }
        return false;
    }
}
