# BlockLegend Hide Lore Tooltips

> 在方块传说服务器中自定义隐藏物品的信息显示  
> Customizing the information display of hidden items in the BlockLegends Server.

---

## 基本信息

| 项目 | 内容 |
|------|------|
| 模组名称 | BlockLegend Hide Lore Tooltips |
| 内部ID | `blocklegend_hidelore` |
| 版本 | **v1.1.0** |
| Minecraft版本 | 1.20.1 (Fabric) |
| 作者 | Moyuans |
| GitHub | [https://github.com/Moo-yuan/BlockLegend-Hide-Lore-Tooltips](https://github.com/Moo-yuan/BlockLegend-Hide-Lore-Tooltips) |
| 开源协议 | GPL-3.0 |

---

## 文件结构

```
BlockLegend-Hide-Lore-Tooltips/
├── build.gradle                          # Gradle 构建配置
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar            # Gradle Wrapper
│       └── gradle-wrapper.properties     # Gradle 8.8 配置
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/mooyuan/blocklegend/hidelore/
│   │   │       ├── HideLoreMod.java          # 主类：Tooltip 处理核心逻辑
│   │   │       ├── ModConfig.java            # 配置类：AutoConfig 配置定义
│   │   │       ├── KeyOption.java            # 按键枚举：4 种可选按键
│   │   │       ├── EnchantmentDatabase.java  # 附魔数据库：304 个附魔识别
│   │   │       ├── ElementDatabase.java      # 元素数据库：6 种元素 × 伤害/防御
│   │   │       ├── SkillDatabase.java        # 技能数据库：触发词 + 冷却词 + 保护词
│   │   │       ├── AttributeDatabase.java    # 属性数据库：16 属性 + 8 RPG 前缀
│   │   │       ├── WeaponTypeDatabase.java   # 武器类型数据库：星级/等级/类型
│   │   │       ├── ToolTypeDatabase.java     # 工具类型数据库：5 种工具识别
│   │   │       ├── LoreTextDatabase.java     # 文本数据库：149+ Lore 文本识别
│   │   │       └── integration/
│   │   │           └── ModMenuIntegration.java   # ModMenu 集成
│   │   └── resources/
│   │       ├── fabric.mod.json             # 模组元数据
│   │       └── assets/blocklegend_hidelore/
│   │           ├── icon.png                # 128×128 图标
│   │           └── lang/
│   │               └── zh_cn.json          # 汉化文件
│   └── test/
└── README.md
```

---

## 依赖版本

| 依赖 | 版本 |
|------|------|
| Minecraft | 1.20.1 |
| Yarn Mappings | 1.20.1+build.10 |
| Fabric Loader | 0.14.22 |
| Fabric API | 0.89.0+1.20.1 |
| Cloth Config | 11.1.118 |
| ModMenu | 7.2.2 |
| fabric-loom | 1.6-SNAPSHOT |
| Gradle | 8.8 |

---

## 功能概述

本模组是一个客户端 Fabric 模组，用于在 BlockLegend RPG 服务器中自定义隐藏物品的 Lore / NBT 提示信息。

### 核心功能

| 功能 | 说明 |
|------|------|
| 隐藏附魔区 | 隐藏原版 + 插件附魔名称和描述 |
| 隐藏属性区 | 隐藏攻击伤害 / 速度 / 暴击 / 技能伤害等 RPG 属性 |
| 隐藏技能组 | 隐藏全部技能（技能名 + 冷却 / 持续） |
| 隐藏符文 / 宝石槽 | 隐藏空槽位或全部槽位 |
| 隐藏底部信息 | 隐藏耐久度 / 已绑定 / 击杀人数 / 无法破坏 |
| 原版属性移除 | 隐藏纹饰 / 工具属性 / 护甲属性 |
| 材料保护 | Paper / Feather / Stick 不受隐藏规则影响 |
| 自定义按键展开 | 按住按键展开隐藏信息（默认 Caps Lock） |
| **v1.1 新增：关键词白名单** | 商店界面物品不隐藏任何 lore |
| **v1.1 新增：物品 ID 白名单** | 通过物品英文 ID 禁用隐藏 |

---

## 8 分区识别系统

物品 Lore 从上到下分为 8 个区域，按优先级识别：

| 优先级 | 区域 | 说明 |
|--------|------|------|
| 0 | 名称行 | 第 0 行，固定保留 |
| 1 | 附魔区 | 附魔名称 + 附魔描述 |
| 2 | 类型 2 区 | 星级枩 + 等级 Lv. + 武器类型 |
| 3 | 属性区 | RPG 前缀 + 属性关键词 |
| 4 | 元素区 | 元素前缀 + 元素类型 + 伤害 / 防御 |
| 5 | 技能区 | 触发词 + 技能名 + 冷却 / 持续 |
| 6 | 符文 / 宝石区 | 空槽 / 已镶嵌 |
| 7 | Lore 文本区 | 渐变文本 + 套装信息 |
| 8 | 底部区 | 耐久 / 绑定 / 击杀 / 无法破坏 |

---

## 配置系统

配置框架：AutoConfig + Cloth Config + Jankson  
配置文件路径：`.minecraft/config/blocklegend_hidelore.json`

### 配置项列表（23 个）

#### 【Lore 修改页】

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| `enabled` | `true` | 启用模组（总开关） |
| `hideEnchantments` | `false` | 隐藏附魔区（存在 BUG，暂不建议使用） |
| `hideAttributes` | `false` | 隐藏属性（攻击伤害 / 速度 / 暴击等） |
| `hideSkills` | `true` | 隐藏技能组（技能名 + 冷却 / 持续） |
| `hideEmptySlots` | `false` | 隐藏空符文 / 宝石槽 |
| `hideAllSlots` | `false` | 隐藏全部符文 / 宝石槽 |
| `hideDurability` | `false` | 隐藏耐久度 |
| `hideBoundStatus` | `false` | 隐藏已绑定 |
| `hideKillCount` | `false` | 隐藏击杀人数 |
| `hideUnbreakable` | `false` | 隐藏无法破坏 |
| `unbreakableOverridesDurability` | `false` | 无法破坏替代耐久 |
| `hintText` | `[按 {key} 查看 {count} 条隐藏信息]` | 提示文字（支持 `{key}` 和 `{count}` 变量） |
| `separatorLine` | `--------------------` | 分隔线（展开模式显示） |

#### 【原版修改页】

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| `customKey` | `CAPS_LOCK` | 自定义展开按键（Caps Lock / Left Ctrl / Left Alt / Right Shift） |
| `hideArmorTrim` | `false` | 隐藏纹饰 |
| `hideVanillaToolStats` | `false` | 隐藏原版工具属性（**在主手时** / 攻击伤害 / 攻击速度） |
| `hideVanillaArmorStats` | `false` | 隐藏原版护甲属性（戴在头上时 / 护甲值 / 盔甲韧性 / 击退抗性） |
| `protectEnchantedBook` | `true` | 保护附魔书（不受任何隐藏规则影响） |
| `protectPaper` | `true` | 保护材料（Paper / Feather 只保留绑定信息） |
| `boldUnbreakable` | `true` | 无法破坏加粗 |

#### 【v1.1 新增：物品 ID 白名单】

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| `itemIdProtection.enabled` | `true` | 启用物品 ID 白名单 |
| `itemIdProtection.protectedIds` | `["minecraft:paper", "minecraft:feather"]` | 受保护的物品 ID 列表（支持完整 ID 或部分匹配） |

---

## 白名单关键词（v1.1 新增）

以下关键词出现在物品 Lore 中时，**该物品完全不隐藏任何 lore**（全局生效）：

| 关键词 | 典型场景 |
|--------|----------|
| `购买` | 商店购买按钮 `(shift+左键)购买` |
| `领取` | 奖励领取 `左键 点击领取` |
| `预览` | 物品预览 `右键 点击预览` |
| `评分` | 评分 / 收藏 `[右键] 评分/收藏` |
| `收藏` | 评分 / 收藏 `[右键] 评分/收藏` |
| `升级至` | VIP / SVIP 升级 `升级至 SVIP` |
| `右键查看` | 礼包查看 `豪华周礼包 (右键查看)` |

> **注意：** 白名单只保护**商店 / 交互界面**的物品，购买后的物品（无关键词）正常执行隐藏规则。

---

## 自定义按键功能

### 实现方式
- 不注册 Fabric KeyBinding（避免显示在原版按键设置中）
- 纯配置项控制，通过 ModMenu 配置界面下拉列表选择
- 使用 GLFW 直接检测按键状态

### 可选按键

| 按键 | 键码 |
|------|------|
| Caps Lock（默认） | 280 |
| Left Ctrl | 341 |
| Left Alt | 342 |
| Right Shift | 344 |

> Left Shift 已移除（存在冲突 BUG）

---

## 构建说明

### 环境要求
- Java 17+
- Gradle 8.8（腾讯云镜像）

### 构建命令
```bash
./gradlew build
```

### 构建输出
```
build/libs/BlockLegend-Hide-Lore-Tooltips-1.1.0.jar
```

### IDEA 设置
- Gradle JVM: Java 21
- Build and run using: Gradle

---

## 更新日志

### v1.1.0（当前版本）

**新增功能：**
- ✅ **关键词白名单**：商店界面物品（含 `购买` / `领取` / `预览` 等关键词）不隐藏任何 lore
- ✅ **物品 ID 白名单**：通过 ModMenu 配置添加物品英文 ID，禁用该物品的隐藏规则
- ✅ 构建系统升级：fabric-loom 1.6-SNAPSHOT + Gradle 8.8

**修复问题：**
- 🔧 修复 `archivesName` 在 Gradle 8.8 中的兼容性问题
- 🔧 修复 ModMenu 依赖下载失败问题（改用 modrinth maven）

**白名单关键词变更：**
- 初始：购买、领取、预览、评分、收藏、升级至、右键查看、**点击**、**切换**
- v1.1 最终：购买、领取、预览、评分、收藏、升级至、右键查看
- 移除 `点击` 和 `切换`（避免购买后物品误判为商店物品）

### v1.0.0

- 初始版本
- 8 分区识别系统（附魔 / 属性 / 技能 / 符文 / 宝石 / Lore / 元素 / 武器类型）
- 自定义按键展开隐藏信息
- ModMenu 配置界面
- 双层 Lore 文本数据库（硬匹配 + 关键词）
- 工具保护机制（下界合金工具 / 钓鱼竿）

---

## 已知问题

1. **隐藏附魔功能存在 BUG** — 插件附魔描述识别不准确，建议关闭此功能
2. **Left Shift 按键冲突** — 已移除该选项

---

## 致谢

- [Fabric Team](https://fabricmc.net/) — 模组加载框架
- [Cloth Config](https://shedaniel.me/) — 配置界面
- [ModMenu](https://modrinth.com/mod/modmenu) — 模组菜单

---

*本文档最后更新于 2026.06.24*
