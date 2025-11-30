# TikTok 风格内容聚合应用

一个仿 TikTok风格的内容发现平台 Android 应用
##  项目简介

本项目是一个内容聚合类 Android 应用，实现了类似 TikTok 和小红书的内容展示界面。主要特点包括：

- **双列瀑布流布局**：支持动态高度适配的内容卡片
- **下拉刷新**：自定义实现的下拉刷新功能
- **上拉加载更多**：自动检测滚动到底部并加载更多内容
- **点赞交互**：支持点击切换点赞状态，实时更新点赞计数
- **数据配置管理**：支持 Mock 数据模拟和网络图片资源配置

##  功能特性
1. **瀑布流布局** - 双列网格布局，支持动态高度
2. **下拉刷新** - 自定义下拉刷新实现，带视觉反馈
3. **上拉加载更多** - 自动检测滚动到底部并加载更多内容
4. **点赞功能** - 点击切换点赞状态，实时更新计数，格式化显示（K/W单位）
5. **数据管理** - Mock数据生成器、数据配置管理、网络图片URL配置


##  使用说明
- **浏览内容**：应用启动后自动加载，双列瀑布流展示
- **下拉刷新**：在顶部向下滑动，超过阈值后释放刷新
- **上拉加载更多**：滚动到底部自动加载
- **点赞**：点击点赞图标切换状态

##  版本要求
- **开发语言**：Java
- **最低 SDK**：24 (Android 7.0)
- **目标 SDK**：36
- **主要依赖**：AppCompat、Material Design、ConstraintLayout、RecyclerView

##  配置修改

### 修改 Mock 数据

**文件位置**：`app/src/main/java/com/example/myapplication/DataConfig.java`

#### 1. 修改标题列表（第16-33行）

```java
private static final String[] MOCK_TITLES = {
    "你的标题1",
    "你的标题2",
    // 添加更多...
};
```

#### 2. 修改用户名列表（第35-52行）

```java
private static final String[] MOCK_USERNAMES = {
    "用户名1",
    "用户名2",
    // 添加更多...
};
```

#### 3. 修改内容文本（第54-70行）

```java
private static final String[] MOCK_CONTENTS = {
    "你的内容描述1",
    "你的内容描述2",
    // 添加更多...
};
```

#### 4. 修改卡片高度（第72行）

```java
private static final int[] MOCK_HEIGHTS = {
    160, 180, 200, 220, 240, 260, 280, 300
};
```

### 修改网络图片 URL

**文件位置**：`app/src/main/java/com/example/myapplication/DataConfig.java`

#### 1. 修改图片基础 URL（第13行）

```java
// 当前使用
private static final String IMAGE_BASE_URL = "https://picsum.photos/";

// 修改为你的图片服务
private static final String IMAGE_BASE_URL = "https://your-image-server.com/";
```

#### 2. 修改 URL 生成方法（第81-83行）

根据你的图片服务API格式修改：

```java
public static String getImageUrl(int width, int height, int seed) {
    // 根据你的API格式修改
    return IMAGE_BASE_URL + width + "/" + height + "?random=" + seed;
}
```

**常用图片服务格式：**

- **Picsum Photos**：`https://picsum.photos/800/600?random=123`
- **Unsplash**：`https://source.unsplash.com/800x600/?sig=123`
- **Placeholder**：`https://via.placeholder.com/800x600`

#### 3. 使用固定图片 URL（可选）

在 `DataConfig.java` 中添加：

```java
private static final String[] MOCK_IMAGE_URLS = {
    "https://example.com/image1.jpg",
    "https://example.com/image2.jpg",
    // 添加更多图片URL
};

public static String[] getMockImageUrls() {
    return MOCK_IMAGE_URLS.clone();
}
```

然后在 `MockDataGenerator.java` 的 `generateInitialData` 方法中使用：

```java
String[] imageUrls = DataConfig.getMockImageUrls();
String imageUrl = imageUrls[i % imageUrls.length];
```

### 修改刷新数据内容

**文件位置**：`app/src/main/java/com/example/myapplication/MockDataGenerator.java`

找到 `generateRefreshData` 方法（第60-99行），修改：

```java
String[] refreshTitles = {
    "你的刷新标题1",
    "你的刷新标题2",
    // ...
};

String[] refreshUsernames = {
    "你的用户名1",
    "你的用户名2",
    // ...
};
```

### 修改后生效

1. 保存文件（Ctrl+S）
2. 同步项目（Sync Project with Gradle Files）
3. 重新编译（Build -> Rebuild Project）
4. 运行应用查看效果



##  开发说明

### 扩展网络图片加载

当前项目只配置了图片URL，需要添加图片加载库才能显示：

1. **添加 Glide 依赖**（需要修改 gradle，但当前保持配置不变）：
   ```gradle
   implementation 'com.github.bumptech.glide:glide:4.16.0'
   ```

2. **在适配器中加载图片**：
   ```java
   if (item.getImageUrl() != null) {
       Glide.with(holder.itemView.getContext())
           .load(item.getImageUrl())
           .placeholder(R.color.divider)
           .into(holder.contentImage);
   }
   ```

### 切换为真实 API

1. 创建 API 接口
2. 在 `DataManager` 中调用 API 替换 Mock 数据生成

##  已知问题

1. 网络图片加载：当前使用占位符，需要添加图片加载库才能显示真实图片
2. 下拉刷新：在某些设备上可能需要调整阈值
3. 数据持久化：当前数据不持久化，应用重启后重置


---

**注意**：本项目为学习项目，由于本人技术栈未能匹配项目开发需求,所以在项目中结合自己的速成理解以及ai辅助完成
