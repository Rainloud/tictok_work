package com.example.myapplication;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView contentRecyclerView;
    private ContentAdapter contentAdapter;
    private List<ContentItem> contentItems;
    private StaggeredGridLayoutManager layoutManager;
    private boolean isLoading = false;
    private boolean hasMoreData = true;
    private int currentPage = 0;
    
    // 数据管理器
    private DataManager dataManager;
    
    // 下拉刷新相关
    private float lastY = 0;
    private float pullDistance = 0;
    private boolean isRefreshing = false;
    private View refreshHeaderContainer;
    private ProgressBar refreshProgress;
    private TextView refreshText;
    private ImageView refreshArrow;
    private static final float PULL_THRESHOLD = 100f; // 下拉阈值（px）
    private static final float MAX_PULL_DISTANCE = 150f; // 最大下拉距离（px）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 初始化数据管理器
        dataManager = DataManager.getInstance();
        
        initViews();
        initContentData();
        setupRecyclerView();
    }

    private void initViews() {
        contentRecyclerView = findViewById(R.id.content_recycler_view);
        
        // 初始化刷新头部视图
        refreshHeaderContainer = findViewById(R.id.refresh_header_container);
        refreshProgress = refreshHeaderContainer.findViewById(R.id.refresh_progress);
        refreshText = refreshHeaderContainer.findViewById(R.id.refresh_text);
        refreshArrow = refreshHeaderContainer.findViewById(R.id.refresh_arrow);
        
        // 初始化刷新头部位置（隐藏在顶部）
        refreshHeaderContainer.post(() -> {
            refreshHeaderContainer.setTranslationY(-refreshHeaderContainer.getHeight());
        });
        
        // 设置标签页点击事件
        setupTabClickListeners();
        
        // 设置下拉刷新
        setupPullToRefresh();
        
        // 设置上拉加载更多
        setupLoadMore();
    }

    private void setupTabClickListeners() {
        TextView tabExperience = findViewById(R.id.tab_experience);
        TextView tabFeatured = findViewById(R.id.tab_featured);
        TextView tabLive = findViewById(R.id.tab_live);
        TextView tabHot = findViewById(R.id.tab_hot);
        TextView tabShanghai = findViewById(R.id.tab_shanghai);
        TextView tabFollow = findViewById(R.id.tab_follow);
        TextView tabGroup = findViewById(R.id.tab_group);

        View.OnClickListener tabClickListener = v -> {
            // 重置所有标签颜色
            int unselectedColor = ContextCompat.getColor(this, R.color.tab_unselected);
            int selectedColor = ContextCompat.getColor(this, R.color.tab_selected);
            
            tabExperience.setTextColor(unselectedColor);
            tabFeatured.setTextColor(unselectedColor);
            tabLive.setTextColor(unselectedColor);
            tabHot.setTextColor(unselectedColor);
            tabShanghai.setTextColor(unselectedColor);
            tabFollow.setTextColor(unselectedColor);
            tabGroup.setTextColor(unselectedColor);

            // 设置选中标签颜色
            if (v instanceof TextView) {
                ((TextView) v).setTextColor(selectedColor);
            }
        };

        tabExperience.setOnClickListener(tabClickListener);
        tabFeatured.setOnClickListener(tabClickListener);
        tabLive.setOnClickListener(tabClickListener);
        tabHot.setOnClickListener(tabClickListener);
        tabShanghai.setOnClickListener(tabClickListener);
        tabFollow.setOnClickListener(tabClickListener);
        tabGroup.setOnClickListener(tabClickListener);
    }

