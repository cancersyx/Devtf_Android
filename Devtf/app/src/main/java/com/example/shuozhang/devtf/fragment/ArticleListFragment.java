package com.example.shuozhang.devtf.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shuozhang.devtf.R;
import com.example.shuozhang.devtf.activity.ArticleDetailActivity;
import com.example.shuozhang.devtf.adapter.ArticleAdapter;
import com.example.shuozhang.devtf.bean.Article;
import com.example.shuozhang.devtf.db.DatabaseHelper;
import com.example.shuozhang.devtf.listener.DataListener;
import com.example.shuozhang.devtf.listener.OnItemClickListener;
import com.example.shuozhang.devtf.net.HttpFlinger;
import com.example.shuozhang.devtf.parser.ArticleParser;
import com.example.shuozhang.devtf.widget.AutoLoadRecyclerView;

import java.util.List;

/**
 * Created by shuozhang on 2018/1/2.
 * 文章列表主界面
 * 首先从缓存中读取文章列表，然后再从网络上更新列表，并且最终将更新的数据缓存到数据库中
 */

public class ArticleListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        AutoLoadRecyclerView.onLoadListener {
    private ArticleAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;//下拉刷新
    private AutoLoadRecyclerView mRecyclerView;
    private int mPageIndex = 1;//文章的页面索引，用于分页加载
    private ArticleParser mArticleParser = new ArticleParser();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article, container, false);
        initRefreshView(view);
        initAdapter();
        mSwipeRefreshLayout.setRefreshing(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //从数据库中加载缓存文章
        loadArticlesFromDB();
    }

    private void loadArticlesFromDB() {
        mAdapter.addItems(DatabaseHelper.getInstance().loadArticles());
    }

    private void initAdapter() {
        mAdapter = new ArticleAdapter();
        mAdapter.setOnItemClickListener(new OnItemClickListener<Article>() {
            @Override
            public void onClick(Article article) {
                if (article != null) {
                    jumpToDetailActivity(article);
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        //网络获取数据
        fetchArticles(1);
    }

    private void fetchArticles(final int page) {
        //发起网络请求获取文章列表
        HttpFlinger.get(prepareRequestUrl(), mArticleParser, new DataListener<List<Article>>() {
            @Override
            public void onComplete(List<Article> result) {
                //添加数据
                mAdapter.addItems(result);
                mSwipeRefreshLayout.setRefreshing(false);
                //存储文章列表
                DatabaseHelper.getInstance().saveArticles(result);
                if (result.size() > 0) {
                    mPageIndex++;
                }
            }
        });
    }

    private String prepareRequestUrl() {
        return "http://www.devtf.cn/api/v1/?type=articles&page=" + mPageIndex
                + "&count=20&category=1";
    }

    /**
     * 跳转到页面详情
     *
     * @param article
     */
    private void jumpToDetailActivity(Article article) {
        Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
        intent.putExtra("post_id", article.post_id);
        intent.putExtra("title", article.title);
        startActivity(intent);
    }

    private void initRefreshView(View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView = view.findViewById(R.id.articles_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setOnLoadListener(this);
    }

    @Override
    public void onRefresh() {
        fetchArticles(1);
    }

    @Override
    public void onLoad() {
        mSwipeRefreshLayout.setRefreshing(true);
        fetchArticles(mPageIndex);
    }
}
