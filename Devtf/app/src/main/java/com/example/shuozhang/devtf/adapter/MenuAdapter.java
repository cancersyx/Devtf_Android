package com.example.shuozhang.devtf.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shuozhang.devtf.R;
import com.example.shuozhang.devtf.bean.MenuItem;
import com.example.shuozhang.devtf.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shuozhang on 2018/1/2.
 * @version 2.0
 */

public class MenuAdapter extends RecyclerBaseAdapter<MenuItem,MenuAdapter.MenuViewHolder>{
    @Override
    protected void bindDataToItemView(MenuViewHolder viewHolder, MenuItem item) {
        viewHolder.nameTextView.setText(item.text);
        viewHolder.userImageView.setImageResource(item.iconResId);
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MenuViewHolder(inflateItemView(parent,R.layout.menu_item));
    }
//    List<MenuItem> mDataSet = new ArrayList<>();
//    OnItemClickListener<MenuItem> mItemClickListener;
//
//    public MenuAdapter(List<MenuItem> dataSet) {
//        mDataSet = dataSet;
//    }
//
//
//    @Override
//    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new MenuViewHolder(inflateItemView(parent, R.layout.menu_item));
//    }
//
//    @Override
//    public void onBindViewHolder(MenuViewHolder viewHolder, int position) {
//        final MenuItem item = getItem(position);
//        viewHolder.nameTextView.setText(item.text);
//        viewHolder.userImageView.setImageResource(item.iconResId);
//        setupItemViewClickListener(viewHolder, item);
//    }
//
//    public void setOnItemClickListener(OnItemClickListener<MenuItem> clickListener) {
//        this.mItemClickListener = clickListener;
//    }
//
//    protected MenuItem getItem(int position) {
//        return mDataSet.get(position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return mDataSet.size();
//    }
//
//    protected void setupItemViewClickListener(MenuViewHolder viewHolder, final MenuItem item) {
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mItemClickListener != null) {
//                    mItemClickListener.onClick(item);
//                }
//            }
//        });
//    }
//
//    protected View inflateItemView(ViewGroup viewGroup, int layoutId) {
//        return LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
//    }

    class MenuViewHolder extends RecyclerView.ViewHolder {
        public ImageView userImageView;
        public TextView nameTextView;

        public MenuViewHolder(View itemView) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.iv_menu_icon_imageview);
            nameTextView = itemView.findViewById(R.id.tv_menu_text);
        }
    }
}
