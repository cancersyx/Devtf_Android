package com.example.shuozhang.devtf;

/**
 * Created by EWorld
 * 2018/1/9 00:03
 *
 * @Description
 * @E-mail 852333743@qq.com
 */

public class ArticleDetail {
    public String postId;
    public String content;

    public ArticleDetail() {
    }
    public ArticleDetail(String pId,String html){
        postId = pId;
        content = html;
    }
}
