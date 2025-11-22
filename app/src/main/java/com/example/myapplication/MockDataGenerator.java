package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Mock数据生成器
 * 用于生成模拟数据，支持动态更新
 */
public class MockDataGenerator {
    
    private static final Random random = new Random();
    
    /**
     * 生成初始Mock数据
     * @param count 生成数量
     * @return Mock数据列表
     */
    public static List<ContentItem> generateInitialData(int count) {
        List<ContentItem> items = new ArrayList<>();
        String[] titles = DataConfig.getMockTitles();
        String[] usernames = DataConfig.getMockUsernames();
        String[] contents = DataConfig.getMockContents();
        int[] heights = DataConfig.getMockHeights();
        
        // 屏幕宽度（用于生成合适的图片宽度）
        int screenWidth = 400; // 假设屏幕宽度，实际应该从设备获取
        int cardWidth = screenWidth / 2 - 16; // 两列布局，减去边距
        
        for (int i = 0; i < count && i < titles.length; i++) {
            int height = heights[i % heights.length];
            // 随机调整高度，增加变化
            height += random.nextInt(40) - 20;
            
            // 生成网络图片URL
            int imageHeight = (int)(height * 2.5); // 转换为像素高度
            String imageUrl = DataConfig.getRandomImageUrl(cardWidth, imageHeight);
            
            items.add(new ContentItem(
                    titles[i],
                    contents[i % contents.length],
                    usernames[i % usernames.length],
                    random.nextInt(5000) + 10,
                    random.nextBoolean(), // 随机点赞状态
                    0, // 不使用本地资源
                    imageUrl,
                    height
            ));
        }
        
        return items;
    }
    
    /**
     * 生成刷新数据（最新内容）
     * @param count 生成数量
     * @return Mock数据列表
     */
    public static List<ContentItem> generateRefreshData(int count) {
        List<ContentItem> items = new ArrayList<>();
        
        String[] refreshTitles = {
                "最新：今日热点",
                "最新：热门推荐",
                "最新：精选内容",
                "最新：热门话题"
        };
        
        String[] refreshUsernames = {
                "热点追踪",
                "热门推荐官",
                "精选编辑",
                "话题达人"
        };
        
        int screenWidth = 400;
        int cardWidth = screenWidth / 2 - 16;
        int[] heights = {200, 260, 240, 280};
        
        for (int i = 0; i < count && i < refreshTitles.length; i++) {
            int height = heights[i];
            int imageHeight = (int)(height * 2.5);
            String imageUrl = DataConfig.getRandomImageUrl(cardWidth, imageHeight);
            
            items.add(new ContentItem(
                    refreshTitles[i],
                    "这是最新刷新的内容 " + (i + 1) + "，包含最新的信息和动态。",
                    refreshUsernames[i],
                    random.nextInt(1000) + 100,
                    false,
                    0,
                    imageUrl,
                    height
            ));
        }
        
        return items;
    }
    
    /**
     * 生成加载更多数据
     * @param page 页码
     * @param pageSize 每页数量
     * @return Mock数据列表
     */
    public static List<ContentItem> generateLoadMoreData(int page, int pageSize) {
        List<ContentItem> items = new ArrayList<>();
        
        // 模拟：第5页之后没有更多数据
        if (page > 5) {
            return items;
        }
        
        String[] titles = DataConfig.getMockTitles();
        String[] usernames = DataConfig.getMockUsernames();
        String[] contents = DataConfig.getMockContents();
        int[] heights = DataConfig.getMockHeights();
        
        int screenWidth = 400;
        int cardWidth = screenWidth / 2 - 16;
        int startIndex = (page - 1) * pageSize;
        
        for (int i = 0; i < pageSize; i++) {
            int index = startIndex + i;
            if (index >= titles.length) {
                // 如果超出范围，循环使用
                index = index % titles.length;
            }
            
            int height = heights[index % heights.length];
            height += random.nextInt(40) - 20;
            
            int imageHeight = (int)(height * 2.5);
            String imageUrl = DataConfig.getRandomImageUrl(cardWidth, imageHeight);
            
            items.add(new ContentItem(
                    titles[index] + " (第" + page + "页)",
                    "这是第" + page + "页的内容 " + (i + 1) + "，包含一些较长的文本描述，用于测试瀑布流布局的动态高度适配效果。内容可以包含多行文字，展示不同的信息。",
                    usernames[index % usernames.length],
                    random.nextInt(5000) + 10,
                    random.nextBoolean(),
                    0,
                    imageUrl,
                    height
            ));
        }
        
        return items;
    }
    
    /**
     * 生成单个Mock数据项
     * @param title 标题
     * @param content 内容
     * @param username 用户名
     * @return Mock数据项
     */
    public static ContentItem generateSingleItem(String title, String content, String username) {
        int screenWidth = 400;
        int cardWidth = screenWidth / 2 - 16;
        int[] heights = DataConfig.getMockHeights();
        int height = heights[random.nextInt(heights.length)];
        height += random.nextInt(40) - 20;
        
        int imageHeight = (int)(height * 2.5);
        String imageUrl = DataConfig.getRandomImageUrl(cardWidth, imageHeight);
        
        return new ContentItem(
                title,
                content,
                username,
                random.nextInt(5000) + 10,
                random.nextBoolean(),
                0,
                imageUrl,
                height
        );
    }
}

