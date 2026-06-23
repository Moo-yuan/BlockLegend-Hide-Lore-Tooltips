package com.mooyuan.blocklegend.hidelore;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.AutoConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Config(name = "blocklegend_hidelore")
public class ModConfig implements ConfigData {

    @ConfigEntry.Category("lore")
    @ConfigEntry.Gui.Tooltip
    public boolean enabled = true;

    @ConfigEntry.Category("lore")
    @ConfigEntry.Gui.Tooltip
    public boolean hideEnchantments = false;

    @ConfigEntry.Category("lore")
    @ConfigEntry.Gui.Tooltip
    public boolean hideAttributes = false;

    @ConfigEntry.Category("lore")
    @ConfigEntry.Gui.Tooltip
    public boolean hideSkills = true;

    @ConfigEntry.Category("lore")
    @ConfigEntry.Gui.Tooltip
    public boolean hideEmptySlots = false;

    @ConfigEntry.Category("lore")
    @ConfigEntry.Gui.Tooltip
    public boolean hideAllSlots = false;

    @ConfigEntry.Category("lore")
    @ConfigEntry.Gui.Tooltip
    public boolean hideDurability = false;

    @ConfigEntry.Category("lore")
    @ConfigEntry.Gui.Tooltip
    public boolean hideBoundStatus = false;

    @ConfigEntry.Category("lore")
    @ConfigEntry.Gui.Tooltip
    public boolean hideKillCount = false;

    @ConfigEntry.Category("lore")
    @ConfigEntry.Gui.Tooltip
    public boolean hideUnbreakable = false;

    @ConfigEntry.Category("lore")
    @ConfigEntry.Gui.Tooltip
    public boolean unbreakableOverridesDurability = false;

    @ConfigEntry.Category("lore")
    @ConfigEntry.Gui.Tooltip
    public String hintText = "\u00a77[按 {key} 查看 {count} 条隐藏信息]\u00a7r";

    @ConfigEntry.Category("lore")
    @ConfigEntry.Gui.Tooltip
    public String separatorLine = "\u00a77\u00a7m--------------------\u00a7r";

    @ConfigEntry.Category("default")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public KeyOption customKey = KeyOption.CAPS_LOCK;

    @ConfigEntry.Category("default")
    @ConfigEntry.Gui.Tooltip
    public boolean hideArmorTrim = false;

    @ConfigEntry.Category("default")
    @ConfigEntry.Gui.Tooltip
    public boolean hideVanillaToolStats = false;

    @ConfigEntry.Category("default")
    @ConfigEntry.Gui.Tooltip
    public boolean hideVanillaArmorStats = false;

    @ConfigEntry.Category("default")
    @ConfigEntry.Gui.Tooltip
    public boolean protectEnchantedBook = true;

    @ConfigEntry.Category("default")
    @ConfigEntry.Gui.Tooltip
    public boolean protectPaper = true;

    @ConfigEntry.Category("default")
    @ConfigEntry.Gui.Tooltip
    public boolean boldUnbreakable = true;

    // ========== v1.1 新增：物品ID白名单 ==========
    @ConfigEntry.Category("default")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = false)
    public ItemIdProtection itemIdProtection = new ItemIdProtection();

    public static class ItemIdProtection {
        @ConfigEntry.Gui.Tooltip
        public boolean enabled = true;

        @ConfigEntry.Gui.Tooltip
        public List<String> protectedIds = new ArrayList<>(Arrays.asList(
            "minecraft:paper",
            "minecraft:feather"
        ));
    }

    private static ModConfig INSTANCE;

    public static void load() {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
        INSTANCE = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public static ModConfig getInstance() {
        if (INSTANCE == null) {
            load();
        }
        return INSTANCE;
    }

    public static void save() {
        AutoConfig.getConfigHolder(ModConfig.class).save();
    }
}
