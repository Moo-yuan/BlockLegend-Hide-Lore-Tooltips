package com.mooyuan.blocklegend.hidelore;

import java.util.Arrays;
import java.util.List;

public class SkillDatabase {
    public static final List<String> SKILL_TRIGGERS = Arrays.asList(
        "攻击时", "左键", "右键", "Shift+右键", "Shift+左键", "SHIFT", "被动技能", "终结技"
    );
    public static final List<String> COOLDOWN_KEYWORDS = Arrays.asList("冷却", "持续", "间隔", "蓄力", "Timer");
    public static final List<String> PROTECTED_KEYWORDS = Arrays.asList("切换模式", "组合技", "Shift+右键+右键", "双SHIFT");

    public static boolean isSkillTrigger(String stripped) {
        if (stripped == null || stripped.isEmpty()) return false;
        for (String trigger : SKILL_TRIGGERS) {
            if (stripped.contains(trigger)) return true;
        }
        return false;
    }

    public static boolean isCooldownLine(String stripped) {
        if (stripped == null || stripped.isEmpty()) return false;
        for (String cd : COOLDOWN_KEYWORDS) {
            if (stripped.contains(cd)) return true;
        }
        return false;
    }

    public static boolean isProtectedLine(String stripped) {
        if (stripped == null || stripped.isEmpty()) return false;
        for (String protect : PROTECTED_KEYWORDS) {
            if (stripped.contains(protect)) return true;
        }
        return false;
    }
}
