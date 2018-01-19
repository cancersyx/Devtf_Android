package com.example.shuozhang.devtf;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by EWorld
 * 2018/1/10 12:35
 *
 * @Description 文章阅读页面，使用WebView加载
 * @E-mail 852333743@qq.com
 */

public class ArticleDetailActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private WebView mWebView;
    private String mPostId;
    private String mTitle;
    private String mJobUrl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        mProgressBar = findViewById(R.id.loading_progressbar);
        mWebView = findViewById(R.id.articles_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                WebSettings settings = mWebView.getSettings();
                settings.setBuiltInZoomControls(true);
                view.loadUrl(url);
                return true;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(newProgress);
                if (newProgress == 100){
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle extraBundle = getIntent().getExtras();
        if (extraBundle != null && !extraBundle.containsKey("job_url")){
            mPostId = extraBundle.getString("postId");
            mTitle = extraBundle.getString("title");
        }else {
            mJobUrl = extraBundle.getString("job_url");
        }

        ArticleDetail cacheDetail = DatabaseHelper.getInstance().loadArticleDetail(mPostId);
        if (!TextUtils.isEmpty(cacheDetail.content)){
            loadArticle2Webview(cacheDetail.content);
        }else if (!TextUtils.isEmpty(mPostId)){
            getArticleContent();
        }else {
            mWebView.loadUrl(mJobUrl);
        }
    }

    private void getArticleContent(){
        new AsyncTask<Void,Void,String>(){

            @Override
            protected String doInBackground(Void... params) {
                HttpURLConnection urlConnection = null;
                try{
                    urlConnection = (HttpURLConnection) new
                            URL("http://www.devtf.cn/api/v1/?type=article&post_id=" + mPostId).openConnection();
                    urlConnection.connect();
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    InputStream netsInputStream = urlConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(netsInputStream));
                    while ((line = bufferedReader.readLine()) != null){
                        sb.append(line).append("\n");
                        return sb.toString();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    urlConnection.disconnect();
                }
                return "";
            }

            @Override
            protected void onPostExecute(String result) {
                loadArticle2Webview(result);
                DatabaseHelper.getInstance().saveArticleDetails(new ArticleDetail(mPostId,result));
            }
        }.execute();
    }

    private void loadArticle2Webview(String htmlContent) {
        mWebView.loadDataWithBaseURL("", wrapHtml(mTitle, htmlContent),
                "text/html", "utf8", "404");
    }

    private static String wrapHtml(String title, String content) {
        final StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>");
        sb.append("<html dir=\"ltr\" lang=\"zh\">");
        sb.append("<head>");
        sb.append("<meta name=\"viewport\" content=\"width=100%; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;\" />");
        sb.append("<link rel=\"stylesheet\" href='file:///android_asset/style.css' type=\"text/css\" media=\"screen\" />");
        sb.append("<link rel=\"stylesheet\" href='file:///android_asset/default.min.css' type=\"text/css\" media=\"screen\" />");
        sb.append("</head>");
        sb.append("<body style=\"padding:0px 8px 8px 8px;\">");
        sb.append("<div id=\"pagewrapper\">");
        sb.append("<div id=\"mainwrapper\" class=\"clearfix\">");
        sb.append("<div id=\"maincontent\">");
        sb.append("<div class=\"post\">");
        sb.append("<div class=\"posthit\">");
        sb.append("<div class=\"postinfo\">");
        sb.append("<h2 class=\"thetitle\">");
        sb.append("<a>");
        sb.append(title);
        sb.append("</a>");
        sb.append("</h2>");
        sb.append("<hr/>");
        sb.append("</div>");
        sb.append("<div class=\"entry\">");
        sb.append(content);
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("<script src=\'file:///android_asset/highlight.pack.js\'></script>");
        sb.append("<script>hljs.initHighlightingOnLoad();</script>");
        sb.append("</body>");
        sb.append("</html>");
        Log.e("zsf", "html : " + sb.toString());
        return sb.toString();
    }

    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    public void onError(VolleyError error) {
        hideLoading();
    }
}
