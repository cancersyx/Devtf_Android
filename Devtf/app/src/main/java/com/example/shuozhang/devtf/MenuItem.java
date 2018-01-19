package com.example.shuozhang.devtf;

/**
 * Created by shuozhang on 2018/1/2.
 */

public class MenuItem {
    public int iconResId;
    public String text;

    public MenuItem() {
    }

    public MenuItem(String text, int resId) {
        this.text = text;
        iconResId = resId;
    }

}
