package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据管理器
 * 统一管理数据获取、更新和配置
 */
public class DataManager {
    
    private static DataManager instance;
    private List<ContentItem> cachedData;
    private int currentPage = 0;
    private boolean hasMoreData = true;
    
    private DataManager() {
        cachedData = new ArrayList<>();
    }
    
    /**
     * 获取单例实例
     */
    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }
    
    /**
     * 获取初始数据
     * @param count 数据数量
     * @return 数据列表
     */
    public List<ContentItem> getInitialData(int count) {
        List<ContentItem> data = MockDataGenerator.generateInitialData(count);
        cachedData.clear();
        cachedData.addAll(data);
        currentPage = 1;
        hasMoreData = true;
        return new ArrayList<>(data);
    }
    
    /**
     * 刷新数据（获取最新数据）
     * @param count 刷新数据数量
     * @return 最新数据列表
     */
    public List<ContentItem> refreshData(int count) {
        List<ContentItem> refreshItems = MockDataGenerator.generateRefreshData(count);
        List<ContentItem> initialData = MockDataGenerator.generateInitialData(16);
        
        // 合并数据：最新数据在前
        List<ContentItem> allData = new ArrayList<>();
        allData.addAll(refreshItems);
        allData.addAll(initialData);
        
        cachedData.clear();
        cachedData.addAll(allData);
        currentPage = 1;
        hasMoreData = true;
        
        return new ArrayList<>(allData);
    }
    
    /**
     * 加载更多数据
     * @param pageSize 每页数量
     * @return 新加载的数据列表
     */
    public List<ContentItem> loadMoreData(int pageSize) {
        if (!hasMoreData) {
            return new ArrayList<>();
        }
        
        currentPage++;
        List<ContentItem> newData = MockDataGenerator.generateLoadMoreData(currentPage, pageSize);
        
        if (newData.isEmpty()) {
            hasMoreData = false;
        } else {
            cachedData.addAll(newData);
        }
        
        return new ArrayList<>(newData);
    }
    
    /**
     * 更新数据项（例如点赞状态）
     * @param position 位置
     * @param item 更新后的数据项
     */
    public void updateItem(int position, ContentItem item) {
        if (position >= 0 && position < cachedData.size()) {
            cachedData.set(position, item);
        }
    }
    
    /**
     * 获取所有缓存数据
     */
    public List<ContentItem> getAllCachedData() {
        return new ArrayList<>(cachedData);
    }
    
    /**
     * 清除缓存数据
     */
    public void clearCache() {
        cachedData.clear();
        currentPage = 0;
        hasMoreData = true;
    }
    
    /**
     * 获取当前页码
     */
    public int getCurrentPage() {
        return currentPage;
    }
    
    /**
     * 是否还有更多数据
     */
    public boolean hasMoreData() {
        return hasMoreData;
    }
    
    /**
     * 添加单个数据项
     */
    public void addItem(ContentItem item) {
        cachedData.add(item);
    }
    
    /**
     * 添加多个数据项
     */
    public void addItems(List<ContentItem> items) {
        cachedData.addAll(items);
    }
    
    /**
     * 设置数据（用于完全替换）
     */
    public void setData(List<ContentItem> data) {
        cachedData.clear();
        cachedData.addAll(data);
    }
}

