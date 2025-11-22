# TikTok 风格内容聚合应用

一个仿 TikTok/小红书风格的内容发现平台 Android 应用，采用双列瀑布流布局，支持下拉刷新和上拉加载更多功能。

## 📱 项目简介

本项目是一个内容聚合类 Android 应用，实现了类似 TikTok 和小红书的内容展示界面。主要特点包括：

- **双列瀑布流布局**：支持动态高度适配的内容卡片
- **下拉刷新**：自定义实现的下拉刷新功能
- **上拉加载更多**：自动检测滚动到底部并加载更多内容
- **点赞交互**：支持点击切换点赞状态，实时更新点赞计数
- **数据配置管理**：支持 Mock 数据模拟和网络图片资源配置

## ✨ 功能特性

### 核心功能

1. **瀑布流布局**
   - 双列网格布局，支持动态高度
   - 使用 `StaggeredGridLayoutManager` 实现
   - 内容卡片根据图片和文本自动调整高度

2. **下拉刷新**
   - 自定义下拉刷新实现（不依赖 SwipeRefreshLayout）
   - 下拉手势检测和视觉反馈
   - 刷新头部动画效果
   - 刷新状态提示

3. **上拉加载更多**
   - 自动检测滚动到底部
   - 显示加载更多指示器
   - 分页加载数据
   - 数据加载状态管理

4. **点赞功能**
   - 点击点赞图标切换点赞状态
   - 实时更新点赞计数
   - 点赞状态视觉反馈（填充/轮廓图标）
   - 点赞数格式化显示（K/W 单位）

5. **数据管理**
   - Mock 数据生成器
   - 数据配置管理
   - 网络图片 URL 配置
   - 数据缓存机制

### UI 组件

- **顶部导航栏**：标签页切换、搜索栏、通知徽章
- **内容卡片**：图片、标题、用户头像、用户名、点赞数
- **底部导航栏**：首页、朋友、添加、消息、我
- **刷新头部**：下拉刷新指示器
- **加载更多**：底部加载指示器

## 🛠️ 技术栈

- **开发语言**：Java
- **最低 SDK 版本**：24 (Android 7.0)
- **目标 SDK 版本**：36
- **编译 SDK 版本**：36
- **主要依赖库**：
  - `androidx.appcompat:appcompat` - AppCompat 支持库
  - `com.google.android.material:material` - Material Design 组件
  - `androidx.activity:activity` - Activity 支持库
  - `androidx.constraintlayout:constraintlayout` - ConstraintLayout 布局
  - `androidx.recyclerview:recyclerview` - RecyclerView（通过 Material 库间接包含）

## 📁 项目结构

```
app/src/main/
├── java/com/example/myapplication/
│   ├── MainActivity.java              # 主Activity，包含UI逻辑
│   ├── ContentItem.java               # 内容数据模型
│   ├── ContentAdapter.java            # RecyclerView适配器
│   ├── DataConfig.java                # 数据配置管理类
│   ├── MockDataGenerator.java         # Mock数据生成器
│   └── DataManager.java               # 数据管理器（单例）
│
└── res/
    ├── layout/
    │   ├── activity_main.xml          # 主界面布局
    │   ├── item_content_card.xml      # 内容卡片布局
    │   ├── item_loading_more.xml      # 加载更多布局
    │   └── view_refresh_header.xml    # 刷新头部布局
    │
    ├── drawable/
    │   ├── ic_*.xml                   # 图标资源
    │   ├── content_card_background.xml
    │   ├── profile_*.xml
    │   └── ...
    │
    └── values/
        ├── colors.xml                 # 颜色资源
        ├── dimens.xml                 # 尺寸资源
        ├── strings.xml                # 字符串资源
        └── themes.xml                 # 主题资源
```

## 🚀 快速开始

### 环境要求

- Android Studio Hedgehog | 2023.1.1 或更高版本
- JDK 11 或更高版本
- Android SDK 24 或更高版本
- Gradle 8.13.0

### 安装步骤

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd ticktok
   ```

2. **打开项目**
   - 使用 Android Studio 打开项目
   - 等待 Gradle 同步完成

3. **运行项目**
   - 连接 Android 设备或启动模拟器
   - 点击运行按钮或使用快捷键 `Shift + F10`
   - 应用将自动安装并启动

### 构建配置

项目使用 Gradle Version Catalog 管理依赖，配置文件位于：
- `gradle/libs.versions.toml` - 版本和依赖配置
- `app/build.gradle` - 应用构建配置
- `build.gradle` - 项目级构建配置

## 📖 使用说明

### 基本操作

1. **浏览内容**
   - 应用启动后自动加载初始内容
   - 内容以双列瀑布流形式展示
   - 每个卡片显示图片、标题、用户信息和点赞数

2. **下拉刷新**
   - 在列表顶部向下滑动
   - 下拉超过阈值（100px）后显示"释放刷新"
   - 释放后自动刷新，获取最新内容

3. **上拉加载更多**
   - 滚动到列表底部
   - 自动检测并加载更多内容
   - 显示加载更多指示器

4. **点赞操作**
   - 点击内容卡片上的点赞图标
   - 切换点赞状态（已点赞/未点赞）
   - 点赞数实时更新

5. **标签切换**
   - 点击顶部导航栏的标签
   - 切换不同内容分类（精选、经验、直播等）

### 数据配置

#### Mock 数据配置

Mock 数据配置在 `DataConfig.java` 中：

```java
// 修改标题列表
private static final String[] MOCK_TITLES = {
    "标题1",
    "标题2",
    // ...
};

// 修改用户名列表
private static final String[] MOCK_USERNAMES = {
    "用户名1",
    "用户名2",
    // ...
};
```

#### 网络图片配置

网络图片使用 picsum.photos 占位图片服务：

```java
// 修改图片基础URL
private static final String IMAGE_BASE_URL = "https://picsum.photos/";

