package com.example.shuozhang.devtf.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.shuozhang.devtf.R;

/**
 * @author EWorld
 *         2018/1/21 22:40
 * @e-mail 852333743@qq.com
 * @description 用来针对第一版本中MainActivity和ArticleDetailActivity都需要去初始化Toolbar
 */

public abstract class BaseActionBarActivity extends AppCompatActivity{
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewResId());
        setupToolbar();
        initWidgets();
    }

    /**
     * 获取Activity的布局id
     * @return
     */
    protected abstract int getContentViewResId();

    protected void setupToolbar(){
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
    }

    protected void initWidgets(){}

    protected void afterOnCreate(){}
}
