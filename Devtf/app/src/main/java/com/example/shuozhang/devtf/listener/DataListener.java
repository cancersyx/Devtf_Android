package com.example.shuozhang.devtf.listener;

/**
 * @author EWorld
 *         2018/1/22 15:36
 * @e-mail 852333743@qq.com
 * @description
 */

//数据回调
public interface DataListener<T> {
    void onComplete(T result);
}
