package com.mooyuan.blocklegend.hidelore;

import java.util.Arrays;
import java.util.List;

/**
 * v2.0 简化：删除附魔描述相关方法
 *
 * 保留内容：
 * - 附魔前缀、插件附魔列表、原版附魔列表（附魔书处理用）
 * - isAnyEnchantment() 方法（附魔书处理用）
 *
 * 删除内容：
 * - ENCHANTMENT_DESC_HARD（硬匹配附魔描述库）
 * - isKeywordEnchantmentDesc()（关键词组合匹配）
 * - isExactOrContainsDesc()（描述匹配入口）
 * - isVanillaEnchantmentDesc()（原版附魔描述）
 * - isAnyEnchantmentDesc()（任意附魔描述）
 *
 * 原因：v2.0 附魔区改为整片区域判定，不再逐行识别附魔描述
 * 附魔描述与技能/属性描述的关键词冲突问题通过整片区域边界解决
 */
public class EnchantmentDatabase {

    public static final List<String> ENCHANTMENT_PREFIXES = Arrays.asList("鬸", "鬹", "鬻", "鬺");

    public static final List<String> PLUGIN_ENCHANTMENTS = Arrays.asList(
            "鬸乘云", "鬹沉重打击", "鬹大步流星", "鬸电锯", "鬹钓雷", "鬹反震装甲", "鬸丰收",
            "鬹火箭燃料", "鬹坚壁清野", "鬸捷足", "鬹恐慌", "鬻炼狱", "鬹梦魇反应装甲",
            "鬸耐热", "鬸粘液杀手", "鬹破壁", "鬸潜影牧民", "鬸轻踏", "鬸熔炼", "鬻生命打击",
            "鬹生死时速", "鬻圣焰附着", "鬹脱身", "鬹威慑", "鬸修枝", "鬻药水免疫", "鬸夜视",
            "鬸羽落", "鬸窒息免疫", "鬻终界者", "鬸饱食", "鬺淬火打磨", "鬺锻体", "鬹飞檐走壁",
            "鬺秘术增幅", "鬺铁壁石肤", "鬻背水一战", "鬹捕获", "鬹不屈", "鬻刺客", "鬻淬毒护甲",
            "鬻导师光环", "鬸防爆", "鬸减震", "鬸节食", "鬻饥饿诅咒", "鬻疾风光环", "鬸急救",
            "鬻经验猎手", "鬺空中优势", "鬻快速射击", "鬸矿灯", "鬹雷返", "鬸绿化", "鬻霉运诅咒",
            "鬸耐力", "鬸农夫", "鬹疲倦", "鬸牵引", "鬹弱敌", "鬻生命偷取", "鬹新陈代谢",
            "鬹雪霜", "鬻压制", "鬻再生光环", "鬻治疗增幅", "鬺自愈", "鬻百步穿杨", "鬹毒刺之地",
            "鬹遁形", "鬹腐浊之地", "鬹哥布林弹射", "鬹攻击失效", "鬹经验补给", "鬹疾速逃脱",
            "鬻绝处逢生", "鬹雷击", "鬹掠取", "鬻韧性", "鬺三重射击", "鬻铁壁", "鬺绝处逢生",
            "鬺韧性", "鬺铁壁", "鬺御战姿态", "鬹下界勘探者", "鬺先发制人", "鬺献祭之力",
            "鬻御战姿态", "鬺致命打击", "鬻摧木", "鬹缓冲", "鬺净化", "鬻猎手", "鬸末影杀手",
            "破坏诅咒", "鬺突进", "鬺悬浮", "鬻永恒诅咒", "鬻幽匿杀手", "鬻战鼓光环", "鬻*追踪导弹"
    );

    public static final List<String> VANILLA_ENCHANTMENTS = Arrays.asList(
            "水下速掘", "节肢杀手", "绑定诅咒", "爆炸保护", "Breach", "引雷", "Density",
            "深海探索者", "效率", "摔落保护", "火焰附加", "火焰保护", "火矢", "时运",
            "冰霜行者", "穿刺", "无限", "击退", "抢夺", "忠诚", "海之眷顾", "饵钓",
            "经验修补", "多重射击", "穿透", "力量", "弹射物保护", "保护", "冲击",
            "快速装填", "水下呼吸", "激流", "锋利", "精准采集", "亡灵杀手", "灵魂疾行",
            "横扫之刃", "迅捷潜行", "荆棘", "耐久", "消失诅咒", "Wind Burst"
    );

    // ========== v2.0 删除：附魔描述相关方法 ==========
    // 以下方法已删除，附魔区改为整片区域判定：
    // - ENCHANTMENT_DESC_HARD
    // - isKeywordEnchantmentDesc()
    // - isExactOrContainsDesc()
    // - isVanillaEnchantmentDesc()
    // - isAnyEnchantmentDesc()

    // ========== 保留：附魔书处理用 ==========

    public static boolean isAnyEnchantment(String stripped) {
        if (stripped == null || stripped.isEmpty()) return false;
        for (String prefix : ENCHANTMENT_PREFIXES) {
            if (stripped.startsWith(prefix)) return true;
        }
        for (String enc : PLUGIN_ENCHANTMENTS) {
            if (stripped.contains(enc)) return true;
        }
        for (String enc : VANILLA_ENCHANTMENTS) {
            if (stripped.contains(enc)) return true;
        }
        return false;
    }

    // ========== 保留：原版附魔识别（附魔书用）==========
    public static boolean isVanillaEnchantment(String stripped) {
        if (stripped == null || stripped.isEmpty()) return false;
        for (String enc : VANILLA_ENCHANTMENTS) {
            if (stripped.contains(enc)) return true;
        }
        return false;
    }
}