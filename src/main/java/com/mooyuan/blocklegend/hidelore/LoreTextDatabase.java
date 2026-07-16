package com.mooyuan.blocklegend.hidelore;

import java.util.Arrays;
import java.util.List;

public class LoreTextDatabase {

    // ========== 第一层：硬匹配（精确保护，优先级最高）==========
    // 这些文本绝对不会被其他区域误识别
    public static final List<String> EXACT_LORE_LINES = Arrays.asList(
        // 套装效果
        "元素套装: [4] 形成两个能阻挡任何攻击的护盾，护盾会在一段时间后自我恢复。",
        "元素宗师套装: [4] 形成两个能阻挡任何攻击的护盾，护盾会在更短的时间内自我恢复。",
        "冰晶套装: [4] 移动时在身后留下一片冰雾对敌人造成减速效果",
        "幻夜套装: [4] 黑夜时获得夜视和力量II增益效果",
        "曜日套装: [4] 白昼时获得速度II和生命恢复I增益效果",
        "行者套装: [4] 每五秒恢复10点法力值，",
        "荆棘套装: [4]受到伤害时弹开周围敌人，并获得移动速度。",
        "翠影套装: [4] 手持飞镖二段跳(双SHIFT)",
        "森之套装: [5] 快速射击第三次击中目标时减速目标，造成额外伤害，并获得速度提升。",
        "森之疾弦套装: [5] 快速射击第五次击中目标时造成额外伤害，造成额外伤害,并获得速度提升。",
        "翠影龙吟套装: [4] 手持飞镖二段跳(双SHIFT)，飞镖击中实体后获得霸体效果: 伤害吸收III +速度II 持续2.5秒",
        "赤鬼套装: [4] 每次使用 血斩 都会获得叠加的移动速度，最多叠加至速度III。满层时太刀将造成40%额外伤害。",
        "赤鬼将军套装: [4] 每次使用 血斩 都会获得叠加的移动速度，最多叠加至速度IV。满层时太刀将造成60%额外伤害。",
        "幽境翼之套装: [4] 在未受到攻击时，提供32临时生命值受伤后，套装武器伤害提高30%，持续4秒。CD:30秒",
        "辉光套装: [4]获得生命回复II [5]获得生命回复III",
        "龙之套装: [4] 受到伤害时有25%几率对周围敌人造成伤害",
        "圣骑士套装: [4] 每使用一次技能在你周围叠加一个神圣环，每4层叠加会创造一团光辉灵气，治疗并为你自己和附近的盟友提供抗性。",
        "天界圣骑士套装: [4] 每使用一次技能在你周围叠加一个神圣环，每4层叠加会创造一团光辉灵气，治疗并为你自己和附近的盟友提供抗性。",
        "神龙套装: [4] 受到伤害时有25%几率对周围敌人造成伤害",
        "闪耀潮汐套装: 在水中时获得水下呼吸,力量,速度III",
        "烈焰套装: [4]着火时造成额外伤害",
        "雷之套装: [4] 攻击或技能有30%概率召唤一道闪电并击晕敌人",

        // 组合技
        "组合技-烈火焚天斩: Shift+右键+右键",
        "组合技-霜爆: 冰墙+火力全开",
        "组合技-虚空箭雨: 虚空箭头+虫洞跃迁",

        // 工具描述
        "能连锁采伐原木类方块,建筑时谨慎使用！此工具能高效地砍伐树木采集资源",
        "不费吹灰之力将一座山夷为平地此工具可高效率地采集各种矿物",
        "拥有海神力量的钓竿，你现在强的可怕。",
        "这把铲子蕴含着愚公移山的决心每次挖掘都带着无与伦比的效率",
        "Shift+右键切换模式(精准/时运)",

        // 通用 Lore
        "身披元素法袍，就是穿上了大自然的庇护。",
        "梦开始的地方。",
        "/pw zcyf前往专用钓鱼岛屿使用",
        "锅铲是我的武器，食材是我的士兵。我要往你的土豆丝里掺点姜>:P",
        "失落的古代魔具，曾是吸血鬼族至高无上的宝物。如今，它静默于历史尘埃，等待着有缘人唤醒其尘封的力量。",
        "失落的古代魔具，曾是吸血鬼族至高无上的宝物。它能幻化为镰刀斩断一切，秘典赋予魔力，双刃舞动死亡之舞，长矛贯穿灵魂深渊。如今，它静默于历史尘埃，等待着有缘人唤醒其尘封的力量。",
        "似寂灭的火星般，蓄发着燎原之火。心如火焰炽，梦似炬光燃。",
        "如炽热的火焰般，象征着力量与勇气。影随火星动，夜照焱途长。",
        "2025圣诞限定武器",
        "2024圣诞限定武器",
        "挥舞此扇可如秋风扫落叶般扫清敌人",
        "一柄皎洁的寒色利剑，如同霜气凝结而成，闪烁着令人颤栗的锋芒。",
        "由寒铁锻造而成，散发着淡淡的蓝光。未寒心先悸，触雪落霜痕。",
        "凝聚着北境的无尽寒霜，如极星般冷冽耀眼。凛风割面痛，冻骨戍寒疆。",
        "冰蓝色的光芒在枪身上流转，散发着深海的神秘气息。",
        "闪耀着冰蓝色的光芒，枪身环绕着金色的纹饰，仿佛深海之王的宝物,传说中能够召唤海洋的力量。",
        "蒸汽机器! 2024周年活动武器",
        "被遗忘的古老工艺与未来科技的完美结合，在每一次扣动扳机时，都预示着敌人的末日。",
        "一件散发着深邃黑色光芒的胸甲。",
        "一把散发着深邃黑色光芒的剑。",
        "每一天它都会呈现出不同的光彩。",
        "银甲熠熠，金錾辉煌。纳自然于一体，赋英杰以巍峨。",
        "由古老星辰陨落的碎片制成。",
        "穿上它，仿佛能让人融入夜色中，只留下飘忽不定的银色轮廓。",
        "一把似乎从夜幕中锻造出的长剑，刀身微微闪烁着幽蓝的光辉。",
        "采用先进的青棘纤维编织技术，能有效抵御锐器刺穿，同时保持轻便。",
        "林间古老荆棘凝聚而成，在战斗中能唤醒森林的力量。",
        "每一块铁片都闪烁着绿色的光芒。",
        "锋利的边缘闪烁着如同新叶般的翠绿光芒，赋予了这件武器无与伦比的生命力。",
        "其表面覆盖着翠绿色的藤蔓，仿佛森林的一部分被赋予了生命。",
        "弓身以翠绿色的竹子制成，弓背上雕刻着精美的藤蔓纹路。",
        "由翠绿色的藤蔓和闪耀的金属片编织而成。护甲赋予你超凡的射速，每一箭都快若闪电。",
        "疾速的箭矢穿越丛林，犹如闪电撕裂夜空。连弦声都未曾听见，敌人已被定格在死亡的瞬间。",
        "盔甲的材质轻盈无比，让战士们能以风之速度移动，轻松闪避每一次攻击，是敏捷和保护的完美结合。",
        "刀刃闪耀着血色光泽的传奇武器，在月光下闪烁着诡异的红光。",
        "铠甲的光芒在夜战中尤为显眼，上面的每一块甲片都似乎在诉说着一段血腥而英勇的故事。",
        "刀刃上流淌着闪耀的红色光芒，宛如血液一般每一次斩击都在空中留下了燃烧的赤色轨迹。",
        "每次挥舞这把镰刀，都伴随着一道如幻翼般的轻盈影子。",
        "一套融合了深渊与天空的铠甲，如同指引着通往幽境的路。",
        "这把镰刀从最深的幽境提炼而出，翼状的图腾在月光下仿佛能够起舞。",
        "这件护甲不仅坚固，还拥有自己的光芒，穿戴者仿佛成为了战场上的一道光线。",
        "剑刃闪烁着耀眼的光芒，每一次挥舞都如同太阳的光芒穿破黑暗。",
        "由无比坚硬的龙鳞锻造而成的盔甲，",
        "巨龙之骨锻造而成的长矛，上面刻有古老的符文。",
        "由历代最伟大的神匠亲手锻造，不仅防御力极高，更有神圣之力的保佑。",
        "传说中的神圣武器，曾由历代的圣骑士使用。不仅拥有惊人的破坏力，更有坚不可摧的防御能力。",
        "不仅具有无与伦比的防御力，蕴含着天界的神圣力量。穿上它的战士将被视为天界的代表，在凡间受到万众尊敬。",
        "由天界最伟大的神匠亲手打造的武器，它不仅凝聚了天界的神圣力量，还有着无与伦比的防护能力。",
        "一把由神龙武士亲手打造的盔甲，它是他忠诚与勇敢的象征。",
        "一把由神龙武士亲手打造的长矛，它是他忠诚与勇敢的象征。",
        "古老的力量在这根法杖中涌动，象征着火、水、雷和风的和谐统一。",
        "这顶帽子充满了大自然的力量，赋予你对元素的理解与掌控。",
        "这条下摆是由元素精华编织而成，它将在你的旅途中保护你。",
        "像是一双可以任意控制元素的手，让你在战斗中总是步步为营。",
        "你将成为风暴的主宰，火焰的领主，雷电的守护者，以及空气的旋律者。",
        "诅咒骷髅骨打造而成的头盔",
        "诅咒骷髅骨打造而成的胸甲",
        "诅咒骷髅骨打造而成的护腿",
        "诅咒骷髅骨打造而成的靴子",
        "诅咒骷髅骨打造而成的剑",
        "诅咒骷髅骨打造而成的弓",
        "诅咒骷髅骨打造而成的斧头",
        "注入了梦魇之魂的合金盔甲",
        "注入了梦魇之魂的合金剑",
        "注入了梦魇之魂的合金弓",
        "注入了梦魇之魂的合金斧",
        "来自北方极寒之地的头盔",
        "来自北方极寒之地的胸甲",
        "来自北方极寒之地的护腿",
        "来自北方极寒之地的靴子",
        "来自北方极寒之地的宝剑",
        "来自北方极寒之地的弓箭",
        "来自北方极寒之地的斧头",
        "由坚硬无比的暴风合金制成",
        "由暗黑凋零骨打造而成",
        "来自远古海洋深处的头盔",
        "来自远古海洋深处的胸甲",
        "来自远古海洋深处的护腿",
        "来自远古海洋深处的靴子",
        "燃烧着地狱烈火的头盔",
        "燃烧着地狱烈火的胸甲",
        "燃烧着地狱烈火的护腿",
        "燃烧着地狱烈火的靴子",
        "燃烧着地狱烈火的镰刀",
        "蕴含着雷之力量的头盔",
        "蕴含着雷之力量的胸甲",
        "蕴含着雷之力量的护腿",
        "蕴含着雷之力量的靴子",
        "蕴含着雷之力量的战锤",
        "雷神索尔所用的头盔",
        "雷神索尔所用的胸甲",
        "雷神索尔所用的护腿",
        "雷神索尔所用的靴子",
        "雷神索尔所使用的武器",
        "古老的寒冰法师所用的胸甲",
        "古老的寒冰法师所用的护腿",
        "古老的寒冰法师所用的靴子",
        "古老的寒冰法师所用的武器 使用者可造成成吨的技能伤害",
        "古老的寒冰法师元老所用的头盔",
        "古老的寒冰法师元老所用的胸甲",
        "古老的寒冰法师元老所用的护腿",
        "古老的寒冰法师元老所用的武器 使用者可造成成吨的技能伤害",
        "一件由虚空能量锻造的头盔，刻有神秘的符文，闪烁着紫色的光芒。",
        "一件由虚空能量锻造的胸甲，刻有神秘的符文，闪烁着紫色的光芒。",
        "一件由虚空能量锻造的护腿，刻有神秘的符文，闪烁着紫色的光芒。",
        "一件由虚空能量锻造的靴子，刻有神秘的符文，闪烁着紫色的光芒。",
        "一把由虚空能量锻造的弓,刻有古老的符文。能够发射出无形的箭矢，穿透任何物质。",
        "一件由虚空能量和闪冥精粹锻造的头盔，刻有神秘的符文，闪烁着金紫色的光芒。",
        "一件由虚空能量和闪冥精粹锻造的胸甲，刻有神秘的符文，闪烁着金紫色的光芒。",
        "一件由虚空能量和闪冥精粹锻造的护腿，刻有神秘的符文，闪烁着金紫色的光芒。",
        "一件由虚空能量和闪冥精粹锻造的靴子，刻有神秘的符文，闪烁着金紫色的光芒。",
        "一把由虚空能量锻造的弓,刻有古老的符文。能够发射出无形的箭矢，穿透任何物质。",
        "上面刻有契约的符文，覆盖着黑色的阴影佩戴者与一种神秘的力量签订了契约。",
        "上面刻有契约的符文，闪耀着金色的光芒。佩戴者可以感受到天使之神的庇护。",
        "这本神秘的书籍上刻着各种奇异的符文，使用者与书中的多个幻兽签订了契约。",
        "蒸汽动力工坊的最新产品 \"汉总是先开枪...\"",
        "无形之刃，最为致命。\"无知者在劫难逃！\"",
        "链剑蜿蜒若荆棘,剑风起时蛇影舞,吞吐如风。持剑者踏风而舞,链刃如蛇缠敌,剑风所至,身形飘忽来去无踪。",
        "\"破晓的誓约如十字星光般永恒，\"\"纵使长夜荆棘，亦为彷徨者刻印救赎的轨迹。\"\"",
        "\"黎明前的誓言永不褪色，\"\"正如晨星指引迷途者穿越长夜。\"",
        "初夏的第一扇绿 红色的碎片四下飞溅,飘落满地残红",
        "初夏的第一扇绿",
        "2026新春限定·神鬼开天巨斧 残胚尽蜕，镇界成锋；执之者可断岳开天，震慑万灵。",
        "2026新春限定·开天遗物 战神血魄浸染，混沌金光缠刃；一斧落下，山河俱裂。",
        "探险家深入古代遗迹，于白雾弥漫处拾得双骰与牌。暗藏玄机的赌具，似乎有着不可思议的力量。\"梭哈是一种智慧!\" 2025赌神活动限定",
        "探险家深入古代遗迹，于白雾弥漫处拾得双骰与牌。\"梭哈，优势在我!\" 2025赌神活动限定",
        "蕴含着末影龙体内强大的魔法之力"
    );

