package com.mooyuan.blocklegend.hidelore;

import java.util.Arrays;
import java.util.List;

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

    // ========== 硬匹配附魔描述数据库（无变量描述）==========
    public static final List<String> ENCHANTMENT_DESC_HARD = Arrays.asList(
        "一次射出 3 支箭矢，对烟花和药水箭生效",
        "一次性射出三支箭",
        "三叉戟投掷后会返回",
        "佩戴后获得无限夜视效果",
        "使玩家在水下挖掘方块的速度与在陆地上时一样",
        "右键点击泥土可将其转换为草方块",
        "在水下或雨天投掷三叉戟时会推动玩家",
        "在获取经验球时，修补主/副手以及盔甲栏的工具与护甲",
        "在雷雨天三叉戟落地时引发闪电",
        "射击时普通箭矢不会被消耗",
        "挖掘的方块会完整掉落，掉落其本身",
        "死亡时带有该 buff 的物品会被销毁而不是掉落",
        "点击右键消除当前所有负面效果(CD:180秒)",
        "砍伐天然原木时，自动砍倒整棵树（适用所有原版木材）",
        "穿戴时获得火焰抗性",
        "穿戴获得缓降效果",
        "箭矢使目标着火，造成 5 火焰伤害",
        "自动熔炼挖掘的方块",
        "自动补种农作物",
        "装备的物品无法从护甲槽中移除",
        "进入黑暗时自动获得发光效果，持续5秒(CD:3秒)",
        "防止在铁砧上修改此物品",
        "防止跳跃时踩坏作物"
    );

    // ========== 关键词组合（有变量描述）==========
    public static boolean isKeywordEnchantmentDesc(String stripped) {
        if (stripped == null || stripped.isEmpty()) return false;

        // 提升X点Y
        if (stripped.contains("提升") && stripped.contains("点")) {
            if (stripped.contains("生命值")) return true;
            if (stripped.contains("攻击力")) return true;
            if (stripped.contains("技能伤害加成")) return true;
            if (stripped.contains("防御力")) return true;
        }

        // 攻击有X%的概率/几率
        if (stripped.contains("攻击有") && (stripped.contains("概率") || stripped.contains("几率"))) return true;

        // 每X秒有X%的概率/几率
        if (stripped.contains("每") && stripped.contains("秒") && (stripped.contains("概率") || stripped.contains("几率"))) return true;

        // 每X秒有X%的概率恢复
        if (stripped.contains("每") && stripped.contains("秒") && stripped.contains("恢复")) return true;

        // 遭受攻击时有X%的几率
        if (stripped.contains("遭受攻击") && (stripped.contains("几率") || stripped.contains("概率"))) return true;

        // 受到伤害时有X%的概率
        if (stripped.contains("受到伤害") && (stripped.contains("概率") || stripped.contains("几率"))) return true;

        // 格挡攻击时有X%概率
        if (stripped.contains("格挡攻击") && (stripped.contains("概率") || stripped.contains("几率"))) return true;

        // 击杀生物后/时
        if (stripped.contains("击杀生物") && (stripped.contains("获得") || stripped.contains("几率"))) return true;

        // 血量低于X%时
        if (stripped.contains("血量低于") && stripped.contains("%")) return true;

        // 生命值低于X%时
        if (stripped.contains("生命值低于") && stripped.contains("%")) return true;

        // 在...时触发
        if (stripped.contains("触发") && (stripped.contains("时") || stripped.contains("受到伤害"))) return true;

        // 给X格范围内
        if (stripped.contains("给") && stripped.contains("格") && stripped.contains("范围")) return true;

        // 范围内所有玩家
        if (stripped.contains("范围") && stripped.contains("所有玩家")) return true;

        // 增加/减少X%的Y
        if ((stripped.contains("增加") || stripped.contains("提高") || stripped.contains("降低") || stripped.contains("减少")) 
            && stripped.contains("%")) return true;

        // 赋予+X的Y
        if (stripped.contains("赋予") && stripped.contains("+")) return true;

        // 以牺牲X%生命值为代价
        if (stripped.contains("牺牲") && stripped.contains("生命值")) return true;

        // 自身造成的PVP伤害
        if (stripped.contains("PVP伤害") || stripped.contains("PVP")) return true;

        // 命中头部时
        if (stripped.contains("命中") && stripped.contains("头部")) return true;

        // 对满血目标
        if (stripped.contains("满血") && stripped.contains("目标")) return true;

        // 对...生物增加
        if (stripped.contains("对") && stripped.contains("生物") && stripped.contains("增加")) return true;

        // 在...维度时
        if (stripped.contains("维度") && stripped.contains("时")) return true;

        // 在...中射箭
        if (stripped.contains("射箭") && stripped.contains("额外")) return true;

        // 在弓的张力为X%时
        if (stripped.contains("弓") && stripped.contains("张力")) return true;

        // 箭矢每飞行X个方块
        if (stripped.contains("箭矢") && stripped.contains("飞行")) return true;

        // 有X%的几率在对手身上引发X道闪电
        if (stripped.contains("闪电") || stripped.contains("雷击")) return true;

        // 一次性射出三支箭
        if (stripped.contains("一次性") && stripped.contains("射出")) return true;

        // 点击右键可...
        if (stripped.contains("点击右键")) return true;

        // 右键点击...
        if (stripped.contains("右键点击")) return true;

        // 自动补种/熔炼/砍倒
        if (stripped.contains("自动") && (stripped.contains("补种") || stripped.contains("熔炼") || stripped.contains("砍倒"))) return true;

        // 使收杆的拉扯效果
        if (stripped.contains("收杆") || stripped.contains("拉扯")) return true;

        // 通过进食获得
        if (stripped.contains("进食") && stripped.contains("饱食度")) return true;

        // 受到治疗效果
        if (stripped.contains("治疗") && stripped.contains("效果")) return true;

        // 奔跑时饱食损失
        if (stripped.contains("奔跑") && stripped.contains("饱食")) return true;

        // 跳跃后获得
        if (stripped.contains("跳跃") && stripped.contains("获得")) return true;

        // 进入黑暗时
        if (stripped.contains("黑暗") && stripped.contains("发光")) return true;

        // 阻挡伤害时
        if (stripped.contains("阻挡") && stripped.contains("伤害")) return true;

        // 移动速度提升
        if (stripped.contains("移动速度") && stripped.contains("提升")) return true;

        // 穿戴时/佩戴后获得
        if ((stripped.contains("穿戴") || stripped.contains("佩戴")) && stripped.contains("获得")) return true;

        // 穿戴获得
        if (stripped.contains("穿戴获得")) return true;

        // 防止...
        if (stripped.contains("防止")) return true;

        // 挖掘的方块会完整掉落
        if (stripped.contains("挖掘") && stripped.contains("完整掉落")) return true;

        // 射击时普通箭矢不会被消耗
        if (stripped.contains("射击") && stripped.contains("消耗")) return true;

        // 三叉戟投掷后会返回
        if (stripped.contains("三叉戟") && stripped.contains("返回")) return true;

        // 在水下或雨天投掷三叉戟时会推动玩家
        if (stripped.contains("三叉戟") && stripped.contains("推动")) return true;

        // 在雷雨天三叉戟落地时引发闪电
        if (stripped.contains("雷") && stripped.contains("三叉戟") && stripped.contains("闪电")) return true;

        // 在玩家周围X格范围内的水源转化为冰
        if (stripped.contains("水源") && stripped.contains("冰")) return true;

        // 在灵魂沙和灵魂土上行走速度增加
        if (stripped.contains("灵魂") && stripped.contains("速度")) return true;

        // 减少潜行时的移动减缓
        if (stripped.contains("潜行") && stripped.contains("减缓")) return true;

        // 给予X%的几率将部分受到的伤害反射给攻击者
        if (stripped.contains("反射") && stripped.contains("伤害")) return true;

        // 装备的物品无法从护甲槽中移除
        if (stripped.contains("装备") && stripped.contains("移除")) return true;

        // 死亡时带有该buff的物品会被销毁而不是掉落
        if (stripped.contains("死亡") && stripped.contains("销毁")) return true;

        // 防止在铁砧上修改此物品
        if (stripped.contains("铁砧") && stripped.contains("修改")) return true;

        // Multiplies upwards knockback
        if (stripped.contains("knockback") || stripped.contains("upwards")) return true;

        // Reduces effectiveness of the target's armor
        if (stripped.contains("armor") && stripped.contains("effectiveness")) return true;

        // Deals bonus damage per block fallen
        if (stripped.contains("bonus damage") && stripped.contains("fallen")) return true;

        return false;
    }

    public static boolean isExactOrContainsDesc(String stripped) {
        if (stripped == null || stripped.isEmpty()) return false;

        // 1. 硬匹配（无变量描述）
        for (String desc : ENCHANTMENT_DESC_HARD) {
            if (stripped.equals(desc)) return true;
            if (stripped.contains(desc)) return true;
        }

        // 2. 关键词组合匹配（有变量描述）
        if (isKeywordEnchantmentDesc(stripped)) return true;

        return false;
    }

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

    // ========== 原版附魔识别 ==========
    public static boolean isVanillaEnchantment(String stripped) {
        if (stripped == null || stripped.isEmpty()) return false;
        for (String enc : VANILLA_ENCHANTMENTS) {
            if (stripped.contains(enc)) return true;
        }
        return false;
    }

    public static boolean isVanillaEnchantmentDesc(String stripped) {
        if (stripped == null || stripped.isEmpty()) return false;

        // 原版附魔描述关键词
        List<String> vanillaDescKeywords = Arrays.asList(
            "增加物品耐久度", "增加挖掘速度", "减少摔落伤害", "增加",
            "减少", "使对手着火", "箭矢使目标着火", "增加特定方块掉落",
            "在玩家周围", "对海洋生物增加", "射击时普通箭矢不会被消耗",
            "增加攻击击退", "增加最大普通掉落", "三叉戟投掷后会返回",
            "增加钓鱼获得宝藏", "减少钓鱼等待时间", "一次射出",
            "箭矢穿透", "增加箭矢伤害", "减少受到", "增加箭矢击退",
            "减少十字弩装填时间", "延长水下呼吸时间", "在水下或雨天",
            "增加近战伤害", "挖掘的方块会完整掉落", "对亡灵生物增加",
            "在灵魂沙和灵魂土上", "增加横扫攻击伤害", "减少潜行时的移动减缓",
            "给予", "几率将部分受到的伤害反射", "死亡时带有该buff的物品会被销毁",
            "装备的物品无法从护甲槽中移除", "Multiplies upwards knockback",
            "Reduces effectiveness of the target's armor", "Deals bonus damage per block fallen",
            "使玩家在水下挖掘方块的速度", "对节肢生物增加", "减少爆炸伤害",
            "在雷雨天三叉戟落地时引发闪电", "增加水下移动速度",
            "减少火焰伤害", "减少燃烧时间", "增加特定方块掉落的几率",
            "在玩家周围格范围内的水源转化为冰", "对海洋生物增加的额外伤害",
            "增加攻击击退", "增加最大普通掉落", "增加稀有掉落的几率",
            "减少钓鱼等待时间", "在获取经验球时修补", "一次射出3支箭矢",
            "箭矢穿透个实体", "增加箭矢伤害", "减少受到的弹射物伤害",
            "减少受到的伤害", "增加箭矢击退", "减少十字弩装填时间",
            "延长水下呼吸时间", "增加近战伤害", "对亡灵生物增加",
            "在灵魂沙和灵魂土上行走速度", "增加横扫攻击伤害",
            "减少潜行时的移动减缓", "给予的几率将部分受到的伤害反射给攻击者"
        );

        for (String keyword : vanillaDescKeywords) {
            if (stripped.contains(keyword)) return true;
        }

        return false;
    }

    public static boolean isAnyEnchantmentDesc(String stripped) {
        return isExactOrContainsDesc(stripped);
    }
}
