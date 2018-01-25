package com.example.shuozhang.devtf.activity;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.shuozhang.devtf.R;
import com.example.shuozhang.devtf.adapter.MenuAdapter;
import com.example.shuozhang.devtf.bean.MenuItem;
import com.example.shuozhang.devtf.fragment.ArticleListFragment;
import com.example.shuozhang.devtf.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActionBarActivity {

    protected FragmentManager mFragmentManager;
    private Fragment mArticleFragment = new ArticleListFragment();//文章列表Fragment
    private Fragment mAboutFragment;//关于Fragment
    private DrawerLayout mDrawerLayout;//菜单布局
    private RecyclerView mMenuRecyclerView;//菜单RecyclerView
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidgets() {
        mFragmentManager = getSupportFragmentManager();
        setupDrawerToggle();
        setupMenuRecyclerView();
        //显示文章列表fragment
        addFragment(mArticleFragment);
    }

    private void setupDrawerToggle() {
        mDrawerLayout = findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.drawer_open, R.string.drawer_close);
        //该方法会自动和actionBar关联, 将开关的图片显示在了action上，如果不设置，也可以有抽屉的效果，不过是默认的图标
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void setupMenuRecyclerView() {
        mMenuRecyclerView = findViewById(R.id.rv_menu);
        mMenuRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        MenuAdapter menuAdapter = new MenuAdapter();
        menuAdapter.addItems(prepareMenuItems());
        menuAdapter.setOnItemClickListener(new OnItemClickListener<MenuItem>() {
            @Override
            public void onClick(MenuItem item) {
                clickMenuItem(item);
            }
        });
        mMenuRecyclerView.setAdapter(menuAdapter);

    }

    private List<MenuItem> prepareMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(getString(R.string.article), R.drawable.home));
        menuItems.add(new MenuItem(getString(R.string.about_menu), R.drawable.about));
        menuItems.add(new MenuItem(getString(R.string.exit), R.drawable.exit));
        return menuItems;
    }

    private void clickMenuItem(MenuItem item) {
        mDrawerLayout.closeDrawers();
        switch (item.iconResId) {
            case R.drawable.home:
                replaceFragment(mArticleFragment);
                break;
            case R.drawable.about:
                replaceFragment(mAboutFragment);
                break;
            case R.drawable.exit:
                isQuit();
                break;
        }
    }

    protected void addFragment(Fragment fragment) {
        mFragmentManager.beginTransaction().add(R.id.articles_container, fragment).commit();
    }

    protected void replaceFragment(Fragment fragment) {
        if (fragment != null){
            mFragmentManager.beginTransaction().replace(R.id.articles_container, fragment).commit();
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