// 生成图片URL
String imageUrl = DataConfig.getImageUrl(width, height, seed);
```

#### 数据生成

通过 `MockDataGenerator` 生成数据：

```java
// 生成初始数据
List<ContentItem> data = MockDataGenerator.generateInitialData(count);

// 生成刷新数据
List<ContentItem> refreshData = MockDataGenerator.generateRefreshData(count);

// 生成加载更多数据
List<ContentItem> moreData = MockDataGenerator.generateLoadMoreData(page, pageSize);
```

## 🔧 配置说明

### 颜色配置

颜色资源在 `res/values/colors.xml` 中定义：

- `background_dark` - 主背景色 (#FF1A1A1A)
- `surface_dark` - 卡片背景色 (#FF2C2C2C)
- `text_primary` - 主要文字颜色 (#FFFFFFFF)
- `text_secondary` - 次要文字颜色 (#FFB3B3B3)
- `accent_red` - 强调色/点赞色 (#FFFF0000)

### 尺寸配置

尺寸资源在 `res/values/dimens.xml` 中定义：

- 卡片边距、圆角半径
- 文字大小
- 间距大小
- 图标尺寸

### 下拉刷新配置

在 `MainActivity.java` 中配置：

```java
private static final float PULL_THRESHOLD = 100f;      // 下拉阈值（px）
private static final float MAX_PULL_DISTANCE = 150f;   // 最大下拉距离（px）
```

## 🏗️ 架构设计

### 数据层

- **ContentItem**：数据模型，包含标题、内容、用户信息、图片URL等
- **DataConfig**：数据配置类，管理Mock数据配置和图片URL配置
- **MockDataGenerator**：Mock数据生成器，生成各种类型的数据
- **DataManager**：数据管理器（单例），统一管理数据获取、更新和缓存

### UI层

- **MainActivity**：主Activity，管理UI逻辑和用户交互
- **ContentAdapter**：RecyclerView适配器，管理内容列表显示
- **布局文件**：XML布局文件，定义UI结构

### 功能模块

1. **瀑布流布局模块**
   - 使用 `StaggeredGridLayoutManager`
   - 动态高度计算和适配

2. **下拉刷新模块**
   - 触摸事件检测
   - 刷新头部动画
   - 数据刷新逻辑

3. **上拉加载模块**
   - 滚动监听
   - 加载更多逻辑
   - 加载状态管理

4. **点赞交互模块**
   - 点击事件处理
   - 状态更新
   - UI反馈

## 📝 开发说明

### 添加新功能

1. **添加新的数据字段**
   - 在 `ContentItem.java` 中添加字段和getter/setter
   - 更新构造函数
   - 在适配器中绑定数据

2. **修改布局**
   - 编辑对应的XML布局文件
   - 更新适配器中的视图绑定

3. **添加新的Mock数据**
   - 在 `DataConfig.java` 中添加数据配置
   - 在 `MockDataGenerator.java` 中添加生成逻辑

### 扩展网络图片加载

当前项目使用占位符显示图片。要加载真实网络图片，需要：

1. **添加图片加载库**（如 Glide）：
   ```gradle
   // 在 app/build.gradle 中添加
   implementation 'com.github.bumptech.glide:glide:4.16.0'
   ```

2. **在适配器中加载图片**：
   ```java
   // 在 ContentAdapter.onBindViewHolder() 中
   if (item.getImageUrl() != null) {
       Glide.with(holder.itemView.getContext())
           .load(item.getImageUrl())
           .placeholder(R.color.divider)
           .into(holder.contentImage);
   }
   ```

### 切换为真实API

1. **创建API接口**：
   ```java
   public interface ApiService {
       @GET("api/content")
       Call<List<ContentItem>> getContentList();
   }
   ```

2. **在DataManager中调用API**：
   ```java
   public List<ContentItem> getInitialData() {
       // 调用API获取数据
       // 替换Mock数据生成
   }
   ```

## 🐛 已知问题

1. **网络图片加载**：当前使用占位符，需要添加图片加载库才能显示真实图片
2. **下拉刷新**：在某些设备上可能需要调整阈值
3. **数据持久化**：当前数据不持久化，应用重启后重置

## 🔮 未来计划

- [ ] 集成图片加载库（Glide/Picasso）
- [ ] 添加数据持久化（Room数据库）
- [ ] 实现真实API接口
- [ ] 添加内容详情页
- [ ] 优化性能和内存使用
- [ ] 添加单元测试和UI测试
- [ ] 支持暗黑模式切换
- [ ] 添加更多交互功能（评论、分享等）

## 📄 许可证

本项目仅供学习和参考使用。

## 👥 贡献

欢迎提交 Issue 和 Pull Request！

## 📞 联系方式

如有问题或建议，请通过 Issue 反馈。

## 📖 配置修改指南

详细的Mock数据和网络图片配置修改说明，请查看：

**[配置修改指南.md](./配置修改指南.md)**

该文档包含：
- Mock数据修改步骤（标题、用户名、内容）
- 网络图片URL配置方法
- 使用固定图片URL的方法
- 常用图片服务URL格式
- 完整示例和常见问题

## 🚀 上传到 GitHub

详细的 GitHub 上传步骤，请查看：

**[GitHub上传指南.md](./GitHub上传指南.md)**

该文档包含：
- 创建 GitHub 仓库
- 使用命令行上传
- 使用 Android Studio 上传
- 常见问题解决
- 安全建议

---

**注意**：本项目为学习项目，部分功能使用Mock数据模拟。在生产环境中需要替换为真实的API接口和数据源。

