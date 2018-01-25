package com.example.shuozhang.devtf.net;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.example.shuozhang.devtf.listener.DataListener;
import com.example.shuozhang.devtf.parser.DefaultParser;
import com.example.shuozhang.devtf.parser.RespParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author EWorld
 *         2018/1/22 15:31
 * @e-mail 852333743@qq.com
 * @description 网络执行引擎
 */

public final class HttpFlinger {
    private static final DefaultParser DEFAULT_PARSER = new DefaultParser();

    public HttpFlinger() {
    }

    public static void get(String reqUrl, DataListener<String> listener) {
        get(reqUrl, DEFAULT_PARSER, listener);
    }

    /**
     * 泛型函数，
     * @param reqUrl 请求url
     * @param parser 网络请求结果的解析器
     * @param listener 用于回调解析器解析到的结果的listener
     * @param <T>
     */
    @SuppressLint("StaticFieldLeak")
    public static <T> void get(final String reqUrl, final RespParser<T> parser, final DataListener<T> listener) {
        //创建异步任务
        new AsyncTask<Void, Void, T>() {
            @Override
            protected T doInBackground(Void... params) {
                HttpURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpURLConnection) new URL(reqUrl).openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setConnectTimeout(8000);
                    urlConnection.setReadTimeout(8000);
                    urlConnection.connect();
                    String result = streamToString(urlConnection.getInputStream());
                    //解析数据
                    return parser.parseResponse(result);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(T result) {
                //回调结果
                if (listener != null) {
                    listener.onComplete(result);
                }
            }
        }.execute();
    }

    private static String streamToString(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line = null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }


}
