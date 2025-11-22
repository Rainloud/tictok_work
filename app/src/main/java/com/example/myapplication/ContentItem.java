package com.example.myapplication;

public class ContentItem {
    private String title; // 标题
    private String contentText;
    private String username;
    private int likeCount;
    private boolean isLiked; // 点赞状态
    private int imageResource; // 临时使用资源ID，实际应该使用URL
    private String imageUrl; // 网络图片URL
    private int imageHeight; // 图片高度（dp），用于瀑布流动态高度

    public ContentItem(String contentText, String username, int likeCount, int imageResource) {
        this("", contentText, username, likeCount, false, imageResource, 200);
    }

    public ContentItem(String contentText, String username, int likeCount, int imageResource, int imageHeight) {
        this("", contentText, username, likeCount, false, imageResource, imageHeight);
    }

    public ContentItem(String title, String contentText, String username, int likeCount, int imageResource, int imageHeight) {
        this(title, contentText, username, likeCount, false, imageResource, imageHeight);
    }

    public ContentItem(String title, String contentText, String username, int likeCount, boolean isLiked, int imageResource, int imageHeight) {
        this(title, contentText, username, likeCount, isLiked, imageResource, null, imageHeight);
    }

    public ContentItem(String title, String contentText, String username, int likeCount, boolean isLiked, int imageResource, String imageUrl, int imageHeight) {
        this.title = title;
        this.contentText = contentText;
        this.username = username;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
        this.imageResource = imageResource;
        this.imageUrl = imageUrl;
        this.imageHeight = imageHeight;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    /**
     * 切换点赞状态并更新计数
     */
    public void toggleLike() {
        if (isLiked) {
            // 取消点赞
            isLiked = false;
            if (likeCount > 0) {
                likeCount--;
            }
        } else {
            // 点赞
            isLiked = true;
            likeCount++;
        }
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

