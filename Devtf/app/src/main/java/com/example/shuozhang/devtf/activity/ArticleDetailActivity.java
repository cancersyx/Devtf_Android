package com.example.shuozhang.devtf.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.shuozhang.devtf.R;
import com.example.shuozhang.devtf.bean.ArticleDetail;
import com.example.shuozhang.devtf.db.DatabaseHelper;
import com.example.shuozhang.devtf.listener.DataListener;
import com.example.shuozhang.devtf.net.HtmlUtil;
import com.example.shuozhang.devtf.net.HttpFlinger;

/**
 * Created by EWorld
 * 2018/1/10 12:35
 *
 * @Description 文章阅读页面，使用WebView加载
 * @E-mail 852333743@qq.com
 */

public class ArticleDetailActivity extends BaseActionBarActivity {
    private ProgressBar mProgressBar;
    private WebView mWebView;
    private String mPostId;
    private String mTitle;
    private String mJobUrl;


    @Override
    protected int getContentViewResId() {
        return R.layout.activity_article_detail;
    }

    @Override
    protected void initWidgets() {
        mProgressBar = findViewById(R.id.loading_progressbar);
        mWebView = findViewById(R.id.articles_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                WebSettings settings = mWebView.getSettings();
                settings.setBuiltInZoomControls(true);
                view.loadUrl(url);
                return true;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void afterOnCreate() {
        Bundle extraBundle = getIntent().getExtras();
        if (extraBundle != null && !extraBundle.containsKey("job_url")) {
            mPostId = extraBundle.getString("post_id");
            mTitle = extraBundle.getString("title");
        } else {
            mJobUrl = extraBundle.getString("job_url");
        }
        //从数据库中获取文章内容
        ArticleDetail cacheDetail = DatabaseHelper.getInstance().loadArticleDetail(mPostId);
        if (!TextUtils.isEmpty(cacheDetail.content)) {
            loadArticle2Webview(cacheDetail.content);
        } else if (!TextUtils.isEmpty(mPostId)) {
            fetchArticleContent();
        } else {
            mWebView.loadUrl(mJobUrl);
        }
    }

    private void fetchArticleContent() {
        String reqUrl = "http://www.devtf.cn/api/v1/?type=article&post_id=" + mPostId;
        HttpFlinger.get(reqUrl, new DataListener<String>() {

            @Override
            public void onComplete(String result) {
                loadArticle2Webview(result);
                DatabaseHelper.getInstance().saveArticleDetails(new ArticleDetail(mPostId, result));
            }
        });
    }

    private void loadArticle2Webview(String htmlContent) {
        mWebView.loadDataWithBaseURL("", HtmlUtil.wrapArticleContent(mTitle, htmlContent),
                "text/html", "utf8", "404");
    }
}
