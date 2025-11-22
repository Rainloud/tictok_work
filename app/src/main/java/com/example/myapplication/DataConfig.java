package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据配置管理类
 * 支持Mock数据模拟和网络图片资源配置
 */
public class DataConfig {
    
    // 网络图片资源基础URL（使用占位图片服务）
    private static final String IMAGE_BASE_URL = "https://picsum.photos/";
    
    // Mock数据配置
    private static final String[] MOCK_TITLES = {
            "面相学解读",
            "Instagram账号创建",
            "中医养生：睡眠与五脏",
            "80年代回忆",
            "深圳野生动物园攻略",
            "上海美食探店",
            "摄影技巧分享",
            "旅行日记",
            "生活小妙招",
            "健身心得",
            "读书笔记",
            "电影推荐",
            "穿搭指南",
            "护肤心得",
            "美食制作",
            "宠物日常"
    };
    
    private static final String[] MOCK_USERNAMES = {
            "麦瞌阿瑟",
            "WuTi_6",
            "琳公子",
            "时光旅行者",
            "旅行达人",
            "美食家",
            "摄影师",
            "生活家",
            "健身教练",
            "读书人",
            "影评人",
            "时尚博主",
            "美妆师",
            "厨神",
            "宠物爱好者",
            "经验分享者"
    };
    
    private static final String[] MOCK_CONTENTS = {
            "天菩萨...她的面相真的应了老人那句话。看过她的采访就...",
            "有人需要帮 忙创建Instagram账号吗？这里有详细的步骤和注意事项。",
            "中医: 睡眠差 是五脏在求救\n一、爱做梦(养血)\n最近老是做梦,睡得不踏实。脸色也不好,发白没起色,还总头晕、没劲儿,这可能是脾胃不太舒服或者就是想事情太多,把气血都耗着了 #每日分享 #失眠",
            "嗨,这里是三岁的我,和我80 年代的家#旧时光老照片 #纪念",
            "这是示例内容，包含一些较长的文本描述，用于测试瀑布流布局的动态高度适配效果。内容可以包含多行文字，展示不同的信息。",
            "分享一些生活中的小技巧和经验，希望能帮助到大家。",
            "记录旅行的点点滴滴，分享美好的回忆。",
            "生活中有很多小妙招，可以让生活更便捷。",
            "坚持健身，保持健康的生活方式。",
            "读书是一种享受，分享读书心得。",
            "推荐一些好看的电影，值得一看。",
            "分享穿搭心得，打造个人风格。",
            "护肤心得分享，保持肌肤健康。",
            "美食制作教程，简单易学。",
            "宠物日常分享，可爱的小家伙们。"
    };
    
    private static final int[] MOCK_HEIGHTS = {160, 180, 200, 220, 240, 260, 280, 300, 320, 340, 360, 200, 220, 240, 260, 280};
    
    /**
     * 获取网络图片URL
     * @param width 图片宽度
     * @param height 图片高度
     * @param seed 随机种子（用于获取不同的图片）
     * @return 图片URL
     */
    public static String getImageUrl(int width, int height, int seed) {
        return IMAGE_BASE_URL + width + "/" + height + "?random=" + seed;
    }
    
    /**
     * 获取随机网络图片URL
     * @param width 图片宽度
     * @param height 图片高度
     * @return 图片URL
     */
    public static String getRandomImageUrl(int width, int height) {
        int seed = (int)(Math.random() * 10000);
        return getImageUrl(width, height, seed);
    }
    
    /**
     * 获取Mock标题列表
     */
    public static String[] getMockTitles() {
        return MOCK_TITLES.clone();
    }
    
    /**
     * 获取Mock用户名列表
     */
    public static String[] getMockUsernames() {
        return MOCK_USERNAMES.clone();
    }
    
    /**
     * 获取Mock内容列表
     */
    public static String[] getMockContents() {
        return MOCK_CONTENTS.clone();
    }
    
    /**
     * 获取Mock高度列表
     */
    public static int[] getMockHeights() {
        return MOCK_HEIGHTS.clone();
    }
    
    /**
     * 更新图片基础URL（支持动态配置）
     */
    public static void setImageBaseUrl(String baseUrl) {
        // 这里可以扩展为支持动态更新
        // 目前使用静态配置
    }
    
    /**
     * 获取图片基础URL
     */
    public static String getImageBaseUrl() {
        return IMAGE_BASE_URL;
    }
}