    // ========== 第二层：关键词匹配（兜底识别）==========
    public static final List<String> LORE_KEYWORDS = Arrays.asList(
        "套装:", "套装：", "通过 铁匠锻造 获得", "通过 铁匠锻造 获取", 
        "通过 铁匠锻造 获得。", "组合技-", "切换模式", "双SHIFT",
        "由", "来自", "注入", "蕴含", "燃烧", "诅咒", "古老的", "失落的",
        "凝聚", "闪耀", "挥舞", "身披", "穿上", "佩戴", "使用", "持有"
    );

    public static final List<String> LORE_ENDINGS = Arrays.asList(
        "通过 铁匠锻造 获得", "通过 铁匠锻造 获取", "通过 铁匠锻造 获得。"
    );
    public static final List<String> SET_KEYWORDS = Arrays.asList("套装:", "套装：");
    public static final List<String> SET_EFFECT_PREFIXES = Arrays.asList("[4]", "[5]");
    public static final List<String> SEPARATOR_LINES = Arrays.asList(
        "======================================", "--------------------",
        "§m======================================", "§m--------------------",
        "§7§m======================================", "§7§m--------------------",
        "§8§m======================================", "§8§m--------------------",
        "§f§m======================================", "§f§m--------------------",
        "§x§6§6§6§6§6§6§m======================================", "§x§6§6§6§6§6§6§m--------------------"
    );
    public static final List<String> TOOL_LORE_TEXTS = Arrays.asList(
        "能连锁采伐原木类方块,建筑时谨慎使用！",
        "此工具能高效地砍伐树木采集资源",
        "不费吹灰之力将一座山夷为平地",
        "此工具可高效率地采集各种矿物",
        "拥有海神力量的钓竿，",
        "你现在强的可怕。",
        "这把铲子蕴含着愚公移山的决心",
        "每次挖掘都带着无与伦比的效率",
        "Shift+右键切换模式(精准/时运)"
    );
    public static final List<String> PROTECTED_KEYWORDS = Arrays.asList(
        "切换模式", "组合技", "Shift+右键+右键", "双SHIFT"
    );
    public static final List<String> EQUIPMENT_TYPES = Arrays.asList(
        "头盔", "胸甲", "护腿", "靴子", "剑", "弓", "斧", "斧头", "镰刀", "长矛",
        "法杖", "法袍", "帽子", "手套", "鞋子", "武器", "盔甲", "铠甲", "战锤"
    );
    public static final List<String> VARIABLE_PREFIXES = Arrays.asList(
        "由", "来自", "注入", "蕴含", "燃烧", "诅咒", "古老的", "失落的",
        "凝聚", "闪耀", "挥舞", "身披", "穿上", "佩戴", "使用", "持有"
    );

