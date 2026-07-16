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
    public static final String VERSION = "2.0";
    public static final String AUTHOR = "Moyuans";
    public static final String GITHUB_URL = "https://github.com/Moo-yuan/BlockLegend-Hide-Lore-Tooltips";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final List<String> ARMOR_TRIM_START = Arrays.asList("已有升级", "Upgrade");
    private static final List<String> TOOL_STATS_START = Arrays.asList("在主手时", "When in main hand");
    private static final List<String> ARMOR_STATS_START = Arrays.asList(
            "戴在头上时", "穿在身上时", "穿在腿上时", "穿在脚上时",
            "When on head", "When on body", "When on legs", "When on feet"
    );
    private static final List<String> ARMOR_EXTRA_STATS = Arrays.asList("盔甲韧性", "击退抗性");

    // 工具保护列表：只启用底层隐藏（基于 CustomModelData + 基础ID）
    private static final List<String> TOOL_ONLY_BOTTOM = Arrays.asList(
            "minecraft:netherite_pickaxe|10000",  // 穿山稿
            "minecraft:netherite_shovel|10000",   // 愚公铲
            "minecraft:netherite_axe|10000",      // 鲁班斧
            "minecraft:fishing_rod|10006"         // 海神钓竿
    );

    private static final List<String> PROTECTED_KEYWORDS = Arrays.asList(
            "购买", "领取", "预览", "评分", "收藏", "升级至", "右键查看", "右键使用", "背包",
            "左键点击", "上一类别", "下一类别", "左键出售", "右键出售",
            "右键即可出售该物品", "出售32个", "左键签收全部 ", "右键删除全部 ",
            "左键装备", "右键自定义",
            "左键签收", "右键删除",
            "无权限者仅能移除自己的商品", "无权限者仅能操作自己的商品",
            "左键单击", "右键单击", "Shift+左键单击"
    );

    // v2.0 新增：图鉴/制作界面关键词
    // 这些关键词出现在图鉴/制作界面，整片保护不隐藏
    private static final List<String> GUIDE_KEYWORDS = Arrays.asList(
            "制作时间:",    // 制作时间: 1200s
            "配方:",        // 配方:
            "<进阶装备>",  // <进阶装备>
            "左键制作!",    // 左键制作!
            "右键预览!"     // 右键预览!
    );

    @Override
    public void onInitializeClient() {
        LOGGER.info("[{}] v{} BlockLegend服务器自定义隐藏信息模组加载中...", MOD_NAME, VERSION);
        LOGGER.info("[{}] 作者: {} | GitHub: {}", MOD_NAME, AUTHOR, GITHUB_URL);

        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            if (stack == null || stack.isEmpty()) return;

            ModConfig config = ModConfig.getInstance();
            if (!config.enabled) return;

            // 附魔书单独处理：直接移除所有附魔行（保持原有逻辑）
            if (stack.getItem() == Items.ENCHANTED_BOOK) {
                if (config.protectEnchantedBook) return;
                else removeEnchantedBookEnchantments(lines);
            }

            // 移除原版属性（纹饰/工具/护甲）
            removeVanillaAttributes(lines, config);

            boolean keyPressed = isCustomKeyPressed();
            Text nameLine = lines.isEmpty() ? null : lines.get(0);
            boolean isPaper = stack.getItem() == Items.PAPER;
            boolean isFeather = stack.getItem() == Items.FEATHER;
            boolean isStick = stack.getItem() == Items.STICK;

            String itemId = getItemId(stack);
            boolean isTool = ToolTypeDatabase.isTool(itemId);
            boolean toolHasType2 = ToolTypeDatabase.hasType2(itemId);
            boolean isToolOnlyBottom = checkToolOnlyBottom(stack, itemId);

            // 构建 LineInfo 列表，逐行解析
            List<LineInfo> lineInfos = new ArrayList<>();
            for (int i = 0; i < lines.size(); i++) {
                Text line = lines.get(i);
                String lineStr = line.getString();
                String stripped = stripFormatting(lineStr);
                lineInfos.add(new LineInfo(line, lineStr, stripped, i, i == 0));
            }

            // ========== v2.0 新增：图鉴区识别（最高优先级）==========
            // 如果检测到图鉴/制作界面特征，整片保护不隐藏
            markGuideRegion(lineInfos);

            // ========== 分区识别（按优先级顺序）==========
            // 1. 底部区：从底部向上扫描，标记耐久/绑定/击杀/无法破坏
            markBottomStrict(lineInfos);

            // 2. 类型2区：标记星级/等级/武器类型
            // 注意：类型2必须在附魔区之前识别，因为附魔区需要类型2位置作为边界
            markType2Strict(lineInfos, isTool, toolHasType2);

            // 3. 附魔区：v2.0 重构 - 整片区域判定
            // 名称后到类型2/其他区域前 = 附魔区整片
            markEnchantRegion(lineInfos);

            // 4. 属性区：RPG前缀 + 属性关键词
            markAttributesStrict(lineInfos);

            // 5. 元素区：元素前缀 + 元素类型 + 伤害/防御
            markElementsStrict(lineInfos);

            // 6. 技能区：触发词 + 技能名 + 冷却/持续
            markSkillsStrict(lineInfos);

            // 7. 符文/宝石区：空槽/已镶嵌
            markRuneGemsStrict(lineInfos);

            // 8. Lore文本区：套装/锻造/分隔线等
            markLoreStrict(lineInfos);

            // ========== v2.0 白名单关键词保护 ==========
            for (LineInfo info : lineInfos) {
                if (isProtectedByWhitelist(info.stripped)) {
                    info.isProtectedByWhitelist = true;
                }
            }

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
            if (isToolOnlyBottom) {
                // 工具保护：只启用底层隐藏
                for (LineInfo info : lineInfos) {
                    if (info.isNameLine) continue;
                    boolean shouldHide = false;
                    if (info.inBottomRegion) {
                        if (info.stripped.contains("耐久度") && config.hideDurability) shouldHide = true;
                        if (info.stripped.contains("已绑定") && config.hideBoundStatus) shouldHide = true;
                        if (info.stripped.contains("击杀人数") && config.hideKillCount) shouldHide = true;
                        if (info.stripped.contains("无法破坏") && config.hideUnbreakable) shouldHide = true;
                    }
                    info.shouldHide = shouldHide;
                }
            } else {
                // 正常隐藏逻辑
                for (LineInfo info : lineInfos) {
                    if (info.isNameLine) continue;
                    boolean shouldHide = false;

                    // v2.0 附魔区整片隐藏：hideEnchantments 控制整个附魔区
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

                // 无法破坏替代耐久：只隐藏底部区的耐久度
                if (config.unbreakableOverridesDurability && hasUnbreakable(stack)) {
                    for (LineInfo info : lineInfos) {
                        if (info.inBottomRegion && info.stripped.contains("耐久度")) info.shouldHide = true;
                    }
                }

                // Paper/Feather 保护：只保留"已绑定"行
                if ((isPaper || isFeather) && config.protectPaper) {
                    for (LineInfo info : lineInfos) {
                        if (!info.stripped.contains("已绑定")) {
                            info.shouldHide = false;
                        }
                    }
                }

                // Stick 完全保护
                if (isStick) {
                    for (LineInfo info : lineInfos) {
                        info.shouldHide = false;
                    }
                }

                // 无法破坏加粗
                if (config.boldUnbreakable) {
                    for (LineInfo info : lineInfos) {
                        if (info.stripped.contains("无法破坏")) {
                            info.line = Text.literal("\u00a7l" + info.line.getString() + "\u00a7r");
                        }
                    }
                }
            }

            // 白名单保护强制显示
            for (LineInfo info : lineInfos) {
                if (info.isProtectedByWhitelist) {
                    info.shouldHide = false;
                }
            }

            buildOutput(lines, lineInfos, config, keyPressed, nameLine, allRuneGemsHidden, anyRuneGemsHidden);
        });

        ModConfig.load();
        LOGGER.info("[{}] v{} 模组加载完成！", MOD_NAME, VERSION);
    }

    // ========== 工具方法 ==========

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

    private boolean isProtectedByWhitelist(String stripped) {
        if (stripped == null || stripped.isEmpty()) return false;
        for (String keyword : PROTECTED_KEYWORDS) {
            if (stripped.contains(keyword)) return true;
        }
        if (stripped.matches(".*右键最多装填\\d+次水.*")) return true;
        return false;
    }

    // ========== v2.0 新增：图鉴区识别 ==========

    /**
     * 图鉴区识别（最高优先级）
     *
     * 检测到图鉴/制作界面特征时，整片标记为图鉴区，强制不隐藏。
     * 图鉴物品 Lore 结构与普通装备不同，包含制作时间、配方、材料列表等。
     *
     * 识别关键词：
     * - "制作时间:"  → 制作时间: 1200s
     * - "配方:"      → 配方:
     * - "<进阶装备>" → <进阶装备>
     * - "左键制作!"  → 左键制作!
     * - "右键预览!"  → 右键预览!
     *
     * 这些关键词在普通装备 Lore 中不会出现，误判率极低。
     */
    private void markGuideRegion(List<LineInfo> lines) {
        // 先检测是否包含图鉴特征
        boolean isGuideItem = false;
        for (LineInfo info : lines) {
            if (isGuideKeyword(info.stripped)) {
                isGuideItem = true;
                break;
            }
        }

        // 如果是图鉴物品，整片标记（名称行除外）
        if (isGuideItem) {
            for (LineInfo info : lines) {
                if (!info.isNameLine) {
                    info.inGuideRegion = true;
                }
            }
        }
    }

    /**
     * 检查是否是图鉴关键词
     */
    private boolean isGuideKeyword(String stripped) {
        if (stripped == null || stripped.isEmpty()) return false;
        for (String keyword : GUIDE_KEYWORDS) {
            if (stripped.contains(keyword)) return true;
        }
        return false;
    }

    // ========== 原版属性移除 ==========

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

    // ========== 附魔书处理 ==========

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

    // ========== v2.0 核心重构：附魔区整片识别 ==========

    /**
     * v2.0 核心重构：附魔区整片识别
     *
     * 原理：名称行(0)到类型2区(2)之间的整片区域 = 附魔区
     * 不再逐行匹配附魔名称/描述，避免插件附魔漏识别和描述误判问题
     *
     * 边界查找优先级：
     * 1. 类型2开头（星级/等级/武器类型）- 最可靠
     * 2. 属性开头（RPG前缀）
     * 3. 元素开头（元素前缀）
     * 4. 符文/宝石行
     * 5. 底部行（耐久/绑定/击杀/无法破坏）
     * 6. 明确技能触发词（严格匹配开头，避免"点击右键"等附魔描述误判）
     *
     * 如果第1行就是区域开头 → 无附魔区
     */
    private void markEnchantRegion(List<LineInfo> lines) {
        // 第一步：找类型2起始位置（最可靠的边界）
        int type2Start = findType2Start(lines);

        if (type2Start > 1) {
            // 有类型2，且不在第1行 → 附魔区 = [1, type2Start-1]
            for (int i = 1; i < type2Start; i++) {
                lines.get(i).inEnchantRegion = true;
            }
            return;
        }

        // 第二步：无类型2，找其他边界
        int fallbackStart = findFallbackRegionStart(lines);

        if (fallbackStart > 1) {
            // 有其他区域 → 附魔区 = [1, fallbackStart-1]
            for (int i = 1; i < fallbackStart; i++) {
                lines.get(i).inEnchantRegion = true;
            }
        }
        // 如果 fallbackStart <= 1 → 无附魔区（第1行就是区域开头或只有名称行）
    }

    /**
     * 查找类型2起始位置
     * 返回类型2开头的行索引，没找到返回 -1
     */
    private int findType2Start(List<LineInfo> lines) {
        for (int i = 1; i < lines.size(); i++) {
            if (WeaponTypeDatabase.isType2Start(lines.get(i).stripped)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 查找 fallback 区域起始位置（无类型2时使用）
     *
     * 优先级：属性 > 元素 > 符文/宝石 > 底部 > 明确技能触发词
     *
     * 技能触发词严格匹配开头，避免与附魔描述冲突：
     * - 匹配："攻击时 ||"、"左键 ||"、"右键 ||"、"被动技能"、"终结技"
     * - 不匹配："点击右键..."（附魔描述）
     */
    private int findFallbackRegionStart(List<LineInfo> lines) {
        for (int i = 1; i < lines.size(); i++) {
            String stripped = lines.get(i).stripped;

            // 属性开头（RPG前缀，很可靠）
            if (AttributeDatabase.isAttributeLine(stripped)) return i;

            // 元素开头（元素前缀，很可靠）
            if (ElementDatabase.isElementLine(stripped)) return i;

            // 符文/宝石行（很可靠）
            if (isRuneGemLine(stripped)) return i;

            // 底部行（很可靠）
            if (isBottomLine(stripped)) return i;

            // 明确技能触发词（严格匹配开头，避免误判附魔描述）
            // 注意：技能格式是 "攻击时 || 血斩"，所以触发词后面跟空格
            if (isStrictSkillTrigger(stripped)) return i;
        }
        return -1;
    }

    /**
     * 严格技能触发词匹配
     * 只匹配明确的技能开头，避免与附魔描述冲突
     *
     * 匹配：攻击时、左键、右键、Shift+右键、Shift+左键、被动技能、终结技
     * 注意：后面必须跟空格（技能格式："攻击时 || 血斩"）
     * 避免匹配 "点击右键..." 等附魔描述
     */
    private boolean isStrictSkillTrigger(String stripped) {
        if (stripped == null || stripped.isEmpty()) return false;

        // 必须以这些词开头，且后面跟着空格（技能格式："攻击时 || 血斩"）
        if (stripped.startsWith("攻击时 ")) return true;
        if (stripped.startsWith("左键 ")) return true;
        if (stripped.startsWith("右键 ")) return true;
        if (stripped.startsWith("Shift+右键 ")) return true;
        if (stripped.startsWith("Shift+左键 ")) return true;
        if (stripped.startsWith("被动技能")) return true;
        if (stripped.startsWith("终结技")) return true;

        return false;
    }

    // ========== 底部区识别 ==========

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

    // ========== 类型2区识别 ==========

    /**
     * v2.0 修改：删除 isEnchantment 引用
     * 类型2区后面不会是附魔行（附魔区在类型2之前），所以不需要检查 inEnchantRegion
     */
    private void markType2Strict(List<LineInfo> lines, boolean isTool, boolean toolHasType2) {
        if (isTool && !toolHasType2) return;

        for (int i = 0; i < lines.size(); i++) {
            LineInfo info = lines.get(i);
            if (info.isNameLine || info.inBottomRegion) continue;

            if (WeaponTypeDatabase.isType2Start(info.stripped)) {
                info.inType2Region = true;
                // 扩展下一行（如果不是名称行/底部行）
                if (i + 1 < lines.size()) {
                    LineInfo next = lines.get(i + 1);
                    if (!next.isNameLine && !next.inBottomRegion) {
                        next.inType2Region = true;
                    }
                }
                // 扩展下下行（如果是空行/图标）
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

    // ========== 属性区识别 ==========

    private void markAttributesStrict(List<LineInfo> lines) {
        boolean inRegion = false;
        int type2End = -1;

        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).inType2Region) type2End = i;
        }

        for (int i = 0; i < lines.size(); i++) {
            LineInfo info = lines.get(i);
            String stripped = info.stripped;

            // v2.0：使用 inEnchantRegion 替代 isEnchantment
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

    // ========== 元素区识别 ==========

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

    // ========== 技能区识别 ==========

    private void markSkillsStrict(List<LineInfo> lines) {
        boolean inRegion = false;
        for (int i = 0; i < lines.size(); i++) {
            LineInfo info = lines.get(i);
            String stripped = info.stripped;

            if (info.isAlreadyMarked()) continue;

            // 白名单保护优先
            if (isProtectedByWhitelist(stripped)) {
                inRegion = false;
                continue;
            }

            if (SkillDatabase.isSkillTrigger(stripped)) {
                inRegion = true;
                info.inSkillRegion = true;
            } else if (inRegion) {
                if (SkillDatabase.isCooldownLine(stripped)) {
                    info.inSkillRegion = true;
                } else if (stripped.isEmpty() || isOnlyIcon(stripped)) {
                    info.inSkillRegion = true;
                    inRegion = false;
                } else {
                    inRegion = false;
                }
            }
        }
    }

    // ========== 符文/宝石区识别 ==========

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

    // ========== Lore文本区识别 ==========

    private void markLoreStrict(List<LineInfo> lines) {
        for (int i = 0; i < lines.size(); i++) {
            LineInfo info = lines.get(i);
            if (info.isAlreadyMarked()) continue;

            String stripped = info.stripped;
            if (LoreTextDatabase.isLoreText(stripped)) {
                info.inLoreRegion = true;
                // 向前扩展空行
                for (int j = i - 1; j >= 0; j--) {
                    LineInfo prev = lines.get(j);
                    if (prev.isAlreadyMarked()) break;
                    if (prev.stripped.isEmpty() || isOnlyIcon(prev.stripped)) {
                        prev.inLoreRegion = true;
                    } else {
                        break;
                    }
                }
                // 向后扩展空行
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

    // ========== v2.0 重构：processHiddenLines + addRegionSmart ==========

    /**
     * v2.0 重构：展开模式下重组隐藏行
     *
     * 核心改进：
     * 1. 使用 addRegionSmart 智能添加区域，避免连续空行
     * 2. 附魔区隐藏后，如果后面还有其他隐藏区域，自动添加空行分隔
     * 3. 区域间空行智能判断：只有当前区域最后一行不是空行时才添加
     */
    private List<LineInfo> processHiddenLines(List<LineInfo> hiddenLines, List<LineInfo> allLines,
                                              ModConfig config, boolean allRuneGemsHidden, boolean anyRuneGemsHidden) {
        List<LineInfo> result = new ArrayList<>();

        // 按区域分类隐藏行
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

        // 判断各区域是否有隐藏内容（用于附魔区空行判断）
        boolean hasAfterEnchant = !attrHidden.isEmpty() || !skillHidden.isEmpty() ||
                !runeGemHidden.isEmpty() || !loreHidden.isEmpty() ||
                !bottomHidden.isEmpty() || !otherHidden.isEmpty();
        boolean hasAfterAttr = !skillHidden.isEmpty() || !runeGemHidden.isEmpty() ||
                !loreHidden.isEmpty() || !bottomHidden.isEmpty() || !otherHidden.isEmpty();
        boolean hasAfterSkill = !runeGemHidden.isEmpty() || !loreHidden.isEmpty() ||
                !bottomHidden.isEmpty() || !otherHidden.isEmpty();
        boolean hasAfterRuneGem = !loreHidden.isEmpty() || !bottomHidden.isEmpty() || !otherHidden.isEmpty();
        boolean hasAfterLore = !bottomHidden.isEmpty() || !otherHidden.isEmpty();
        boolean hasAfterBottom = !otherHidden.isEmpty();

        // 按顺序添加各区域
        addRegionSmart(result, enchantHidden, true, hasAfterEnchant, true);    // 附魔区，isFirst=true, isEnchant=true
        addRegionSmart(result, attrHidden, false, hasAfterAttr, false);
        addRegionSmart(result, skillHidden, false, hasAfterSkill, false);
        addRegionSmart(result, runeGemHidden, false, hasAfterRuneGem, false);
        addRegionSmart(result, loreHidden, false, hasAfterLore, false);
        addRegionSmart(result, bottomHidden, false, hasAfterBottom, false);
        addRegionSmart(result, otherHidden, false, false, false);

        return result;
    }

    /**
     * 智能添加区域
     *
     * @param result 结果列表
     * @param regionLines 区域隐藏行
     * @param isFirstRegion 是否是第一个区域（不需要前导空行）
     * @param hasFollowingRegions 后面是否还有其他隐藏区域
     * @param isEnchantRegion 是否是附魔区（特殊处理：后面有区域时添加空行）
     */
    private void addRegionSmart(List<LineInfo> result, List<LineInfo> regionLines,
                                boolean isFirstRegion, boolean hasFollowingRegions,
                                boolean isEnchantRegion) {
        if (regionLines.isEmpty()) return;

        // 不是第一个区域 → 需要添加分隔空行
        if (!isFirstRegion) {
            // 只有当前 result 最后一行不是空行时才添加空行
            // 避免与上一区域的结束空行重复
            if (!result.isEmpty() && !isLastLineEmpty(result)) {
                result.add(createBlankLineInfo());
            }
        }

        // 添加区域所有行
        result.addAll(regionLines);

        // 附魔区特殊处理：如果后面还有其他隐藏区域，且最后一行不是空行，添加空行
        // 这样展开模式下附魔区和下一个区域之间有清晰分隔
        if (isEnchantRegion && hasFollowingRegions) {
            if (!result.isEmpty() && !isLastLineEmpty(result)) {
                result.add(createBlankLineInfo());
            }
        }
    }

    /**
     * 检查列表最后一行是否为空行
     */
    private boolean isLastLineEmpty(List<LineInfo> lines) {
        if (lines.isEmpty()) return false;
        LineInfo last = lines.get(lines.size() - 1);
        return last.stripped.isEmpty() || isOnlyIcon(last.stripped);
    }

    // ========== 输出构建 ==========

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

        // 移除开头空行
        while (!visibleLines.isEmpty() &&
                (visibleLines.get(0).stripped.isEmpty() || isOnlyIcon(visibleLines.get(0).stripped))) {
            visibleLines.remove(0);
        }

        // 移除末尾空行
        while (!visibleLines.isEmpty() &&
                (visibleLines.get(visibleLines.size() - 1).stripped.isEmpty() ||
                        isOnlyIcon(visibleLines.get(visibleLines.size() - 1).stripped))) {
            visibleLines.remove(visibleLines.size() - 1);
        }

        // 合并连续空行（保险措施）
        visibleLines = mergeEmptyLines(visibleLines);

        for (LineInfo info : visibleLines) {
            lines.add(info.line);
        }

        if (keyPressed && hiddenCount > 0) {
            lines.add(Text.literal(config.separatorLine));

            List<LineInfo> processedHidden = processHiddenLines(hiddenLines, lineInfos, config,
                    allRuneGemsHidden, anyRuneGemsHidden);

            // v2.0：对展开模式的隐藏行也合并连续空行（保险措施）
            processedHidden = mergeEmptyLines(processedHidden);

            for (LineInfo info : processedHidden) {
                lines.add(info.line);
            }
        } else if (!keyPressed && hiddenCount > 0) {
            lines.add(Text.literal(formatHintText(config.hintText, config.customKey.getDisplayName(), hiddenCount)));
        }
    }

    private LineInfo createBlankLineInfo() {
        Text blankText = Text.literal("");
        return new LineInfo(blankText, "", "", -1, false);
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

    // ========== 工具方法 ==========

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

    // ========== LineInfo 内部类 ==========

    /**
     * v2.0 修改：
     * 1. 删除 isEnchantment 和 isEnchantmentDesc 字段（附魔区统一使用 inEnchantRegion）
     * 2. 新增 inGuideRegion 字段（图鉴区整片保护）
     */
    private class LineInfo {
        Text line;
        String original;
        String stripped;
        int index;
        boolean isNameLine;

        boolean shouldHide = false;
        // v2.0 删除：boolean isEnchantment = false;
        // v2.0 删除：boolean isEnchantmentDesc = false;
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
        boolean isProtectedByWhitelist = false;
        // v2.0 新增：图鉴区标记，整片保护不隐藏
        boolean inGuideRegion = false;

        LineInfo(Text line, String original, String stripped, int index, boolean isNameLine) {
            this.line = line;
            this.original = original;
            this.stripped = stripped;
            this.index = index;
            this.isNameLine = isNameLine;
        }

        /**
         * v2.0 修改：
         * 1. 删除 isEnchantment 和 isEnchantmentDesc 检查
         * 2. 新增 inGuideRegion 检查
         * 附魔区统一使用 inEnchantRegion，图鉴区使用 inGuideRegion
         */
        boolean isAlreadyMarked() {
            return isNameLine || inEnchantRegion || inType2Region ||
                    inAttributeRegion || inElementRegion || inSkillRegion ||
                    inRuneGemRegion || inBottomRegion || inLoreRegion || inGuideRegion;
        }
    }
}