package com.example.shuozhang.devtf.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by EWorld
 * 2018/1/8 18:47
 *
 * @Description
 * @E-mail 852333743@qq.com
 */

public class AutoLoadRecyclerView extends RecyclerView {
    onLoadListener mLoadListener;
    boolean isLoading = false;
    boolean isValidDelay = true;
    public AutoLoadRecyclerView(Context context) {
        this(context,null);
    }

    public AutoLoadRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AutoLoadRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO: 2018/1/9 查询下isInEditMode是什么
        if (isInEditMode()){
            return;
        }
        init();
    }

    private void init() {
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                checkLoadMore(dx,dy);
            }
        });
    }

    private void checkLoadMore(int dx, int dy) {
        if (isBottom(dx,dy) && !isLoading && isValidDelay && mLoadListener != null){
            isValidDelay = false;
            mLoadListener.onLoad();
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    isValidDelay = true;
                }
            }, 1000);
        }
    }

    private boolean isBottom(int dx,int dy){
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        int totalItemCount = layoutManager.getItemCount();
        return lastVisibleItem >= totalItemCount - 4 && dy > 0;
    }

    public void setOnLoadListener(onLoadListener listener){
        mLoadListener = listener;
    }

    public static interface onLoadListener{
        void onLoad();
    }
}
