========================================
BlockLegend Hide Lore Tooltips - 构建包 v1.1
========================================

【构建命令】

Windows:
    gradlew.bat build

Linux/Mac:
    ./gradlew build

（如果 gradlew 没有执行权限，先运行: chmod +x gradlew）

【重要：需要 gradle-wrapper.jar】

构建包缺少 gradle-wrapper.jar，需要自行获取：

方案 1（推荐）：从其他 Fabric 项目复制
    复制以下文件到本目录：
    - gradlew
    - gradlew.bat
    - gradle/wrapper/gradle-wrapper.jar
    - gradle/wrapper/gradle-wrapper.properties

方案 2：使用系统 Gradle
    1. 安装 Gradle 8.5
    2. 运行: gradle wrapper --gradle-version 8.5
    3. 再运行: ./gradlew build

方案 3：手动下载
    下载: https://services.gradle.org/distributions/gradle-8.5-bin.zip
    解压后使用 bin/gradle 命令

【构建输出】
    build/libs/BlockLegend-Hide-Lore-Tooltips-1.0.0.jar

【前置步骤】
1. 确保所有 .java 源码文件已在 src/main/java/ 对应目录
2. 将 icon.png (128x128) 放入 src/main/resources/assets/blocklegend_hidelore/
3. 确保 gradle-wrapper.jar 存在

========================================
