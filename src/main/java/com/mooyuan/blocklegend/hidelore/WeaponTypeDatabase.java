package com.mooyuan.blocklegend.hidelore;

import java.util.Arrays;
import java.util.List;

public class WeaponTypeDatabase {
    public static final String TYPE_PREFIX = "戞";
    public static final String STAR_ICON = "枩";
    public static final String LEVEL_PREFIX = "Lv.";
    public static final List<String> WEAPON_TYPES = Arrays.asList(
        "剑", "弓", "斧", "法书", "远程", "法器", "塔罗牌", "铳", "钓竿", "盾牌"
    );

    // 武器类型关键词（用于属性区识别）
    public static final List<String> WEAPON_TYPE_KEYWORDS = Arrays.asList(
        "双手武器", "单手武器", "主手武器", "副手武器", "远程武器", "近战武器"
    );

    public static boolean isType2Start(String stripped) {
        if (stripped == null || stripped.isEmpty()) return false;
        boolean hasStar = stripped.contains(STAR_ICON);
        boolean hasLevel = stripped.contains(LEVEL_PREFIX);
        boolean hasType = stripped.contains(TYPE_PREFIX);
        boolean hasBracketType = stripped.contains("[") && stripped.contains("]");
        if (hasStar && hasLevel) return true;
        if (hasLevel && hasType) return true;
        if (hasStar && hasType) return true;
        if (hasType && hasBracketType) return true;
        return false;
    }

    public static boolean isWeaponType(String stripped) {
        if (stripped == null || stripped.isEmpty()) return false;
        for (String type : WEAPON_TYPE_KEYWORDS) {
            if (stripped.contains(type)) return true;
        }
        return false;
    }
}
