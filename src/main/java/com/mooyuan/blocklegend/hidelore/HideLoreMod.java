package com.mooyuan.blocklegend.hidelore;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HideLoreMod implements ClientModInitializer {
    public static final String MOD_ID = "blocklegend_hidelore";
    public static final String MOD_NAME = "BlockLegend Hide Lore Tooltips";
    public static final String AUTHOR = "Moyuans";
    public static final String GITHUB_URL = "https://github.com/Moo-yuan/BlockLegend-Hide-Lore-Tooltips";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // ========== v1.1 新增：白名单关键词保护（移除"点击"和"切换"）==========
    private static final List<String> PROTECTED_KEYWORDS = Arrays.asList(
            "购买", "领取", "预览", "评分", "收藏", "升级至", "右键查看"
    );

    private static final List<String> ARMOR_TRIM_START = Arrays.asList("已有升级", "Upgrade");
    private static final List<String> TOOL_STATS_START = Arrays.asList("在主手时", "When in main hand");
    private static final List<String> ARMOR_STATS_START = Arrays.asList(
            "戴在头上时", "穿在身上时", "穿在腿上时", "穿在脚上时",
            "When on head", "When on body", "When on legs", "When on feet"
    );
    private static final List<String> ARMOR_EXTRA_STATS = Arrays.asList("盔甲韧性", "击退抗性");

    @Override
    public void onInitializeClient() {
        LOGGER.info("[{}] BlockLegend服务器自定义隐藏信息模组加载中...", MOD_NAME);
        LOGGER.info("[{}] 作者: {} | GitHub: {}", MOD_NAME, AUTHOR, GITHUB_URL);

        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            if (stack == null || stack.isEmpty()) return;

            ModConfig config = ModConfig.getInstance();
            if (!config.enabled) return;

            // ========== v1.1 新增：关键词白名单保护 ==========
            if (containsProtectedKeyword(lines)) {
                return;
            }

            // ========== v1.1 新增：物品ID白名单保护 ==========
            if (config.itemIdProtection.enabled && isProtectedItemId(stack, config)) {
                return;
            }

            if (stack.getItem() == Items.ENCHANTED_BOOK) {
                if (config.protectEnchantedBook) return;
                else removeEnchantedBookEnchantments(lines);
            }

            removeVanillaAttributes(lines, config);

            boolean keyPressed = isCustomKeyPressed();
            Text nameLine = lines.isEmpty() ? null : lines.get(0);
            boolean isPaper = stack.getItem() == Items.PAPER;
            boolean isFeather = stack.getItem() == Items.FEATHER;
            boolean isStick = stack.getItem() == Items.STICK;

            String itemId = getItemId(stack);
            boolean isTool = ToolTypeDatabase.isTool(itemId);
            boolean toolHasType2 = ToolTypeDatabase.hasType2(itemId);

            List<LineInfo> lineInfos = new ArrayList<>();
            for (int i = 0; i < lines.size(); i++) {
                Text line = lines.get(i);
                String lineStr = line.getString();
                String stripped = stripFormatting(lineStr);
                lineInfos.add(new LineInfo(line, lineStr, stripped, i, i == 0));
            }

            // ========== 分区识别（按优先级顺序）=========
            markBottomStrict(lineInfos);
            markType2Strict(lineInfos, isTool, toolHasType2);
            markEnchantmentsByPosition(lineInfos);
            markAttributesStrict(lineInfos);
            markElementsStrict(lineInfos);
            markSkillsStrict(lineInfos);
            markRuneGemsStrict(lineInfos);
            markLoreStrict(lineInfos);

            // ========== 计算符文/宝石统计 ==========
            int totalRuneGemSlots = 0;
            int hiddenRuneGemSlots = 0;
            for (LineInfo info : lineInfos) {
                if (info.inRuneGemRegion && !info.stripped.isEmpty() && !isOnlyIcon(info.stripped)) {
                    totalRuneGemSlots++;
                    if (config.hideAllSlots || (config.hideEmptySlots && info.isEmptySlot)) {
                        hiddenRuneGemSlots++;
                    }
                }
            }
            boolean allRuneGemsHidden = totalRuneGemSlots > 0 && hiddenRuneGemSlots == totalRuneGemSlots;
            boolean anyRuneGemsHidden = hiddenRuneGemSlots > 0 && !allRuneGemsHidden;

            // ========== 应用隐藏规则 ==========
            for (LineInfo info : lineInfos) {
                if (info.isNameLine) continue;
                boolean shouldHide = false;

                if (info.inEnchantRegion && config.hideEnchantments) shouldHide = true;
                if (info.inAttributeRegion && config.hideAttributes) shouldHide = true;
                if (info.inSkillRegion && config.hideSkills) shouldHide = true;

                if (info.inRuneGemRegion) {
                    if (config.hideAllSlots) shouldHide = true;
                    else if (config.hideEmptySlots && info.isEmptySlot) shouldHide = true;
                }

                if (info.inBottomRegion) {
                    if (info.stripped.contains("耐久度") && config.hideDurability) shouldHide = true;
                    if (info.stripped.contains("已绑定") && config.hideBoundStatus) shouldHide = true;
                    if (info.stripped.contains("击杀人数") && config.hideKillCount) shouldHide = true;
                    if (info.stripped.contains("无法破坏") && config.hideUnbreakable) shouldHide = true;
                }

                info.shouldHide = shouldHide;
            }

            if (config.unbreakableOverridesDurability && hasUnbreakable(stack)) {
                for (LineInfo info : lineInfos) {
                    if (info.inBottomRegion && info.stripped.contains("耐久度")) info.shouldHide = true;
                }
            }

            if ((isPaper || isFeather) && config.protectPaper) {
                for (LineInfo info : lineInfos) {
                    if (!info.stripped.contains("已绑定")) {
                        info.shouldHide = false;
                    }
                }
            }

            if (isStick) {
                for (LineInfo info : lineInfos) {
                    info.shouldHide = false;
                }
            }

            if (config.boldUnbreakable) {
                for (LineInfo info : lineInfos) {
                    if (info.stripped.contains("无法破坏")) {
                        info.line = Text.literal("\u00a7l" + info.line.getString() + "\u00a7r");
                    }
                }
            }

            buildOutput(lines, lineInfos, config, keyPressed, nameLine, allRuneGemsHidden, anyRuneGemsHidden);
        });

        ModConfig.load();
        LOGGER.info("[{}] 模组加载完成！", MOD_NAME);
    }

    // ========== v1.1 新增：白名单关键词检测 ==========
    private boolean containsProtectedKeyword(List<Text> lines) {
        for (Text line : lines) {
            String stripped = stripFormatting(line.getString());
            for (String keyword : PROTECTED_KEYWORDS) {
                if (stripped.contains(keyword)) {
                    return true;
                }
            }
        }
        return false;
    }

    // ========== v1.1 新增：物品ID白名单检测 ==========
    private boolean isProtectedItemId(ItemStack stack, ModConfig config) {
        String itemId = getItemId(stack);
        if (itemId == null || itemId.isEmpty()) return false;

        for (String protectedId : config.itemIdProtection.protectedIds) {
            if (protectedId == null || protectedId.isEmpty()) continue;
            if (itemId.equals(protectedId) || itemId.contains(protectedId)) {
                return true;
            }
        }
        return false;
    }

    private String getItemId(ItemStack stack) {
        Identifier id = net.minecraft.registry.Registries.ITEM.getId(stack.getItem());
        return id != null ? id.toString() : "";
    }

    private boolean isCustomKeyPressed() {
        try {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client != null && client.getWindow() != null) {
                ModConfig config = ModConfig.getInstance();
                int keyCode = config.customKey.getKeyCode();
                return GLFW.glfwGetKey(client.getWindow().getHandle(), keyCode) == GLFW.GLFW_PRESS;
            }
        } catch (Exception e) {}
        return false;
    }

    private boolean hasUnbreakable(ItemStack stack) {
        return stack.getNbt() != null && stack.getNbt().getBoolean("Unbreakable");
    }

    private void removeVanillaAttributes(List<Text> lines, ModConfig config) {
        List<Integer> toRemove = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String stripped = stripFormatting(lines.get(i).getString());

            if (config.hideArmorTrim) {
                for (String start : ARMOR_TRIM_START) {
                    if (stripped.contains(start)) {
                        toRemove.add(i);
                        if (i + 1 < lines.size()) toRemove.add(i + 1);
                        if (i + 2 < lines.size()) toRemove.add(i + 2);
                        break;
                    }
                }
            }

            if (config.hideVanillaToolStats) {
                for (String start : TOOL_STATS_START) {
                    if (stripped.contains(start)) {
                        toRemove.add(i);
                        if (i + 1 < lines.size()) toRemove.add(i + 1);
                        if (i + 2 < lines.size()) toRemove.add(i + 2);
                        break;
                    }
                }
            }

            if (config.hideVanillaArmorStats) {
                for (String start : ARMOR_STATS_START) {
                    if (stripped.contains(start)) {
                        toRemove.add(i);
                        if (i + 1 < lines.size()) toRemove.add(i + 1);
                        break;
                    }
                }
                for (String stat : ARMOR_EXTRA_STATS) {
                    if (stripped.contains(stat)) {
                        toRemove.add(i);
                        break;
                    }
                }
            }
        }

        toRemove.sort((a, b) -> b - a);
        for (int idx : toRemove) {
            if (idx < lines.size()) lines.remove(idx);
        }
    }

    private void removeEnchantedBookEnchantments(List<Text> lines) {
        List<Integer> toRemove = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String stripped = stripFormatting(lines.get(i).getString());
            if (EnchantmentDatabase.isAnyEnchantment(stripped)) {
                toRemove.add(i);
            }
        }
        toRemove.sort((a, b) -> b - a);
        for (int idx : toRemove) {
            if (idx < lines.size()) lines.remove(idx);
        }
    }

    private void markEnchantmentsByPosition(List<LineInfo> lines) {
        int type2Start = -1;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).inType2Region) {
                type2Start = i;
                break;
            }
        }

        if (type2Start == -1) {
            for (int i = 0; i < lines.size(); i++) {
                LineInfo info = lines.get(i);
                if (info.isNameLine) continue;
                if (info.inBottomRegion) continue;
                if (AttributeDatabase.isAttributeLine(info.stripped) ||
                        ElementDatabase.isElementLine(info.stripped) ||
                        SkillDatabase.isSkillTrigger(info.stripped) ||
                        isRuneGemLine(info.stripped) ||
                        WeaponTypeDatabase.isWeaponType(info.stripped)) {
                    type2Start = i;
                    break;
                }
            }
        }

        for (int i = 1; i < lines.size(); i++) {
            if (type2Start > 0 && i >= type2Start) break;

            LineInfo info = lines.get(i);
            if (info.isNameLine) continue;
            if (info.inBottomRegion) continue;
            if (info.inType2Region) break;

            if (i > 1 && !info.stripped.isEmpty() && !isOnlyIcon(info.stripped)) {
                if (AttributeDatabase.isAttributeLine(info.stripped) ||
                        ElementDatabase.isElementLine(info.stripped) ||
                        SkillDatabase.isSkillTrigger(info.stripped) ||
                        isRuneGemLine(info.stripped) ||
                        WeaponTypeDatabase.isWeaponType(info.stripped)) {
                    break;
                }
            }

            if (EnchantmentDatabase.isAnyEnchantment(info.stripped)) {
                info.isEnchantment = true;
            }

            info.inEnchantRegion = true;
        }
    }

    private void markBottomStrict(List<LineInfo> lines) {
        for (int i = lines.size() - 1; i >= 0; i--) {
            LineInfo info = lines.get(i);
            String stripped = info.stripped;

            if (stripped.contains("耐久度") || stripped.contains("已绑定") ||
                    stripped.contains("击杀人数") || stripped.contains("无法破坏")) {
                info.inBottomRegion = true;
            } else if (!stripped.isEmpty() && !isOnlyIcon(stripped)) {
                break;
            }
        }
    }

    private void markType2Strict(List<LineInfo> lines, boolean isTool, boolean toolHasType2) {
        if (isTool && !toolHasType2) return;

        for (int i = 0; i < lines.size(); i++) {
            LineInfo info = lines.get(i);
            if (info.isNameLine || info.inBottomRegion) continue;

            if (WeaponTypeDatabase.isType2Start(info.stripped)) {
                info.inType2Region = true;
                if (i + 1 < lines.size()) {
                    LineInfo next = lines.get(i + 1);
                    if (!next.isNameLine && !next.inBottomRegion) {
                        next.inType2Region = true;
                    }
                }
                if (i + 2 < lines.size()) {
                    LineInfo blank = lines.get(i + 2);
                    if ((blank.stripped.isEmpty() || isOnlyIcon(blank.stripped)) && !blank.inBottomRegion) {
                        blank.inType2Region = true;
                    }
                }
                break;
            }
        }
    }

    private void markAttributesStrict(List<LineInfo> lines) {
        boolean inRegion = false;
        int type2End = -1;

        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).inType2Region) type2End = i;
        }

        for (int i = 0; i < lines.size(); i++) {
            LineInfo info = lines.get(i);
            String stripped = info.stripped;

            if (info.isNameLine || info.inEnchantRegion || info.inType2Region || info.inBottomRegion) continue;
            if (i <= type2End && type2End >= 0) continue;

            if (!inRegion && AttributeDatabase.isAttributeLine(stripped)) {
                inRegion = true;
                info.inAttributeRegion = true;
            } else if (inRegion) {
                if (AttributeDatabase.isAttributeLine(stripped)) {
                    info.inAttributeRegion = true;
                } else if (WeaponTypeDatabase.isWeaponType(stripped)) {
                    info.inAttributeRegion = true;
                } else if (stripped.isEmpty() || isOnlyIcon(stripped)) {
                    info.inAttributeRegion = true;
                } else {
                    inRegion = false;
                }
            }
        }
    }

    private void markElementsStrict(List<LineInfo> lines) {
        for (int i = 0; i < lines.size(); i++) {
            LineInfo info = lines.get(i);
            if (info.isAlreadyMarked()) continue;

            if (ElementDatabase.isElementLine(info.stripped)) {
                info.inElementRegion = true;
                if (i + 1 < lines.size()) {
                    LineInfo next = lines.get(i + 1);
                    if (!next.isAlreadyMarked() &&
                            (next.stripped.isEmpty() || isOnlyIcon(next.stripped))) {
                        next.inElementRegion = true;
                    }
                }
            }
        }
    }

    private void markSkillsStrict(List<LineInfo> lines) {
        boolean inRegion = false;
        for (int i = 0; i < lines.size(); i++) {
            LineInfo info = lines.get(i);
            String stripped = info.stripped;

            if (info.isAlreadyMarked()) continue;
            if (SkillDatabase.isProtectedLine(stripped)) continue;

            if (SkillDatabase.isSkillTrigger(stripped)) {
                inRegion = true;
                info.inSkillRegion = true;
            } else if (inRegion) {
                if (SkillDatabase.isCooldownLine(stripped)) {
                    info.inSkillRegion = true;
                } else if (stripped.isEmpty() || isOnlyIcon(stripped)) {
                    info.inSkillRegion = true;
                    inRegion = false;
                } else if (SkillDatabase.isSkillTrigger(stripped)) {
                    info.inSkillRegion = true;
                } else {
                    inRegion = false;
                }
            }
        }
    }

    private void markRuneGemsStrict(List<LineInfo> lines) {
        boolean inRegion = false;

        for (int i = 0; i < lines.size(); i++) {
            LineInfo info = lines.get(i);
            String stripped = info.stripped;

            if (info.isAlreadyMarked()) continue;

            if (isRuneGemLine(stripped)) {
                if (!inRegion) inRegion = true;
                info.inRuneGemRegion = true;
                if (stripped.contains("空") || stripped.contains("空置")) {
                    info.isEmptySlot = true;
                }
            } else if (inRegion) {
                if (stripped.isEmpty() || isOnlyIcon(stripped)) {
                    info.inRuneGemRegion = true;
                    info.isRuneGemBlankEnd = true;
                    inRegion = false;
                } else {
                    inRegion = false;
                }
            }
        }
    }

    private void markLoreStrict(List<LineInfo> lines) {
        for (int i = 0; i < lines.size(); i++) {
            LineInfo info = lines.get(i);
            if (info.isAlreadyMarked()) continue;

            String stripped = info.stripped;
            if (LoreTextDatabase.isLoreText(stripped)) {
                info.inLoreRegion = true;
                for (int j = i - 1; j >= 0; j--) {
                    LineInfo prev = lines.get(j);
                    if (prev.isAlreadyMarked()) break;
                    if (prev.stripped.isEmpty() || isOnlyIcon(prev.stripped)) {
                        prev.inLoreRegion = true;
                    } else {
                        break;
                    }
                }
                for (int j = i + 1; j < lines.size(); j++) {
                    LineInfo next = lines.get(j);
                    if (next.isAlreadyMarked()) break;
                    if (next.stripped.isEmpty() || isOnlyIcon(next.stripped)) {
                        next.inLoreRegion = true;
                    } else {
                        break;
                    }
                }
            }
        }
    }

    private void buildOutput(List<Text> lines, List<LineInfo> lineInfos, ModConfig config,
                             boolean keyPressed, Text nameLine, boolean allRuneGemsHidden, boolean anyRuneGemsHidden) {
        lines.clear();
        if (nameLine != null) lines.add(nameLine);

        List<LineInfo> visibleLines = new ArrayList<>();
        List<LineInfo> hiddenLines = new ArrayList<>();
        int hiddenCount = 0;

        for (LineInfo info : lineInfos) {
            if (info.isNameLine) continue;
            if (!info.shouldHide) {
                visibleLines.add(info);
            } else {
                hiddenLines.add(info);
                hiddenCount++;
            }
        }

        while (!visibleLines.isEmpty() &&
                (visibleLines.get(0).stripped.isEmpty() || isOnlyIcon(visibleLines.get(0).stripped))) {
            visibleLines.remove(0);
        }

        visibleLines = mergeEmptyLines(visibleLines);

        for (LineInfo info : visibleLines) {
            lines.add(info.line);
        }

        if (keyPressed && hiddenCount > 0) {
            lines.add(Text.literal(config.separatorLine));

            List<LineInfo> processedHidden = processHiddenLines(hiddenLines, lineInfos, config,
                    allRuneGemsHidden, anyRuneGemsHidden);

            for (LineInfo info : processedHidden) {
                lines.add(info.line);
            }
        } else if (!keyPressed && hiddenCount > 0) {
            lines.add(Text.literal(formatHintText(config.hintText, config.customKey.getDisplayName(), hiddenCount)));
        }
    }

    private List<LineInfo> processHiddenLines(List<LineInfo> hiddenLines, List<LineInfo> allLines,
                                              ModConfig config, boolean allRuneGemsHidden, boolean anyRuneGemsHidden) {
        List<LineInfo> result = new ArrayList<>();

        boolean hasBottomLayer = false;
        for (LineInfo info : allLines) {
            if (info.inBottomRegion && !info.shouldHide) {
                hasBottomLayer = true;
                break;
            }
        }

        List<LineInfo> enchantHidden = new ArrayList<>();
        List<LineInfo> attrHidden = new ArrayList<>();
        List<LineInfo> skillHidden = new ArrayList<>();
        List<LineInfo> runeGemHidden = new ArrayList<>();
        List<LineInfo> loreHidden = new ArrayList<>();
        List<LineInfo> bottomHidden = new ArrayList<>();
        List<LineInfo> otherHidden = new ArrayList<>();

        for (LineInfo info : hiddenLines) {
            if (info.inEnchantRegion) enchantHidden.add(info);
            else if (info.inAttributeRegion) attrHidden.add(info);
            else if (info.inSkillRegion) skillHidden.add(info);
            else if (info.inRuneGemRegion) runeGemHidden.add(info);
            else if (info.inLoreRegion) loreHidden.add(info);
            else if (info.inBottomRegion) bottomHidden.add(info);
            else otherHidden.add(info);
        }

        boolean firstRegion = true;

        if (!enchantHidden.isEmpty()) {
            if (!firstRegion) result.add(createBlankLineInfo());
            firstRegion = false;
            result.addAll(enchantHidden);
            if (!attrHidden.isEmpty() || !skillHidden.isEmpty() || !runeGemHidden.isEmpty() ||
                    !loreHidden.isEmpty() || !bottomHidden.isEmpty()) {
                result.add(createBlankLineInfo());
            }
        }

        if (!attrHidden.isEmpty()) {
            if (!firstRegion) result.add(createBlankLineInfo());
            firstRegion = false;
            result.addAll(attrHidden);
        }

        if (!skillHidden.isEmpty()) {
            if (!firstRegion) result.add(createBlankLineInfo());
            firstRegion = false;
            result.addAll(skillHidden);
        }

        if (!runeGemHidden.isEmpty()) {
            if (!firstRegion) result.add(createBlankLineInfo());
            firstRegion = false;
            result.addAll(runeGemHidden);
            if (anyRuneGemsHidden && hasBottomLayer) {
                result.add(createBlankLineInfo());
            }
        }

        if (!loreHidden.isEmpty()) {
            if (!firstRegion) result.add(createBlankLineInfo());
            firstRegion = false;
            result.addAll(loreHidden);
        }

        if (!bottomHidden.isEmpty()) {
            if (!firstRegion) result.add(createBlankLineInfo());
            firstRegion = false;
            result.addAll(bottomHidden);
        }

        if (!otherHidden.isEmpty()) {
            if (!firstRegion) result.add(createBlankLineInfo());
            firstRegion = false;
            result.addAll(otherHidden);
        }

        return result;
    }

    private LineInfo createBlankLineInfo() {
        Text blankText = Text.literal("");
        LineInfo blank = new LineInfo(blankText, "", "", -1, false);
        return blank;
    }

    private String formatHintText(String hintText, String keyName, int hiddenCount) {
        return hintText.replace("{key}", keyName).replace("{count}", String.valueOf(hiddenCount));
    }

    private List<LineInfo> mergeEmptyLines(List<LineInfo> lines) {
        List<LineInfo> result = new ArrayList<>();
        boolean lastWasEmpty = false;
        for (LineInfo info : lines) {
            boolean isEmpty = info.stripped.isEmpty() || isOnlyIcon(info.stripped);
            if (isEmpty) {
                if (!lastWasEmpty) {
                    result.add(info);
                    lastWasEmpty = true;
                }
            } else {
                result.add(info);
                lastWasEmpty = false;
            }
        }
        return result;
    }

    private boolean isRuneGemLine(String stripped) {
        if (stripped == null || stripped.isEmpty()) return false;

        if (stripped.contains("空符文槽") || stripped.contains("空置宝石槽")) return true;

        List<String> runeTypes = Arrays.asList(
                "屠杀符文", "强攻符文", "易攻符文", "格挡符文", "招架符文",
                "闪避符文", "破盾符文", "电符文"
        );
        for (String rune : runeTypes) {
            if (stripped.contains(rune)) return true;
        }

        List<String> gemTypes = Arrays.asList(
                "力量宝石", "法术宝石", "生命宝石", "防御宝石",
                "爆伤宝石", "闪避宝石", "格挡宝石", "招架宝石"
        );
        for (String gem : gemTypes) {
            if (stripped.contains(gem)) return true;
        }

        if (stripped.contains("[") && stripped.contains("]") &&
                (stripped.contains("符文") || stripped.contains("宝石"))) {
            return true;
        }

        return false;
    }

    private boolean checkToolOnlyBottom(ItemStack stack, String itemId) {
        boolean isNetheriteTool = itemId.contains("netherite_pickaxe") ||
                itemId.contains("netherite_shovel") ||
                itemId.contains("netherite_hoe") ||
                itemId.contains("netherite_axe") ||
                itemId.contains("fishing_rod");

        if (!isNetheriteTool) return false;

        if (stack.getNbt() != null && stack.getNbt().contains("CustomModelData")) {
            return true;
        }

        return false;
    }

    private boolean isBottomLine(String stripped) {
        return stripped.contains("耐久度") || stripped.contains("已绑定") ||
                stripped.contains("击杀人数") || stripped.contains("无法破坏");
    }

    private boolean isOnlyIcon(String stripped) {
        if (stripped.isEmpty()) return true;
        return !stripped.matches(".*[\u4e00-\u9fa5a-zA-Z0-9].*");
    }

    private String stripFormatting(String text) {
        return text.replaceAll("\u00a7[0-9a-zA-Z]", "");
    }

    private class LineInfo {
        Text line;
        String original;
        String stripped;
        int index;
        boolean isNameLine;

        boolean shouldHide = false;
        boolean isEnchantment = false;
        boolean inEnchantRegion = false;
        boolean inType2Region = false;
        boolean inAttributeRegion = false;
        boolean inElementRegion = false;
        boolean inSkillRegion = false;
        boolean inRuneGemRegion = false;
        boolean isEmptySlot = false;
        boolean isRuneGemBlankEnd = false;
        boolean inBottomRegion = false;
        boolean inLoreRegion = false;

        LineInfo(Text line, String original, String stripped, int index, boolean isNameLine) {
            this.line = line;
            this.original = original;
            this.stripped = stripped;
            this.index = index;
            this.isNameLine = isNameLine;
        }

        boolean isAlreadyMarked() {
            return isNameLine || isEnchantment || inEnchantRegion || inType2Region ||
                    inAttributeRegion || inElementRegion || inSkillRegion ||
                    inRuneGemRegion || inBottomRegion || inLoreRegion;
        }
    }
}