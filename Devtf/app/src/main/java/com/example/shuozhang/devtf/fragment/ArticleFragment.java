package com.example.shuozhang.devtf.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shuozhang.devtf.adapter.ArticleAdapter;
import com.example.shuozhang.devtf.widget.AutoLoadRecyclerView;
import com.example.shuozhang.devtf.db.DatabaseHelper;
import com.example.shuozhang.devtf.listener.OnItemClickListener;
import com.example.shuozhang.devtf.R;
import com.example.shuozhang.devtf.activity.ArticleDetailActivity;
import com.example.shuozhang.devtf.bean.Article;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by shuozhang on 2018/1/2.
 * 文章列表主界面
 * 首先从缓存中读取文章列表，然后再从网络上更新列表，并且最终将更新的数据缓存到数据库中
 */

public class ArticleFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        AutoLoadRecyclerView.onLoadListener {
    private ArticleAdapter mAdapter;
    private int mCategory = Article.ALL;
    private SwipeRefreshLayout mSwipeRefreshLayout;//下拉刷新
    private AutoLoadRecyclerView mRecyclerView;
    private List<Article> mDataSet = new ArrayList<>();//文章列表
    private int mPageIndex = 1;//文章的页面索引，用于分页加载

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
        mDataSet.addAll(DatabaseHelper.getInstance().loadArticles());
        mAdapter.notifyDataSetChanged();
    }

    private void initAdapter() {
        mAdapter = new ArticleAdapter(mDataSet);
        mAdapter.setOnItemClickListener(new OnItemClickListener<Article>() {
            @Override
            public void onClick(Article article) {
                if (article != null) {
                    loadArticle(article);
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        //网络获取数据
        getArticles(1);
    }

    public void setArticleCategory(int category) {
        mCategory = category;
    }

    /**
     * 封装异步请求
     *
     * @param page
     */
    private void getArticles(final int page) {
        new AsyncTask<Void, Void, List<Article>>() {

            @Override
            protected void onPreExecute() {
                mSwipeRefreshLayout.setRefreshing(true);
            }

            @Override
            protected List<Article> doInBackground(Void... params) {
                return performRequest(page);
            }

            @Override
            protected void onPostExecute(List<Article> result) {
                //移除已经更新的数据
                result.removeAll(mDataSet);
                //添加全部新数据
                mDataSet.addAll(result);
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
                //存储文章列表到数据库
                DatabaseHelper.getInstance().saveArticles(result);
                if (result.size() > 0) {
                    mPageIndex++;
                }
            }
        }.execute();

    }

    /**
     * 执行网络请求
     *
     * @param page
     * @return
     */
    private List<Article> performRequest(int page) {
        HttpURLConnection urlConnection = null;
        try {
            String url = "http://www.devtf.cn/api/v1/?type=articles&page=" + mPageIndex + "&count=20&category=1";
            urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.connect();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String result = sb.toString();
            return parse(new JSONArray(result));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return new ArrayList<>();
    }

    @SuppressLint("SimpleDateFormat")
    private List<Article> parse(JSONArray jsonArray) {
        List<Article> articleList = new LinkedList<>();
        int count = jsonArray.length();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (int i = 0; i < count; i++) {
            JSONObject itemObject = jsonArray.optJSONObject(i);
            Article article = new Article();
            article.title = itemObject.optString("title");
            article.author = itemObject.optString("author");
            article.postId = itemObject.optString("postId");
            String category = itemObject.optString("category");
            article.category = TextUtils.isEmpty(category) ? 0 : Integer.valueOf(category);
            article.publishTime = formatDate(dateFormat, itemObject.optString("date"));
            articleList.add(article);

        }
        return articleList;
    }

    private String formatDate(SimpleDateFormat dateFormat, String dateString) {
        try {
            Date date = dateFormat.parse(dateString);
            return dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 跳转到页面详情
     *
     * @param article
     */
    private void loadArticle(Article article) {
        Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
        intent.putExtra("postId", article.postId);
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
        getArticles(1);
    }

    @Override
    public void onLoad() {
        mSwipeRefreshLayout.setRefreshing(true);
        getArticles(mPageIndex);
    }
}
