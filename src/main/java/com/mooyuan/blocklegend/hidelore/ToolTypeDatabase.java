package com.mooyuan.blocklegend.hidelore;

import java.util.Arrays;
import java.util.List;

public class ToolTypeDatabase {
    public static final List<String> TOOL_IDS = Arrays.asList(
        "netherite_pickaxe", "netherite_axe", "netherite_shovel", "fishing_rod", "shield"
    );
    public static final List<String> NO_TYPE2_TOOLS = Arrays.asList(
        "netherite_pickaxe", "netherite_shovel"
    );
    public static final List<String> HAS_TYPE2_TOOLS = Arrays.asList(
        "netherite_axe", "fishing_rod", "shield"
    );

    public static boolean isTool(String itemId) {
        if (itemId == null || itemId.isEmpty()) return false;
        for (String id : TOOL_IDS) {
            if (itemId.contains(id)) return true;
        }
        return false;
    }

    public static boolean hasType2(String itemId) {
        if (itemId == null || itemId.isEmpty()) return false;
        for (String id : HAS_TYPE2_TOOLS) {
            if (itemId.contains(id)) return true;
        }
        return false;
    }
}
