package com.example.myapplication;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_CONTENT = 0;
    private static final int TYPE_LOADING = 1;
    
    private List<ContentItem> contentItems;
    private boolean isLoadingMore = false;

    public ContentAdapter(List<ContentItem> contentItems) {
        this.contentItems = contentItems;
    }
    
    public void setLoadingMore(boolean loading) {
        if (isLoadingMore != loading) {
            isLoadingMore = loading;
            if (loading) {
                notifyItemInserted(contentItems.size());
            } else {
                notifyItemRemoved(contentItems.size());
            }
        }
    }
    
    public boolean isLoadingMore() {
        return isLoadingMore;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == contentItems.size() && isLoadingMore) {
            return TYPE_LOADING;
        }
        return TYPE_CONTENT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading_more, parent, false);
            return new LoadingViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_content_card, parent, false);
            return new ContentViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoadingViewHolder) {
            // 加载更多视图，无需绑定数据
            return;
        }
        
        ContentViewHolder contentHolder = (ContentViewHolder) holder;
        ContentItem item = contentItems.get(position);
        
        // 保存position，用于点击事件
        contentHolder.itemPosition = position;
        
        // 设置标题（如果有）
        if (item.getTitle() != null && !item.getTitle().isEmpty()) {
            contentHolder.contentTitle.setVisibility(View.VISIBLE);
            contentHolder.contentTitle.setText(item.getTitle());
        } else {
            contentHolder.contentTitle.setVisibility(View.GONE);
        }
        
        // 设置内容文本
        contentHolder.contentText.setText(item.getContentText());
        
        // 设置用户名
        contentHolder.username.setText(item.getUsername());
        
        // 更新点赞状态UI
        updateLikeUI(contentHolder, item);
        
        // 动态设置图片高度，实现瀑布流效果
        ViewGroup.LayoutParams imageParams = contentHolder.contentImage.getLayoutParams();
        DisplayMetrics metrics = contentHolder.itemView.getContext().getResources().getDisplayMetrics();
        // 将dp转换为px
        int heightInPx = (int) (item.getImageHeight() * metrics.density);
        imageParams.height = heightInPx;
        contentHolder.contentImage.setLayoutParams(imageParams);
        
        // 设置图片 - 优先使用网络图片URL，其次使用本地资源
        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            // 网络图片URL已配置，但需要图片加载库（如Glide）来加载
            // 这里使用占位符，实际项目中应该使用图片加载库
            // Glide.with(contentHolder.itemView.getContext())
            //     .load(item.getImageUrl())
            //     .placeholder(R.drawable.placeholder)
            //     .into(contentHolder.contentImage);
            contentHolder.contentImage.setBackgroundColor(ContextCompat.getColor(
                    contentHolder.itemView.getContext(), R.color.divider));
        } else if (item.getImageResource() != 0) {
            contentHolder.contentImage.setImageResource(item.getImageResource());
        } else {
            // 使用默认占位符
            contentHolder.contentImage.setBackgroundColor(ContextCompat.getColor(
                    contentHolder.itemView.getContext(), R.color.divider));
        }
        
        // 设置用户头像（这里使用占位符，实际应该加载真实头像）
        // 可以在这里使用图片加载库加载用户头像
        
        // 设置点赞点击监听器
        contentHolder.likeContainer.setOnClickListener(v -> {
            int pos = contentHolder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && pos < contentItems.size()) {
                ContentItem clickedItem = contentItems.get(pos);
                clickedItem.toggleLike();
                updateLikeUI(contentHolder, clickedItem);
            }
        });
    }
    
    /**
     * 更新点赞UI状态
     */
    private void updateLikeUI(@NonNull ContentViewHolder holder, ContentItem item) {
        // 格式化点赞数显示
        String likeCountText = formatLikeCount(item.getLikeCount());
        holder.likeCount.setText(likeCountText);
        
        // 根据点赞状态更新图标和颜色
        if (item.isLiked()) {
            // 已点赞状态：使用填充图标，红色
            holder.likeIcon.setImageResource(R.drawable.ic_favorite_filled);
            holder.likeIcon.clearColorFilter();
            holder.likeIcon.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.accent_red));
            holder.likeCount.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.accent_red));
        } else {
            // 未点赞状态：使用轮廓图标，灰色
            holder.likeIcon.setImageResource(R.drawable.ic_favorite);
            holder.likeIcon.clearColorFilter();
            holder.likeIcon.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.text_secondary));
            holder.likeCount.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.text_primary));
        }
    }
    
    /**
     * 格式化点赞数显示
     * @param count 点赞数
     * @return 格式化后的字符串
     */
    private String formatLikeCount(int count) {
        if (count < 1000) {
            return String.valueOf(count);
        } else if (count < 10000) {
            double k = count / 1000.0;
            if (k == (int) k) {
                return String.format("%dK", (int) k);
            }
            return String.format("%.1fK", k);
        } else if (count < 100000) {
            double w = count / 10000.0;
            if (w == (int) w) {
                return String.format("%dW", (int) w);
            }
            return String.format("%.1fW", w);
        } else {
            double w = count / 10000.0;
            if (w == (int) w) {
                return String.format("%dW", (int) w);
            }
            return String.format("%.1fW", w);
        }
    }

    @Override
    public int getItemCount() {
        int count = contentItems != null ? contentItems.size() : 0;
        if (isLoadingMore) {
            count += 1; // 添加加载更多的item
        }
        return count;
    }
    
    public void addItems(List<ContentItem> newItems) {
        int startPosition = contentItems.size();
        contentItems.addAll(newItems);
        notifyItemRangeInserted(startPosition, newItems.size());
    }
    
    public void refreshItems(List<ContentItem> newItems) {
        contentItems.clear();
        contentItems.addAll(newItems);
        notifyDataSetChanged();
    }

    static class ContentViewHolder extends RecyclerView.ViewHolder {
        ImageView contentImage;
        TextView contentTitle;
        TextView contentText;
        ImageView profileImage;
        TextView username;
        ImageView likeIcon;
        TextView likeCount;
        View likeContainer; // 点赞容器，用于点击事件
        int itemPosition; // 保存item位置

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            contentImage = itemView.findViewById(R.id.content_image);
            contentTitle = itemView.findViewById(R.id.content_title);
            contentText = itemView.findViewById(R.id.content_text);
            profileImage = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.username);
            likeIcon = itemView.findViewById(R.id.like_icon);
            likeCount = itemView.findViewById(R.id.like_count);
            // 找到点赞容器（包含图标和文字的LinearLayout）
            likeContainer = itemView.findViewById(R.id.like_container);
        }
    }
    
    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar loadingProgress;
        TextView loadingText;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            loadingProgress = itemView.findViewById(R.id.loading_progress);
            loadingText = itemView.findViewById(R.id.loading_text);
        }
    }
}

