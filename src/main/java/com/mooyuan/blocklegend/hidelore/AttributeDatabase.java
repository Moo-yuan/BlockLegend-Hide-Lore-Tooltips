package com.mooyuan.blocklegend.hidelore;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class AttributeDatabase {
    public static final List<String> ATTRIBUTE_KEYWORDS = Arrays.asList(
        "攻击伤害", "攻击速度", "暴击几率", "暴击伤害", "技能伤害", "技能冷却",
        "法力值", "技能暴击率", "技能暴击伤害", "远程伤害", "招架几率",
        "击退抗性", "闪避几率", "生命值", "基础防御", "命定指数"
    );
    public static final List<String> RPG_PREFIXES = Arrays.asList("鄿", "嬫", "竴", "嫨", "鐖", "鍏", "曞");

    // 支持范围值格式：+35═60 或 +10═20%
    private static final Pattern RANGE_VALUE_PATTERN = Pattern.compile("[+-]?\\d+\\.?\\d*═\\d+\\.?\\d*%?");
    // 支持精确值格式：125.40 或 +59.48% 或 1
    private static final Pattern EXACT_VALUE_PATTERN = Pattern.compile("[+-]?\\d+\\.?\\d*%?");

    public static boolean isAttributeLine(String stripped) {
        if (stripped == null || stripped.isEmpty()) return false;

        // 检查是否有RPG前缀
        boolean hasRpgPrefix = false;
        for (String prefix : RPG_PREFIXES) {
            if (stripped.contains(prefix)) { hasRpgPrefix = true; break; }
        }

        // 检查是否包含属性关键词
        boolean hasAttributeKeyword = false;
        for (String attr : ATTRIBUTE_KEYWORDS) {
            if (stripped.contains(attr)) { hasAttributeKeyword = true; break; }
        }

        // 检查是否包含数值（范围值或精确值）
        boolean hasValue = RANGE_VALUE_PATTERN.matcher(stripped).find() || 
                          EXACT_VALUE_PATTERN.matcher(stripped).find();

        // 属性行必须同时满足：有RPG前缀 + 有属性关键词 + 有数值
        if (hasRpgPrefix && hasAttributeKeyword && hasValue) return true;

        // 如果没有RPG前缀，但包含属性关键词和数值，也认为是属性（兼容某些格式）
        if (hasAttributeKeyword && hasValue) {
            // 额外检查：不包含其他区域的关键词
            if (!WeaponTypeDatabase.isWeaponType(stripped) &&
                !SkillDatabase.isSkillTrigger(stripped) &&
                !ElementDatabase.isElementLine(stripped)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isRangeValue(String stripped) {
        if (stripped == null || stripped.isEmpty()) return false;
        return RANGE_VALUE_PATTERN.matcher(stripped).find();
    }

    public static boolean isExactValue(String stripped) {
        if (stripped == null || stripped.isEmpty()) return false;
        return EXACT_VALUE_PATTERN.matcher(stripped).find();
    }
}
