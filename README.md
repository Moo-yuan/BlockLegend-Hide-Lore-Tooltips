# BlockLegend Hide Lore Tooltips

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.20.1-green.svg)](https://www.minecraft.net/)
[![Fabric Loader](https://img.shields.io/badge/Fabric-0.14.22+-blue.svg)](https://fabricmc.net/)
[![License](https://img.shields.io/badge/License-GPL--3.0-orange.svg)](LICENSE)

在方块传说服务器中自定义隐藏物品的信息显示。

Customizing the information display of hidden items in the BlockLegends Server.

## 作者 / Author

- **Moyuans**

## 功能 / Features

### 隐藏区域
- **附魔区** - 隐藏原版附魔名称和描述（存在BUG，暂不建议使用）
- **属性区** - 隐藏攻击伤害/速度/暴击/技能伤害等属性
- **技能区** - 隐藏全部技能（技能名+冷却/持续）
- **符文/宝石槽** - 隐藏空槽位或全部槽位
- **底层信息** - 隐藏耐久度/已绑定/击杀人数/无法破坏

### 特殊保护
- **工具保护** - 下界合金镐/铲/锄/斧、钓鱼竿只启用底层隐藏
- **附魔书保护** - 附魔书不受隐藏规则影响
- **材料保护** - Paper/Feather材料只保留绑定信息

### 交互
- **自定义按键** - 按住按键展开隐藏信息（默认 Caps Lock）
- **提示文本** - 精简模式显示隐藏行数提示
- **分隔线** - 展开隐藏内容时的分隔线样式

## 配置 / Configuration

通过 ModMenu 打开配置界面：

```
设置 → 模组 → BlockLegend Hide Lore Tooltips → 配置
```

### 配置项

| 配置项 | 说明 | 默认值 |
|---|---|---|
| 启用模组 | 总开关 | 是 |
| 隐藏附魔区 | 隐藏原版附魔（存在BUG） | 否 |
| 隐藏属性 | 隐藏属性行 | 否 |
| 隐藏技能组 | 隐藏技能区域 | 是 |
| 隐藏空符文/宝石槽 | 仅隐藏空槽位 | 否 |
| 隐藏全部符文/宝石槽 | 隐藏所有槽位 | 否 |
| 隐藏耐久度 | 隐藏耐久显示 | 否 |
| 隐藏已绑定 | 隐藏绑定信息 | 否 |
| 隐藏击杀人数 | 隐藏击杀统计 | 否 |
| 隐藏无法破坏 | 隐藏无法破坏标签 | 否 |
| 无法破坏替代耐久 | 有无法破坏时隐藏耐久 | 否 |
| 自定义展开按键 | 展开隐藏信息的按键 | Caps Lock |
| 提示文字 | 精简模式提示文本 | [按 {key} 查看 {count} 条隐藏信息] |
| 分隔线 | 展开时的分隔线 | -------------------- |

## 依赖 / Dependencies

- Fabric API
- Cloth Config API
- ModMenu（可选）

## 安装 / Installation

1. 安装 [Fabric Loader](https://fabricmc.net/use/)
2. 安装 [Fabric API](https://modrinth.com/mod/fabric-api)
3. 安装 [Cloth Config API](https://modrinth.com/mod/cloth-config)
4. 将本模组放入 `mods` 文件夹

## 构建 / Building

```bash
./gradlew build
```

构建输出位于 `build/libs/`。

## 协议 / License

本项目采用 [GPL-3.0](LICENSE) 协议开源。

## 链接 / Links

- GitHub: https://github.com/Moo-yuan/BlockLegend-Hide-Lore-Tooltips
- Issues: https://github.com/Moo-yuan/BlockLegend-Hide-Lore-Tooltips/issues

## 更新日志 / Changelog

### v1.0.0
- 初始版本
- 8分区识别系统（附魔/属性/技能/符文/宝石/Lore/元素/武器类型）
- 自定义按键展开隐藏信息
- ModMenu 配置界面
- 双层 Lore 文本数据库（硬匹配+关键词）
- 工具保护机制（下界合金工具/钓鱼竿）

## 已知问题 / Known Issues

1. **隐藏附魔功能存在BUG** - 插件附魔描述识别不准确，建议关闭此功能
2. **Left Shift 按键冲突** - 已移除该选项

## 致谢 / Credits

- [Fabric Team](https://fabricmc.net/) - 模组加载框架
- [Cloth Config](https://github.com/shedaniel/cloth-config) - 配置界面
- [ModMenu](https://github.com/TerraformersMC/ModMenu) - 模组菜单