    // ========== 识别方法 ==========

    public static boolean isExactLore(String stripped) {
        if (stripped == null || stripped.isEmpty()) return false;
        for (String exact : EXACT_LORE_LINES) {
            if (stripped.equals(exact)) return true;
        }
        return false;
    }

    public static boolean isKeywordLore(String stripped) {
        if (stripped == null || stripped.isEmpty()) return false;
        for (String keyword : LORE_KEYWORDS) {
            if (stripped.contains(keyword)) return true;
        }
        return false;
    }

    public static boolean isLoreEnding(String stripped) {
        if (stripped == null || stripped.isEmpty()) return false;
        for (String ending : LORE_ENDINGS) {
            if (stripped.contains(ending)) return true;
        }
        return false;
    }

    public static boolean isSetKeyword(String stripped) {
        if (stripped == null || stripped.isEmpty()) return false;
        for (String keyword : SET_KEYWORDS) {
            if (stripped.contains(keyword)) return true;
        }
        return false;
    }

    public static boolean isSetEffect(String stripped) {
        if (stripped == null || stripped.isEmpty()) return false;
        for (String prefix : SET_EFFECT_PREFIXES) {
            if (stripped.startsWith(prefix)) return true;
        }
        return false;
    }

    public static boolean isSeparator(String stripped) {
        if (stripped == null || stripped.isEmpty()) return false;
        for (String sep : SEPARATOR_LINES) {
            if (stripped.contains(sep)) return true;
        }
        if (stripped.contains("======")) return true;
        if (stripped.contains("------")) return true;
        return false;
    }

