package com.example.shuozhang.devtf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EWorld
 * 2018/1/8 20:00
 *
 * @Description 处理文章的存储于查询
 * @E-mail 852333743@qq.com
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    //表1-articles，存储文章的基本信息
    private static final String CREATE_ARTICLE_TABLE_SQL = "CREATE TABLE articles ( "
            + " postId INTEGER PRIMARY KEY UNIQUE,"
            + " author VARCHAR(30) NOT NULL,"
            + " title VARCHAR(50) NOT NULL,"
            + " category INTEGER,"
            + " publishTime VARCHAR(50)"
            + " )";
    //表2-article_content,存储文章id与文章的内容
    private static final String CREATE_ARTICLE_CONTENT_TABLE_SQL = "CREATE TABLE article_content ( "
            + " postId INTEGER PRIMARY KEY UNIQUE, "
            + " content TEXT NOT NULL "
            + " )";
    static final String DB_NAME = "tech_frontier.db";
    static final int DB_VERSION = 1;
    public static final String TABLE_ARTICLES = "articles";
    public static final String TABLE_ARTICLE_CONTENT = "article_content";
    private SQLiteDatabase mDatabase;
    static DatabaseHelper sDatabaseHelper;

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mDatabase = getWritableDatabase();
    }

    public static void init(Context context) {
        if (sDatabaseHelper == null) {
            sDatabaseHelper = new DatabaseHelper(context);
        }
    }

    public static DatabaseHelper getInstance() {
        if (sDatabaseHelper == null) {
            throw new NullPointerException("sDatabaseHelper is null,please call init method first.");
        }
        return sDatabaseHelper;
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ARTICLE_TABLE_SQL);
        db.execSQL(CREATE_ARTICLE_CONTENT_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE " + TABLE_ARTICLES);
        db.execSQL("DROP TABLE " + TABLE_ARTICLE_CONTENT);
        onCreate(db);
    }

    /**
     * 存储文章列表
     *
     * @param dataList
     */
    public void saveArticles(List<Article> dataList) {
        for (Article article : dataList) {
            mDatabase.insertWithOnConflict(TABLE_ARTICLES, null, article2ContentValues(article),
                    SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    /**
     * 将文章转换为ContentValues
     *
     * @param item
     * @return
     */
    private ContentValues article2ContentValues(Article item) {
        ContentValues newValues = new ContentValues();
        newValues.put("postId", item.postId);
        newValues.put("author", item.author);
        newValues.put("title", item.title);
        newValues.put("category", item.category);
        newValues.put("publishTime", item.publishTime);
        return newValues;
    }

    public List<Article> loadArticles() {
        Cursor cursor = mDatabase.rawQuery("select * from " + TABLE_ARTICLES, null);
        List<Article> result = parseArticles(cursor);
        cursor.close();
        return result;
    }

    /**
     * 从cursor解析文章列表
     *
     * @param cursor
     * @return
     */
    private List<Article> parseArticles(Cursor cursor) {
        List<Article> articles = new ArrayList<>();
        while (cursor.moveToNext()) {
            Article item = new Article();
            item.postId = cursor.getString(0);
            item.author = cursor.getString(1);
            item.title = cursor.getString(2);
            item.category = cursor.getInt(3);
            item.publishTime = cursor.getString(4);
            //解析数据
            articles.add(item);
        }
        return articles;
    }

    /**
     * 处理存储文章内容
     *
     * @param articleDetail
     */
    public void saveArticleDetails(ArticleDetail articleDetail) {
        mDatabase.insertWithOnConflict(TABLE_ARTICLE_CONTENT, null, articleDetailToContentValues(articleDetail),
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    /**
     * 通过文章id获取文章详情
     *
     * @param postId
     * @return
     */
    public ArticleDetail loadArticleDetail(String postId) {
        Cursor cursor = mDatabase.rawQuery("select * from " + TABLE_ARTICLE_CONTENT + " where postId = "
                + postId, null);
        ArticleDetail detail = new ArticleDetail(postId, parseArticleContent(cursor));
        cursor.close();
        return detail;
    }

    /**
     * 解析文章内容
     *
     * @param cursor
     * @return
     */
    private String parseArticleContent(Cursor cursor) {
        return cursor.moveToNext() ? cursor.getString(1) : "";
    }

    /**
     * 文章详情转换为ContentValues
     *
     * @param detail
     * @return
     */
    protected ContentValues articleDetailToContentValues(ArticleDetail detail) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("post_id", detail.postId);
        contentValues.put("content", detail.content);
        return contentValues;
    }

}
