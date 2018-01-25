package com.example.shuozhang.devtf.parser;

import android.text.TextUtils;

import com.example.shuozhang.devtf.bean.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author EWorld
 *         2018/1/22 15:49
 * @e-mail 852333743@qq.com
 * @description 解析文章列表的解析器具体实现。将服务器返回的json数据转换为文章列表的解析器
 */

public class ArticleParser implements RespParser<List<Article>> {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * 将String ->JsonArray,再从JsonArray中将JsonObject依次解析为Article，并添加到List中。
     * @param result
     * @return
     * @throws JSONException
     */
    @Override
    public List<Article> parseResponse(String result) throws JSONException {
        JSONArray jsonArray = new JSONArray(result);
        List<Article> articleList = new LinkedList<>();
        int count = jsonArray.length();
        for (int i = 0; i < count; i++) {
            JSONObject itemObject = jsonArray.optJSONObject(i);
            articleList.add(parseItem(itemObject));
        }
        return articleList;
    }


    private Article parseItem(JSONObject itemObject) {
        Article articleItem = new Article();
        articleItem.title = itemObject.optString("title");
        articleItem.author = itemObject.optString("author");
        articleItem.post_id = itemObject.optString("post_id");
        String category = itemObject.optString("category");
        articleItem.category = TextUtils.isEmpty(category) ? 0 : Integer.valueOf(category);
        articleItem.publishTime = formatDate(dateFormat, itemObject.optString("data"));
        return articleItem;
    }

    private static String formatDate(SimpleDateFormat dateFormat, String dateString) {
        try {
            Date date = dateFormat.parse(dateString);
            return dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