//瀑布流布局
    private void setupRecyclerView() {
        // 使用2列瀑布流布局，支持动态高度
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // 防止item交换位置，保持布局稳定
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        contentRecyclerView.setLayoutManager(layoutManager);
        
        // 设置RecyclerView属性，优化性能
        contentRecyclerView.setHasFixedSize(false);
        contentRecyclerView.setItemAnimator(null); // 禁用动画以提高性能
        
        contentAdapter = new ContentAdapter(contentItems);
        contentRecyclerView.setAdapter(contentAdapter);
    }
    //自定义下拉刷新
    /**
     * 设置下拉刷新（自定义实现，不依赖SwipeRefreshLayout）
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setupPullToRefresh() {
        // 使用触摸事件检测下拉手势
        contentRecyclerView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastY = event.getY();
                    pullDistance = 0;
                    break;
                    
                case MotionEvent.ACTION_MOVE:
                    float currentY = event.getY();
                    float deltaY = currentY - lastY;
                    
                    // 如果向下滑动且已经在顶部
                    if (deltaY > 0 && !contentRecyclerView.canScrollVertically(-1) && !isRefreshing && !isLoading) {
                        pullDistance += deltaY;
                        
                        // 限制最大下拉距离
                        if (pullDistance > MAX_PULL_DISTANCE) {
                            pullDistance = MAX_PULL_DISTANCE;
                        }
                        
                        // 更新刷新头部视图
                        updateRefreshHeader(pullDistance);
                        
                        // 如果下拉距离超过阈值，显示"释放刷新"
                        if (pullDistance >= PULL_THRESHOLD) {
                            showRefreshHeader(true);
                            refreshText.setText("释放刷新");
                            // 旋转箭头
                            refreshArrow.setRotation(270);
                        } else {
                            showRefreshHeader(true);
                            refreshText.setText("下拉刷新");
                            refreshArrow.setRotation(90);
                        }
                        
                        lastY = currentY;
                        return true; // 拦截事件，阻止RecyclerView滚动
                    } else if (pullDistance > 0) {
                        // 如果正在下拉但条件不满足，重置
                        resetRefreshHeader();
                    }
                    lastY = currentY;
                    break;
                    
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (pullDistance >= PULL_THRESHOLD && !isRefreshing && !isLoading) {
                        // 触发刷新
                        refreshData();
                    } else {
                        // 重置刷新头部
                        resetRefreshHeader();
                    }
                    pullDistance = 0;
                    break;
            }
            return false; // 不拦截事件，让RecyclerView正常处理
        });
    }
    
    /**
     * 更新刷新头部视图
     */
    private void updateRefreshHeader(float distance) {
        if (refreshHeaderContainer != null) {
            float alpha = Math.min(distance / PULL_THRESHOLD, 1.0f);
            refreshHeaderContainer.setAlpha(alpha);
            
            // 根据下拉距离调整位置，从顶部向下滑出
            float headerHeight = refreshHeaderContainer.getHeight();
            if (headerHeight == 0) {
                // 如果高度还未测量，使用最小高度
                headerHeight = 60 * getResources().getDisplayMetrics().density;
            }
            float translationY = -headerHeight + Math.min(distance, MAX_PULL_DISTANCE);
            refreshHeaderContainer.setTranslationY(translationY);
            
            // 同时让RecyclerView向下移动，露出刷新头部
            contentRecyclerView.setTranslationY(Math.min(distance, MAX_PULL_DISTANCE));
        }
    }
    
    /**
     * 显示刷新头部
     */
    private void showRefreshHeader(boolean show) {
        if (refreshHeaderContainer != null) {
            refreshHeaderContainer.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
    
    /**
     * 重置刷新头部
     */
    private void resetRefreshHeader() {
        if (refreshHeaderContainer != null) {
            float headerHeight = refreshHeaderContainer.getHeight();
            if (headerHeight == 0) {
                headerHeight = 60 * getResources().getDisplayMetrics().density;
            }
            
            // 同时动画刷新头部和RecyclerView
            ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(refreshHeaderContainer, "translationY", 
                    refreshHeaderContainer.getTranslationY(), -headerHeight);
            headerAnimator.setDuration(200);
            headerAnimator.setInterpolator(new LinearInterpolator());
            headerAnimator.start();
            
            ObjectAnimator recyclerAnimator = ObjectAnimator.ofFloat(contentRecyclerView, "translationY", 
                    contentRecyclerView.getTranslationY(), 0);
            recyclerAnimator.setDuration(200);
            recyclerAnimator.setInterpolator(new LinearInterpolator());
            recyclerAnimator.start();
            
            refreshHeaderContainer.postDelayed(() -> {
                showRefreshHeader(false);
                refreshHeaderContainer.setAlpha(1.0f);
                refreshArrow.setRotation(90);
            }, 200);
        }
    }
    
    /**
     * 设置上拉加载更多
     */
    private void setupLoadMore() {
        contentRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                
                if (!isLoading && hasMoreData && !isRefreshing) {
                    // 获取最后一个可见item的位置
                    int[] lastVisiblePositions = layoutManager.findLastVisibleItemPositions(null);
                    int lastVisiblePosition = getMaxPosition(lastVisiblePositions);
                    int totalItemCount = layoutManager.getItemCount();
                    
                    // 当滚动到倒数第3个item时，开始加载更多
                    if (lastVisiblePosition >= totalItemCount - 3) {
                        loadMoreData();
                    }
                }
            }
        });
    }
    
    /**
     * 获取数组中的最大值
     */
    private int getMaxPosition(int[] positions) {
        int max = positions[0];
        for (int position : positions) {
            if (position > max) {
                max = position;
            }
        }
        return max;
    }
    
    /**
     * 刷新数据
     */
    private void refreshData() {
        if (isLoading || isRefreshing) {
            return;
        }
        
        isRefreshing = true;
        isLoading = true;
        currentPage = 0;
        
        // 显示刷新状态
        showRefreshHeader(true);
        refreshProgress.setVisibility(View.VISIBLE);
        refreshArrow.setVisibility(View.GONE);
        refreshText.setText("正在刷新...");
        
        // 动画显示刷新头部和移动RecyclerView
        float headerHeight = refreshHeaderContainer.getHeight();
        if (headerHeight == 0) {
            headerHeight = 60 * getResources().getDisplayMetrics().density;
        }
        
        // 将刷新头部移动到可见位置
        ObjectAnimator headerShowAnimator = ObjectAnimator.ofFloat(refreshHeaderContainer, "translationY", 
                refreshHeaderContainer.getTranslationY(), 0);
        headerShowAnimator.setDuration(200);
        headerShowAnimator.start();
        
        // 将RecyclerView向下移动，露出刷新头部
        ObjectAnimator recyclerShowAnimator = ObjectAnimator.ofFloat(contentRecyclerView, "translationY", 
                contentRecyclerView.getTranslationY(), headerHeight);
        recyclerShowAnimator.setDuration(200);
        recyclerShowAnimator.start();
        
        // 淡入动画
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(refreshHeaderContainer, "alpha", 
                refreshHeaderContainer.getAlpha(), 1f);
        alphaAnimator.setDuration(200);
        alphaAnimator.start();
        
        // 使用数据管理器刷新数据
        contentRecyclerView.postDelayed(() -> {
            List<ContentItem> newItems = dataManager.refreshData(4);
            contentAdapter.refreshItems(newItems);
            contentItems = newItems;
            
            // 更新状态
            hasMoreData = dataManager.hasMoreData();
            currentPage = dataManager.getCurrentPage();
            
            // 更新刷新状态文本
            refreshText.setText("刷新完成");
            
            // 延迟隐藏刷新头部
            contentRecyclerView.postDelayed(() -> {
                isRefreshing = false;
                isLoading = false;
                
                // 淡出动画
                ObjectAnimator hideAnimator = ObjectAnimator.ofFloat(refreshHeaderContainer, "alpha", 1f, 0f);
                hideAnimator.setDuration(300);
                hideAnimator.start();
                
                refreshHeaderContainer.postDelayed(() -> {
                    resetRefreshHeader();
                    refreshProgress.setVisibility(View.GONE);
                    refreshArrow.setVisibility(View.VISIBLE);
                }, 300);
            }, 500);
        }, 1500); // 1.5秒延迟
    }
    
    /**
     * 加载更多数据
     */
    private void loadMoreData() {
        if (isLoading || !hasMoreData) {
            return;
        }
        
        isLoading = true;
        contentAdapter.setLoadingMore(true);
        
        // 使用数据管理器加载更多数据
        contentRecyclerView.postDelayed(() -> {
            List<ContentItem> newItems = dataManager.loadMoreData(4);
            
            if (newItems.isEmpty()) {
                // 没有更多数据了
                hasMoreData = false;
            } else {
                contentAdapter.addItems(newItems);
                contentItems.addAll(newItems);
            }
            
            // 更新状态
            hasMoreData = dataManager.hasMoreData();
            currentPage = dataManager.getCurrentPage();
            
            contentAdapter.setLoadingMore(false);
            isLoading = false;
        }, 1200); // 1.2秒延迟
    }
    
    /**
     * 初始化内容数据
     */
    private void initContentData() {
        // 使用数据管理器获取初始数据
        contentItems = dataManager.getInitialData(16);
        hasMoreData = dataManager.hasMoreData();
        currentPage = dataManager.getCurrentPage();
    }
}