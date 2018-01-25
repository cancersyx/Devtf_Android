package com.example.shuozhang.devtf.parser;

import org.json.JSONException;

/**
 * @author EWorld
 *         2018/1/22 15:35
 * @e-mail 852333743@qq.com
 * @description 网络请求结果解析器
 */

public interface RespParser<T> {
    public T parseResponse(String result) throws JSONException;
}