    public static boolean isToolLore(String stripped) {
        if (stripped == null || stripped.isEmpty()) return false;
        for (String text : TOOL_LORE_TEXTS) {
            if (stripped.contains(text)) return true;
        }
        return false;
    }

    public static boolean isProtectedKeyword(String stripped) {
        if (stripped == null || stripped.isEmpty()) return false;
        for (String keyword : PROTECTED_KEYWORDS) {
            if (stripped.contains(keyword)) return true;
        }
        return false;
    }

    public static boolean isVariableTemplate(String stripped) {
        if (stripped == null || stripped.isEmpty()) return false;
        boolean hasPrefix = false;
        for (String prefix : VARIABLE_PREFIXES) {
            if (stripped.contains(prefix)) { hasPrefix = true; break; }
        }
        if (!hasPrefix) return false;
        for (String type : EQUIPMENT_TYPES) {
            if (stripped.contains(type)) return true;
        }
        return false;
    }

    public static boolean isLoreText(String stripped) {
        if (stripped == null || stripped.isEmpty()) return false;
        if (isExactLore(stripped)) return true;
        if (isLoreEnding(stripped)) return true;
        if (isSetKeyword(stripped)) return true;
        if (isSetEffect(stripped)) return true;
        if (isSeparator(stripped)) return true;
        if (isToolLore(stripped)) return true;
        if (isVariableTemplate(stripped)) return true;
        return false;
    }
}
