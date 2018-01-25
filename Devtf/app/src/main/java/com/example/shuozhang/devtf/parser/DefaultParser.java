package com.example.shuozhang.devtf.parser;

import org.json.JSONException;

/**
 * @author EWorld
 *         2018/1/22 15:46
 * @e-mail 852333743@qq.com
 * @description
 */

public class DefaultParser implements RespParser<String> {
    @Override
    public String parseResponse(String result) throws JSONException {
        return result;
    }
}
