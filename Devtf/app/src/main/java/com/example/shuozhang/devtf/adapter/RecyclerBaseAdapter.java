package com.example.shuozhang.devtf.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shuozhang.devtf.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author EWorld
 * 2018/1/21 22:06
 * @e-mail 852333743@qq.com
 * @description
 */

/**
 * 适用于RecyclerView的抽象Adapter，封装了数据集、ViewHolder的创建与绑定过程，简化子类的操作
 *
 * @param <D>
 * @param <V>
 */
public abstract class RecyclerBaseAdapter<D, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {
    /**
     * RecyclerView中的数据集，使用final来定义避免外部原因使得数据源发生改变，外部只需要把数据添加到Adapter中即可。
     */
    protected final List<D> mDataSet = new ArrayList<>();

    /**
     * 点击事件处理回调
     */
    private OnItemClickListener<D> mItemClickListener;

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    /**
     * 获取某个位置下的数据
     *
     * @param position
     * @return
     */
    protected D getItem(int position) {
        return mDataSet.get(position);
    }

    /**
     * 数据添加到数据集中
     *
     * @param items
     */
    public void addItems(List<D> items) {
        items.removeAll(mDataSet);//去除已经存在的数据
        mDataSet.addAll(items);//更新数据
        notifyDataSetChanged();
    }

    /**
     * 这是重要的一个函数！！！！！
     * 绑定数据，主要分为两步，绑定数据与设置每项的点击事件处理
     *
     * @param holder
     * @param position
     */
    @Override
    public final void onBindViewHolder(V holder, int position) {
        //1.首先获取position位置的数据
        final D item = getItem(position);
        //2.调用bindDataToItemView函数来绑定数据，它是抽象的，所以子类必须覆写它来实现具体的数据绑定
        bindDataToItemView(holder, item);
        //3.设置每项数据的点击事件
        setupItemViewClickListener(holder, item);
    }

    protected View inflateItemView(ViewGroup viewGroup, int layoutId) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
    }

    public void setOnItemClickListener(OnItemClickListener<D> mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    /**
     * ItemView的点击事件
     *
     * @param viewHolder
     * @param item
     */
    protected void setupItemViewClickListener(V viewHolder, final D item) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null) {
                    mItemClickListener.onClick(item);
                }
            }
        });
    }

    /**
     * 将数据绑定到ItemView上
     *
     * @param viewHolder
     * @param item
     */
    protected abstract void bindDataToItemView(V viewHolder, D item);
}
