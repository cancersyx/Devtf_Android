package com.example.shuozhang.devtf.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shuozhang.devtf.R;
import com.example.shuozhang.devtf.bean.Article;

/**
 * Created by EWorld
 * 2018/1/8 18:49
 *
 * @version 2.0
 *          主页文章列表Adapter
 * @Description
 * @E-mail 852333743@qq.com
 */

public class ArticleAdapter extends RecyclerBaseAdapter<Article, ArticleAdapter.ArticleViewHolder> {
    @Override
    protected void bindDataToItemView(ArticleViewHolder viewHolder, Article item) {
        viewHolder.titleView.setText(item.title);
        viewHolder.publishTimeView.setText(item.publishTime);
        viewHolder.authorView.setText(item.author);
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflateItemView(parent, R.layout.recycler_article_item);
        return new ArticleViewHolder(itemView);
    }


    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        public TextView titleView;
        public TextView publishTimeView;
        public TextView authorView;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.article_title_tv);
            publishTimeView = itemView.findViewById(R.id.article_time_tv);
            authorView = itemView.findViewById(R.id.article_author_tv);

        }
    }
}
