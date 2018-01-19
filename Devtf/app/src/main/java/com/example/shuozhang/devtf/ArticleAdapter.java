package com.example.shuozhang.devtf;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EWorld
 * 2018/1/8 18:49
 *
 * @Description
 * @E-mail 852333743@qq.com
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {
    List<Article> mDataSet = new ArrayList<>();
    OnItemClickListener<Article> mItemClickListener;

    public ArticleAdapter(List<Article> dataSet) {
        this.mDataSet = dataSet;
    }

    protected Article getItem(int position){
        return mDataSet.get(position);
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return createArticleViewHolder(parent);
    }

    private ArticleViewHolder createArticleViewHolder(ViewGroup parent) {
        return new ArticleViewHolder(inflateItemView(parent,R.layout.recycler_article_item,false));
    }

    protected View inflateItemView(ViewGroup viewGroup,int layoutId,boolean attach){
        return LayoutInflater.from(viewGroup.getContext()).inflate(layoutId,viewGroup,attach);
    }

    public void setOnItemClickListener(OnItemClickListener<Article> itemClickListener){
        this.mItemClickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        Article article = getItem(position);
        bindArticleToItemView(holder,article);
        setupItemViewClickListener(holder,article);
    }

    private void setupItemViewClickListener(ArticleViewHolder holder, final Article article) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null){
                    mItemClickListener.onClick(article);
                }
            }
        });
    }

    /**
     * 绑定文章数据
     * @param holder
     * @param article
     */
    private void bindArticleToItemView(ArticleViewHolder holder, Article article) {
        holder.titleView.setText(article.title);
        holder.publishTimeView.setText(article.publishTime);
        holder.authorView.setText(article.author);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder{
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
