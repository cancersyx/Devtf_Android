package com.example.shuozhang.devtf;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    protected FragmentManager mFragmentManager;
    private Fragment mArticleFragment = new ArticleFragment();//文章列表Fragment
    private Fragment mAboutFragment;//关于Fragment
    private DrawerLayout mDrawerLayout;//菜单布局
    private RecyclerView mMenuRecyclerView;//菜单RecyclerView
    protected Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentManager = getSupportFragmentManager();

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

        mDrawerLayout = findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        //该方法会自动和actionBar关联, 将开关的图片显示在了action上，如果不设置，也可以有抽屉的效果，不过是默认的图标
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mMenuRecyclerView = findViewById(R.id.rv_menu);
        mMenuRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        List<MenuItem> menuItemList = new ArrayList<>();
        menuItemList.add(new MenuItem(getString(R.string.article), R.drawable.home));
        menuItemList.add(new MenuItem(getString(R.string.about_menu), R.drawable.about));
        menuItemList.add(new MenuItem(getString(R.string.exit), R.drawable.exit));

        MenuAdapter menuAdapter = new MenuAdapter(menuItemList);
        menuAdapter.setOnItemClickListener(new OnItemClickListener<MenuItem>() {
            @Override
            public void onClick(MenuItem item) {
                clickMenuItem(item);
            }
        });
        mMenuRecyclerView.setAdapter(menuAdapter);

        mFragmentManager.beginTransaction().add(R.id.articles_container, mArticleFragment).commitAllowingStateLoss();


    }

    private void clickMenuItem(MenuItem item) {
        mDrawerLayout.closeDrawers();
        switch (item.iconResId) {
            case R.drawable.home:
                mFragmentManager.beginTransaction()
                        .replace(R.id.articles_container, mArticleFragment)
                        .commit();
                break;

            case R.drawable.about:
                if (mAboutFragment == null) {
                    mAboutFragment = new AboutFragment();
                }
                mFragmentManager.beginTransaction()
                        .replace(R.id.articles_container, mAboutFragment)
                        .commit();
                break;

            case R.drawable.exit:
                isQuit();
                break;
        }
    }

    private void isQuit() {
        new AlertDialog.Builder(this)
                .setTitle("确认退出？").setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        }).setNegativeButton("取消", null).create().show();
    }
}
